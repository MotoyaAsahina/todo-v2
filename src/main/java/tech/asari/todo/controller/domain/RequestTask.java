package tech.asari.todo.controller.domain;

import java.sql.Timestamp;
import java.util.List;

public record RequestTask(
        int groupId,
        String title,
        String description,
        Integer order,
        Timestamp dueDate,
        List<Integer> tags
) {
}
