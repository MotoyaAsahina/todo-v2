package tech.asari.todo.reposiotry;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import tech.asari.todo.reposiotry.domain.Notification;
import tech.asari.todo.reposiotry.domain.NotificationTime;
import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class NotificationRepository implements INotificationRepository {

    private final JdbcClient client;

    public NotificationRepository(JdbcClient client) {
        this.client = client;
    }

    @Override
    public List<Timestamp> createNotificationTimesIfNotExists(List<NotificationTime> notificationTimes) {
        return notificationTimes.stream().filter(notificationTime -> {
            try {
                client.sql("INSERT INTO notification_times (time) VALUES (:time)")
                        .param("time", notificationTime.time())
                        .update();
            } catch (DuplicateKeyException e) {
                return false;
            }
            return true;
        }).map(NotificationTime::time).toList();
    }

    @Override
    public void createNotifications(List<Notification> notifications) {
        notifications.forEach(notification -> {
            client.sql("INSERT INTO notifications (time, task_id, tag) VALUES (:time, :task_id, :tag)")
                    .param("time", notification.time())
                    .param("task_id", notification.taskId())
                    .param("tag", notification.tag())
                    .update();
        });
    }

    @Override
    public List<Task> getTasksByNotificationTime(Timestamp time) {
        return client.sql("SELECT * FROM tasks WHERE id IN (SELECT task_id FROM notifications WHERE time = :time)")
                .param("time", time)
                .query(Task.class)
                .list();
    }

    @Override
    public List<String> getNotificationTags(int taskId) {
        return client.sql("SELECT tag FROM notifications WHERE task_id = :task_id")
                .param("task_id", taskId)
                .query(String.class)
                .list();
    }

    @Override
    public Map<Integer, List<String>> getAllNotificationTags(String status, boolean deleted) {
        String condition = deleted ? "WHERE deleted_at IS NOT NULL" : "WHERE deleted_at IS NULL";
        switch (status) {
            case "done" -> condition += " AND done_at IS NOT NULL";
            case "undone" -> condition += " AND done_at IS NULL";
            case "all" -> condition += "";
            default -> throw new IllegalArgumentException("status must be one of 'done', 'undone', 'all'");
        }
        return client.sql("""
                        SELECT * FROM notifications
                        WHERE task_id IN (SELECT id FROM tasks %s)
                        """.formatted(condition))
                .query(Notification.class)
                .list()
                .stream()
                .collect(Collectors.groupingBy(Notification::taskId, Collectors.mapping(Notification::tag, Collectors.toList())));
    }

    @Override
    public void deleteNotifications(int taskId) {
        client.sql("DELETE FROM notifications WHERE task_id = :task_id")
                .param("task_id", taskId)
                .update();
    }

    @Override
    public List<Timestamp> getNotificationTimesNotNoticed() {
        return client.sql("SELECT time FROM notification_times WHERE time > NOW()")
                .query(Timestamp.class)
                .list();
    }

    @Override
    public void setNotificationTimeNoticed(Timestamp time, int taskCount) {
        client.sql("""
                        UPDATE notification_times
                        SET noticed = TRUE, noticed_at = NOW(), task_count = :task_count
                        WHERE time = :time
                        """)
                .param("time", time)
                .param("task_count", taskCount)
                .update();
    }
}
