package tech.asari.todo.reposiotry.domain;

import java.sql.Timestamp;

public record NotificationTime(
        Timestamp time,
        boolean noticed,
        Timestamp noticedAt,
        int taskCount
) {

    public static NotificationTime of(Timestamp time) {
        return new NotificationTime(
                time,
                false,
                null,
                0
        );
    }
}
