package tech.asari.todo.reposiotry;

import tech.asari.todo.reposiotry.domain.TagMap;

import java.util.List;
import java.util.Map;

public interface ITagRepository {
    Map<Integer, List<Integer>> getAllTagMaps(String status, boolean deleted);

    List<Integer> getTagMaps(int taskId);

    void createTagMaps(List<TagMap> tagMaps);

    void deleteTagMaps(List<TagMap> tagMaps);
}
