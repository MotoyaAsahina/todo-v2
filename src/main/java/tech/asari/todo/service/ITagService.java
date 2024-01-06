package tech.asari.todo.service;

import tech.asari.todo.controller.domain.RequestTag;
import tech.asari.todo.controller.domain.ResponseTag;

import java.util.List;

public interface ITagService {

    List<ResponseTag> getTags(boolean archived, boolean deleted);

    ResponseTag getTag(int id);

    ResponseTag postTag(RequestTag requestTag);

    ResponseTag putTag(int id, RequestTag requestTag);

    void deleteTag(int id);

    void restoreTag(int id);

    void putTagArchived(int id, boolean archived);
}
