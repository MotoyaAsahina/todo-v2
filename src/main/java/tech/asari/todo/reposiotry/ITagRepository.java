package tech.asari.todo.reposiotry;

import tech.asari.todo.reposiotry.domain.Tag;
import tech.asari.todo.reposiotry.domain.TagMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ITagRepository {

    List<Tag> getAllTags(boolean archived);

    Optional<Tag> getTag(int id);

    List<Tag> getAllDeletedTags();

    Tag createTag(Tag tag);

    Tag updateTag(int id, Tag tag);

    void deleteTag(int id);

    void restoreTag(int id);

    void setArchivedTag(int id, boolean archived);

    Map<Integer, List<Integer>> getAllTagMaps(String status, boolean deleted);

    List<Integer> getTagMaps(int taskId);

    void createTagMaps(List<TagMap> tagMaps);

    void deleteTagMaps(List<TagMap> tagMaps);
}
