package tech.asari.todo.controller.domain;

import tech.asari.todo.reposiotry.domain.Tag;

import java.sql.Timestamp;

public record ResponseTag(
        int id,
        String name,
        String classification,
        String mainColor,
        String borderColor,
        Timestamp createdAt,
        Timestamp archivedAt,
        Timestamp deletedAt
) {

    public ResponseTag(Tag tag) {
        this(
                tag.id(),
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
