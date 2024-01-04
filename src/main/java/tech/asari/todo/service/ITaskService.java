package tech.asari.todo.service;

import tech.asari.todo.controller.domain.RequestTask;
import tech.asari.todo.controller.domain.ResponseTask;

import java.util.List;

public interface ITaskService {

    List<ResponseTask> getTasks(String status, boolean deleted);

    ResponseTask getTask(int id);

    ResponseTask postTask(RequestTask requestTask);

    ResponseTask putTask(int id, RequestTask requestTask);

    void deleteTask(int id);

    void restoreTask(int id);

    void putTaskDone(int id, boolean done);

    void putTaskPinned(int id, boolean pinned);

    void putTaskPending(int id, boolean pending);
}
