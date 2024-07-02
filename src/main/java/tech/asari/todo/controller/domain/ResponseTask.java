package tech.asari.todo.controller.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;

// TODO: Add List<String> notificationTags

public record ResponseTask(
        int id,
        int groupId,
        String title,
        String description,
        boolean pending,
        boolean pinned,
        @Schema(nullable = true)
        Integer order,
        @Schema(nullable = true)
        Timestamp dueDate,
        @Schema(nullable = true)
        Timestamp doneAt,
        Timestamp createdAt,
        @Schema(nullable = true)
        Timestamp deletedAt,
        List<Integer> tags
) {
    public ResponseTask(Task task, List<Integer> tags) {
        this(
                task.id(),
                task.groupId(),
                task.title(),
                task.description(),
                task.pending(),
                task.pinned(),
                task.order(),
                task.dueDate(),
                task.doneAt(),
                task.createdAt(),
                task.deletedAt(),
                tags == null ? List.of() : tags
        );
    }
}
