package tech.asari.todo.controller.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RequestTask(
        int groupId,
        String title,
        String description,
        @Schema(nullable = true)
        Integer order,
        @Schema(nullable = true)
        String dueDate,
        List<Integer> tags
) {
}
