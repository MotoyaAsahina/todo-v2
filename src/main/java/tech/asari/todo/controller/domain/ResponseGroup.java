package tech.asari.todo.controller.domain;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(nullable = true)
        Timestamp archivedAt,
        @Schema(nullable = true)
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
