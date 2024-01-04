package tech.asari.todo.controller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public record RequestPostTask(
        @JsonProperty("group_id")
        int groupId,
        String title,
        String description,
        Integer order,
        @JsonProperty("due_date")
        Timestamp dueDate,
        List<Integer> tags
) {
}
