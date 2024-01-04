package tech.asari.todo.reposiotry.domain;

import tech.asari.todo.controller.domain.RequestPostTask;

import java.sql.Timestamp;

public record Task(
        int id,
        int groupId,
        String title,
        String description,
        boolean pending,
        boolean pinned,
        Integer order,
        Timestamp dueDate,
        Timestamp doneAt,
        Timestamp createdAt,
        Timestamp deletedAt
) {

    public static Task of(RequestPostTask requestPostTask) {
        return new Task(
                0,
                requestPostTask.groupId(),
                requestPostTask.title(),
                requestPostTask.description(),
                false,
                false,
                requestPostTask.order(),
                requestPostTask.dueDate(),
                null,
                new Timestamp(System.currentTimeMillis()),
                null
        );
    }

    public static Task of(int id, Task task) {
        return new Task(
                id,
                task.groupId(),
                task.title(),
                task.description(),
                task.pending(),
                task.pinned(),
                task.order(),
                task.dueDate(),
                task.doneAt(),
                task.createdAt(),
                task.deletedAt()
        );
    }
}
