package tech.asari.todo.controller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;

public record ResponseTask(
        int id,
        @JsonProperty("group_id")
        int groupId,
        String title,
        String description,
        boolean pending,
        boolean pinned,
        Integer order,
        @JsonProperty("due_date")
        Timestamp dueDate,
        @JsonProperty("done_at")
        Timestamp doneAt,
        @JsonProperty("created_at")
        Timestamp createdAt,
        @JsonProperty("deleted_at")
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
