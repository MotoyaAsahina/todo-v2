package tech.asari.todo.controller.domain;

public record RequestTag(
        String name,
        String classification,
        String mainColor,
        String borderColor
) {
}
