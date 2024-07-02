package tech.asari.todo.reposiotry;

import tech.asari.todo.reposiotry.domain.Notification;
import tech.asari.todo.reposiotry.domain.NotificationTime;
import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;

public interface INotificationRepository {

    List<Timestamp> createNotificationTimesIfNotExists(List<NotificationTime> notificationTimes);

    void createNotifications(List<Notification> notifications);

    List<Task> getTasksByNotificationTime(Timestamp time);

    void deleteNotifications(int taskId);

    List<Timestamp> getNotificationTimesNotNoticed();

    void setNotificationTimeNoticed(Timestamp time, int taskCount);
}
