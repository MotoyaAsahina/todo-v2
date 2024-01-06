package tech.asari.todo.reposiotry.domain;

import tech.asari.todo.controller.domain.RequestGroup;

import java.sql.Timestamp;

public record Group(
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

    public static Group of(RequestGroup requestGroup) {
        return new Group(
                0,
                requestGroup.name(),
                requestGroup.description(),
                requestGroup.hasDueDate(),
                requestGroup.classifiedBy(),
                requestGroup.order(),
                null,
                null,
                null
        );
    }

    public static Group of(RequestGroup requestGroup, int order, Timestamp createdAt) {
        return new Group(
                0,
                requestGroup.name(),
                requestGroup.description(),
                requestGroup.hasDueDate(),
                requestGroup.classifiedBy(),
                order,
                createdAt,
                null,
                null
        );
    }

    public static Group of(int id, Group group) {
        return new Group(
                id,
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
