package tech.asari.todo.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.asari.todo.reposiotry.IGroupRepository;
import tech.asari.todo.reposiotry.INotificationRepository;
import tech.asari.todo.reposiotry.ITagRepository;
import tech.asari.todo.reposiotry.domain.Group;
import tech.asari.todo.reposiotry.domain.Notification;
import tech.asari.todo.reposiotry.domain.Tag;
import tech.asari.todo.reposiotry.domain.Task;
import tech.asari.todo.util.DateFormat;
import tech.asari.todo.util.NotificationTagParser;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    @Value("${slack.bot.token}")
    private String slackToken;
    @Value("${slack.bot.channel}")
    private String slackChannel;

    private final ITagRepository tagRepository;
    private final IGroupRepository groupRepository;
    private final INotificationRepository notificationRepo;

    public NotificationService(
            ITagRepository tagRepository,
            IGroupRepository groupRepository,
            INotificationRepository notificationRepo
    ) {
        this.tagRepository = tagRepository;
        this.groupRepository = groupRepository;
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
                notificationRepo.createNotificationTimesIfNotExists(notifications.stream().map(Notification::time).toList());

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

    public void scheduleNotification(Timestamp time) {
        // Schedule notification
        Thread thread = new Thread(() -> {
            try {
                // NOTE: NotificationRepository と schedule メソッドの実装から、sleepTime > 0 は保証されている
                long sleepTime = time.getTime() - System.currentTimeMillis();
                TimeUnit.MILLISECONDS.sleep(sleepTime);

                // Notify
                notify(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public void notify(Timestamp notificationTime) {
        List<Task> tasks = notificationRepo.getActiveTasksByNotificationTime(notificationTime);

        Map<Integer, Group> groups =
                groupRepository.getAll(false).stream().collect(Collectors.toMap(Group::id, Function.identity()));
        Map<Integer, Tag> tags =
                tagRepository.getAllTags(false).stream().collect(Collectors.toMap(Tag::id, Function.identity()));
        Map<Integer, String> notificationTags = notificationRepo.getNotificationTags(notificationTime);

        String message = "Tasks to do:\n" +
                tasks.stream().map(task -> {
                    List<Integer> tagIds = tagRepository.getTagMaps(task.id());
                    return "%s (%s): %s (Remaining %s)".formatted(
                            task.title(),
                            groups.get(task.groupId()).name() + (tagIds.isEmpty() ? "" :
                                    ", " + tagIds.stream().map(tags::get).map(Tag::name).collect(Collectors.joining(", "))),
                            DateFormat.NOTIFICATION().format(task.dueDate()),
                            notificationTags.get(task.id())
                    );
                }).collect(Collectors.joining("\n"));

        try {
            Slack.getInstance().methods(slackToken).chatPostMessage(req -> req
                    .channel(slackChannel)
                    .text(message));
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }

        notificationRepo.setNotificationTimeNoticed(notificationTime, tasks.size());
    }
}
