package tech.asari.todo.controller;

import org.springframework.web.bind.annotation.*;
import tech.asari.todo.controller.domain.RequestPostTask;
import tech.asari.todo.controller.domain.ResponseTask;
import tech.asari.todo.service.ITaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("")
    List<ResponseTask> getTasks(
            @RequestParam(value = "status", required = false, defaultValue = "undone") String status,
            @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted
    ) {
        return taskService.getTasks(status, deleted);
    }

    @PostMapping("")
    ResponseTask postTask(@RequestBody RequestPostTask requestPostTask) {
        return taskService.postTask(requestPostTask);
    }

    @GetMapping("/{id}")
    ResponseTask getTask(@PathVariable int id) {
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    ResponseTask putTask(@PathVariable int id, @RequestBody RequestPostTask requestPostTask) {
        return taskService.putTask(id, requestPostTask);
    }

    @DeleteMapping("/{id}")
    void deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}/restore")
    void restoreTask(@PathVariable int id) {
        taskService.restoreTask(id);
    }

    @PutMapping("/{id}/done")
    void putTaskDone(@PathVariable int id) {
        taskService.putTaskDone(id, true);
    }

    @PutMapping("/{id}/undone")
    void putTaskUndone(@PathVariable int id) {
        taskService.putTaskDone(id, false);
    }

    @PutMapping("/{id}/pinned")
    void putTaskPinned(@PathVariable int id) {
        taskService.putTaskPinned(id, true);
    }

    @PutMapping("/{id}/unpinned")
    void putTaskUnpinned(@PathVariable int id) {
        taskService.putTaskPinned(id, false);
    }

    @PutMapping("/{id}/pending")
    void putTaskPending(@PathVariable int id) {
        taskService.putTaskPending(id, true);
    }

    @PutMapping("/{id}/unpending")
    void putTaskUnpending(@PathVariable int id) {
        taskService.putTaskPending(id, false);
    }
}
