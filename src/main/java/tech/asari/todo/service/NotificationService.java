package tech.asari.todo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import tech.asari.todo.reposiotry.INotificationRepository;
import tech.asari.todo.reposiotry.domain.Notification;
import tech.asari.todo.reposiotry.domain.NotificationTime;
import tech.asari.todo.util.NotificationTagParser;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService implements INotificationService {

    private final INotificationRepository notificationRepo;

    public NotificationService(INotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Override
    public void schedule(int taskId, List<String> notificationTags, Timestamp dueDate) {
        List<Notification> notifications = notificationTags.stream()
                .filter(NotificationTagParser::validate)
                .map(tag -> new Notification(NotificationTagParser.parse(tag, dueDate), taskId, tag))
                .filter(notification -> notification.time().after(new Timestamp(System.currentTimeMillis())))
                .toList();

        List<Timestamp> notificationTimes =
                notificationRepo.createNotificationTimesIfNotExists(
                        notifications.stream().map(notification -> NotificationTime.of(notification.time())).toList());

        notificationRepo.deleteNotifications(taskId);
        notificationRepo.createNotifications(notifications);

        notificationTimes.forEach(this::scheduleNotification);
    }

    @PostConstruct
    public void init() {
        // Initialize notification scheduler
        List<Timestamp> notificationTimes = notificationRepo.getNotificationTimesNotNoticed();
        notificationTimes.forEach(this::scheduleNotification);
    }

    private void scheduleNotification(Timestamp time) {
        // Schedule notification
        Thread thread = new Thread(() -> {
            try {
                long sleepTime = time.getTime() - System.currentTimeMillis();
                if (sleepTime > 0) { // NOTE: Repository の実装から、sleepTime > 0 は保証されている
                    TimeUnit.MILLISECONDS.sleep(sleepTime);

                    // TODO: Post notification

                    notificationRepo.setNotificationTimeNoticed(time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
