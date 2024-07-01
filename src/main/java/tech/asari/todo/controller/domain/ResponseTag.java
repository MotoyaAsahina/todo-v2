package tech.asari.todo.controller.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.asari.todo.reposiotry.domain.Tag;

import java.sql.Timestamp;

public record ResponseTag(
        int id,
        String name,
        @Schema(nullable = true)
        String classification,
        String mainColor,
        @Schema(nullable = true)
        String borderColor,
        Timestamp createdAt,
        @Schema(nullable = true)
        Timestamp archivedAt,
        @Schema(nullable = true)
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
