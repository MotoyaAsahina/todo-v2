package tech.asari.todo.service;

import java.sql.Timestamp;
import java.util.List;

public interface INotificationService {

    void schedule(int taskId, List<String> notificationTags, Timestamp dueDate);
}
