package tech.asari.todo.controller.domain;

import tech.asari.todo.reposiotry.domain.Group;
import tech.asari.todo.reposiotry.domain.HasDueDate;

import java.sql.Timestamp;

public record ResponseGroup(
        int id,
        String name,
        String description,
        HasDueDate hasDueDate,
        String classifiedBy,
        int order,
        Timestamp createdAt,
        Timestamp archivedAt,
        Timestamp deletedAt
) {

    public ResponseGroup(Group group) {
        this(
                group.id(),
                group.name(),
                group.description(),
                group.hasDueDate(),
                group.classifiedBy(),
                group.order(),
                group.createdAt(),
                group.archivedAt(),
                group.deletedAt()
        );
    }
}
