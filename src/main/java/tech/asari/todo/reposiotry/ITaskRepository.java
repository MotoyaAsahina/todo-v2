package tech.asari.todo.reposiotry;

import tech.asari.todo.reposiotry.domain.Task;

import java.util.List;

public interface ITaskRepository {
    List<Task> getAll(String status, boolean deleted);

    Task get(int id);

    void setPending(int id, boolean pending);

    void setPinned(int id, boolean pinned);

    void setDone(int id, boolean done);

    void delete(int id);

    void restore(int id);

    Task update(int id, Task task);

    Task create(Task task);
}
