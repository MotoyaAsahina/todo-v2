package tech.asari.todo.service;

import org.springframework.stereotype.Service;
import tech.asari.todo.controller.domain.RequestPostTask;
import tech.asari.todo.controller.domain.ResponseTask;
import tech.asari.todo.reposiotry.ITagRepository;
import tech.asari.todo.reposiotry.ITaskRepository;
import tech.asari.todo.reposiotry.domain.TagMap;
import tech.asari.todo.reposiotry.domain.Task;

import java.util.List;
import java.util.Map;

@Service
public class TaskService implements ITaskService {
    private final ITaskRepository taskRepo;
    private final ITagRepository tagRepo;

    public TaskService(ITaskRepository taskRepo, ITagRepository tagRepo) {
        this.taskRepo = taskRepo;
        this.tagRepo = tagRepo;
    }

    @Override
    public List<ResponseTask> getTasks(String status, boolean deleted) {
        List<Task> tasks = taskRepo.getAll(status, deleted);
        Map<Integer, List<Integer>> tagMaps = tagRepo.getAllTagMaps(status, deleted);
        return tasks.stream().map(task -> new ResponseTask(task, tagMaps.get(task.id()))).toList();
    }

    @Override
    public ResponseTask getTask(int id) {
        Task task = taskRepo.get(id);
        List<Integer> tagMaps = tagRepo.getTagMaps(id);
        return new ResponseTask(task, tagMaps);
    }

    @Override
    public ResponseTask postTask(RequestPostTask requestPostTask) {
        Task task = taskRepo.create(Task.of(requestPostTask));
        tagRepo.createTagMaps(requestPostTask.tags().stream().map(tagId -> new TagMap(task.id(), tagId)).toList());
        return new ResponseTask(task, requestPostTask.tags());
    }

    @Override
    public ResponseTask putTask(int id, RequestPostTask requestPostTask) {
        Task task = taskRepo.update(id, Task.of(requestPostTask));

        List<Integer> newTags = requestPostTask.tags();
        List<Integer> registeredTags = tagRepo.getTagMaps(id);

        tagRepo.createTagMaps(
                newTags.stream().filter(tagId -> !registeredTags.contains(tagId)).map(tagId -> new TagMap(task.id(), tagId)).toList());
        tagRepo.deleteTagMaps(
                registeredTags.stream().filter(tagId -> !newTags.contains(tagId)).map(tagId -> new TagMap(task.id(), tagId)).toList());

        return new ResponseTask(task, requestPostTask.tags());
    }

    @Override
    public void deleteTask(int id) {
        taskRepo.delete(id);
    }

    @Override
    public void restoreTask(int id) {
        taskRepo.restore(id);
    }

    @Override
    public void putTaskDone(int id, boolean done) {
        taskRepo.setDone(id, done);
    }

    @Override
    public void putTaskPinned(int id, boolean pinned) {
        taskRepo.setPinned(id, pinned);
    }

    @Override
    public void putTaskPending(int id, boolean pending) {
        taskRepo.setPending(id, pending);
    }
}
