package tech.asari.todo.reposiotry;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tech.asari.todo.reposiotry.domain.Tag;
import tech.asari.todo.reposiotry.domain.TagMap;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TagRepository implements ITagRepository {

    private final JdbcClient client;

    public TagRepository(JdbcClient client) {
        this.client = client;
    }

    @Override
    public List<Tag> getAllTags(boolean archived) {
        String sql = "SELECT * FROM tags WHERE deleted_at IS NULL" +
                (archived ? " AND archived_at IS NOT NULL" : " AND archived_at IS NULL");

        return client.sql(sql)
                .query(Tag.class)
                .list();
    }

    @Override
    public Optional<Tag> getTag(int id) {
        return client.sql("SELECT * FROM tags WHERE id = :id")
                .param("id", id)
                .query(Tag.class)
                .optional();
    }

    @Override
    public List<Tag> getAllDeletedTags() {
        return client.sql("SELECT * FROM tags WHERE deleted_at IS NOT NULL")
                .query(Tag.class)
                .list();
    }

    @Override
    public Tag createTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("""
                        INSERT INTO tags (name, classification, main_color, border_color, created_at)
                        VALUES (:name, :classification, :main_color, :border_color, :created_at)
                        """)
                .param("name", tag.name())
                .param("classification", tag.classification())
                .param("main_color", tag.mainColor())
                .param("border_color", tag.borderColor())
                .param("created_at", tag.createdAt())
                .update(keyHolder, "id");

        if (keyHolder.getKey() == null)
            throw new RuntimeException("Failed to create tag");

        return Tag.of(keyHolder.getKey().intValue(), tag);
    }

    @Override
    public Tag updateTag(int id, Tag tag) {
        client.sql("""
                        UPDATE tags
                        SET name = :name, classification = :classification, main_color = :main_color, border_color = :border_color
                        WHERE id = :id
                        """)
                .param("id", id)
                .param("name", tag.name())
                .param("classification", tag.classification())
                .param("main_color", tag.mainColor())
                .param("border_color", tag.borderColor())
                .update();

        return getTag(id).orElseThrow(() -> new RuntimeException("Failed to update tag"));
    }

    @Override
    public void deleteTag(int id) {
        client.sql("UPDATE tags SET deleted_at = :deleted_at WHERE id = :id")
                .param("id", id)
                .param("deleted_at", new Timestamp(System.currentTimeMillis()))
                .update();
    }

    @Override
    public void restoreTag(int id) {
        client.sql("UPDATE tags SET deleted_at = NULL WHERE id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public void setArchivedTag(int id, boolean archived) {
        client.sql("UPDATE tags SET archived_at = :archived_at WHERE id = :id")
                .param("id", id)
                .param("archived_at", archived ? new Timestamp(System.currentTimeMillis()) : null)
                .update();
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
