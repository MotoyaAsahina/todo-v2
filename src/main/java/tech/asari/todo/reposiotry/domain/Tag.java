package tech.asari.todo.reposiotry.domain;

import tech.asari.todo.controller.domain.RequestTag;

import java.sql.Timestamp;

public record Tag(
        int id,
        String name,
        String classification,
        String mainColor,
        String borderColor,
        Timestamp createdAt,
        Timestamp archivedAt,
        Timestamp deletedAt
) {

    public static Tag of(RequestTag requestTag) {
        return new Tag(
                0,
                requestTag.name(),
                requestTag.classification(),
                requestTag.mainColor(),
                requestTag.borderColor(),
                null,
                null,
                null
        );
    }

    public static Tag of(RequestTag requestTag, Timestamp createdAt) {
        return new Tag(
                0,
                requestTag.name(),
                requestTag.classification(),
                requestTag.mainColor(),
                requestTag.borderColor(),
                createdAt,
                null,
                null
        );
    }

    public static Tag of(int id, Tag tag) {
        return new Tag(
                id,
                tag.name(),
                tag.classification(),
                tag.mainColor(),
                tag.borderColor(),
                tag.createdAt(),
                tag.archivedAt(),
                tag.deletedAt()
        );
    }
}
