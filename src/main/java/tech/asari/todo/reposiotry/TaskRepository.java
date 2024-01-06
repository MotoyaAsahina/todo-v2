package tech.asari.todo.reposiotry;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tech.asari.todo.reposiotry.domain.Task;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository implements ITaskRepository {

    private final JdbcClient client;

    public TaskRepository(JdbcClient client) {
        this.client = client;
    }

    @Override
    public List<Task> getAll(String status, boolean deleted) {
        String sql = "SELECT * FROM tasks"
                + (deleted ? " WHERE deleted_at IS NOT NULL" : " WHERE deleted_at IS NULL");

        switch (status) {
            case "done" -> sql += " AND done_at IS NOT NULL";
            case "undone" -> sql += " AND done_at IS NULL";
            case "all" -> sql += "";
            default -> throw new IllegalArgumentException("status must be one of 'done', 'undone', 'all'");
        }

        return client.sql(sql)
                .query(Task.class)
                .list();
    }

    @Override
    public Optional<Task> get(int id) {
        return client.sql("SELECT * FROM tasks WHERE id = :id")
                .param("id", id)
                .query(Task.class)
                .optional();
    }

    @Override
    public void setPending(int id, boolean pending) {
        client.sql("UPDATE tasks SET pending = :pending WHERE id = :id")
                .param("id", id)
                .param("pending", pending)
                .update();
    }

    @Override
    public void setPinned(int id, boolean pinned) {
        client.sql("UPDATE tasks SET pinned = :pinned WHERE id = :id")
                .param("id", id)
                .param("pinned", pinned)
                .update();
    }

    @Override
    public void setDone(int id, boolean done) {
        client.sql("UPDATE tasks SET done_at = :done_at WHERE id = :id")
                .param("id", id)
                .param("done_at", done ? new Timestamp(System.currentTimeMillis()) : null)
                .update();
    }

    @Override
    public void delete(int id) {
        client.sql("UPDATE tasks SET deleted_at = :deleted_at WHERE id = :id")
                .param("id", id)
                .param("deleted_at", new Timestamp(System.currentTimeMillis()))
                .update();
    }

    @Override
    public void restore(int id) {
        client.sql("UPDATE tasks SET deleted_at = :deleted_at WHERE id = :id")
                .param("id", id)
                .param("deleted_at", null)
                .update();
    }

    @Override
    public Task update(int id, Task task) {
        client.sql("""
                        UPDATE tasks
                        SET group_id = :group_id, title = :title, description = :description,
                            `order` = :order, due_date = :due_date
                        WHERE id = :id
                        """)
                .param("id", id)
                .param("group_id", task.groupId())
                .param("title", task.title())
                .param("description", task.description())
                .param("order", task.order())
                .param("due_date", task.dueDate())
                .update();

        return get(id).orElseThrow(() -> new RuntimeException("Failed to update task"));
    }

    @Override
    public Task create(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("""
                        INSERT INTO tasks (group_id, title, description, `order`, due_date, created_at)
                        VALUES (:group_id, :title, :description, :order, :due_date, :created_at)
                        """)
                .param("group_id", task.groupId())
                .param("title", task.title())
                .param("description", task.description())
                .param("order", task.order())
                .param("due_date", task.dueDate())
                .param("created_at", task.createdAt())
                .update(keyHolder, "id");

        if (keyHolder.getKey() == null)
            throw new RuntimeException("Failed to create task");

        return Task.of(keyHolder.getKey().intValue(), task);
    }
}
