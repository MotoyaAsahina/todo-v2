package tech.asari.todo.controller.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestTag(
        String name,
        @Schema(nullable = true)
        String classification,
        String mainColor,
        @Schema(nullable = true)
        String borderColor
) {
}
