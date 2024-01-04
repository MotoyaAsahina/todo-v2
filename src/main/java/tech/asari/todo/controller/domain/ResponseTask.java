package tech.asari.todo.controller.domain;

import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;

public record ResponseTask(
        int id,
        int groupId,
        String title,
        String description,
        boolean pending,
        boolean pinned,
        Integer order,
        Timestamp dueDate,
        Timestamp doneAt,
        Timestamp createdAt,
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
