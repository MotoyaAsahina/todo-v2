package tech.asari.todo.controller.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.asari.todo.reposiotry.domain.HasDueDate;

public record RequestGroup(
        String name,
        String description,
        HasDueDate hasDueDate,
        @Schema(nullable = true)
        String classifiedBy,
        @Schema(nullable = true)
        Integer order
) {
}
