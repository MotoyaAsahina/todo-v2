package tech.asari.todo.reposiotry;

import org.springframework.stereotype.Repository;
import tech.asari.todo.reposiotry.domain.Notification;
import tech.asari.todo.reposiotry.domain.NotificationTime;
import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class NotificationRepository implements INotificationRepository {

    @Override
    public List<Timestamp> createNotificationTimesIfNotExists(List<NotificationTime> notificationTimes) {
        return List.of();
    }

    @Override
    public void createNotifications(List<Notification> notifications) {

    }

    @Override
    public List<Task> getTasksByNotificationTime(Timestamp time) {
        return List.of();
    }

    @Override
    public void deleteNotifications(int taskId) {

    }

    @Override
    public List<Timestamp> getNotificationTimesNotNoticed() {
        // NOTE: 現在時刻よりも後の時間のみ取得する
        return List.of();
    }

    @Override
    public void setNotificationTimeNoticed(Timestamp time, int taskCount) {

    }
}
