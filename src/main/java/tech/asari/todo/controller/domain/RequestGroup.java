package tech.asari.todo.controller.domain;

import tech.asari.todo.reposiotry.domain.HasDueDate;

public record RequestGroup(
        String name,
        String description,
        HasDueDate hasDueDate,
        String classifiedBy,
        Integer order
) {
}
