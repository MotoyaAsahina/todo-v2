package tech.asari.todo.reposiotry;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import tech.asari.todo.reposiotry.domain.TagMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TagRepository implements ITagRepository {

    private final JdbcClient client;

    public TagRepository(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Map<Integer, List<Integer>> getAllTagMaps(String status, boolean deleted) {
        String sql = "SELECT tag_maps.task_id, tag_maps.tag_id FROM tag_maps, tasks WHERE tag_maps.task_id = tasks.id"
                + (deleted ? " AND tasks.deleted_at IS NOT NULL" : " AND tasks.deleted_at IS NULL");

        switch (status) {
            case "done" -> sql += " AND tasks.done_at IS NOT NULL";
            case "undone" -> sql += " AND tasks.done_at IS NULL";
            case "all" -> sql += "";
            default -> throw new IllegalArgumentException("status must be one of 'done', 'undone', 'all'");
        }

        return client.sql(sql)
                .query(TagMap.class)
                .list()
                .stream()
                .collect(Collectors.groupingBy(TagMap::taskId, Collectors.mapping(TagMap::tagId, Collectors.toList())));
    }

    @Override
    public List<Integer> getTagMaps(int taskId) {
        return client.sql("SELECT tag_id FROM tag_maps WHERE task_id = :task_id")
                .param("task_id", taskId)
                .query(Integer.class)
                .list();
    }

    @Override
    public void createTagMaps(List<TagMap> tagMaps) {
        tagMaps.forEach(tagMap ->
                client.sql("INSERT INTO tag_maps (task_id, tag_id) VALUES (:task_id, :tag_id)")
                        .param("task_id", tagMap.taskId())
                        .param("tag_id", tagMap.tagId())
                        .update());
    }

    @Override
    public void deleteTagMaps(List<TagMap> tagMaps) {
        tagMaps.forEach(tagMap ->
                client.sql("DELETE FROM tag_maps WHERE task_id = :task_id AND tag_id = :tag_id")
                        .param("task_id", tagMap.taskId())
                        .param("tag_id", tagMap.tagId())
                        .update());
    }
}
