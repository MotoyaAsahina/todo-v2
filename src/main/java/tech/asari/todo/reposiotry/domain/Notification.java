package tech.asari.todo.reposiotry.domain;

import java.sql.Timestamp;

public record Notification(
        Timestamp time,
        int taskId,
        String tag
) {
}
