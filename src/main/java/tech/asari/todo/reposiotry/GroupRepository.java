package tech.asari.todo.reposiotry;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tech.asari.todo.reposiotry.domain.Group;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupRepository implements IGroupRepository {

    private final JdbcClient client;

    public GroupRepository(JdbcClient client) {
        this.client = client;
    }

    @Override
    public List<Group> getAll(boolean archived) {
        String sql = "SELECT * FROM `groups` WHERE deleted_at IS NULL" +
                (archived ? " AND archived_at IS NOT NULL" : " AND archived_at IS NULL");

        return client.sql(sql)
                .query(Group.class)
                .list();
    }

    @Override
    public List<Group> getAllDeleted() {
        return client.sql("SELECT * FROM `groups` WHERE deleted_at IS NOT NULL")
                .query(Group.class)
                .list();
    }

    @Override
    public Optional<Group> get(int id) {
        return client.sql("SELECT * FROM `groups` WHERE id = :id")
                .param("id", id)
                .query(Group.class)
                .optional();
    }

    @Override
    public Group create(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("""
                        INSERT INTO `groups` (name, description, has_due_date, classified_by, `order`, created_at)
                        VALUES (:name, :description, :hasDueDate, :classifiedBy, :order, :createdAt)
                        """)
                .param("name", group.name())
                .param("description", group.description())
                .param("hasDueDate", group.hasDueDate().toString())
                .param("classifiedBy", group.classifiedBy())
                .param("order", group.order())
                .param("createdAt", new Timestamp(System.currentTimeMillis()))
                .update(keyHolder, "id");

        if (keyHolder.getKey() == null)
            throw new RuntimeException("Failed to create task");

        return Group.of(keyHolder.getKey().intValue(), group);
    }

    @Override
    public Group update(int id, Group group) {
        client.sql("""
                        UPDATE `groups`
                        SET name = :name, description = :description, has_due_date = :hasDueDate,
                            classified_by = :classifiedBy, `order` = :order
                        WHERE id = :id
                        """)
                .param("id", id)
                .param("name", group.name())
                .param("description", group.description())
                .param("hasDueDate", group.hasDueDate().toString())
                .param("classifiedBy", group.classifiedBy())
                .param("order", group.order())
                .update();

        return get(id).orElseThrow(() -> new RuntimeException("Failed to update task"));
    }

    @Override
    public void delete(int id) {
        client.sql("UPDATE `groups` SET deleted_at = :deleted_at WHERE id = :id")
                .param("id", id)
                .param("deleted_at", new Timestamp(System.currentTimeMillis()))
                .update();
    }

    @Override
    public void restore(int id) {
        client.sql("UPDATE `groups` SET deleted_at = NULL WHERE id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public void setArchived(int id, boolean archived) {
        client.sql("UPDATE `groups` SET archived_at = :archived_at WHERE id = :id")
                .param("id", id)
                .param("archived_at", archived ? new Timestamp(System.currentTimeMillis()) : null)
                .update();
    }

    @Override
    public int getLastOrder() {
        return client.sql("SELECT MAX(`order`) FROM `groups`")
                .query(Integer.class)
                .optional()
                .orElse(-1);
    }

    @Override
    public void moveDownAfter(int order) {
        client.sql("UPDATE `groups` SET `order` = `order` + 1 WHERE `order` >= :order")
                .param("order", order)
                .update();
    }

    @Override
    public void moveDownBetween(int from, int to) {
        client.sql("UPDATE `groups` SET `order` = `order` + 1 WHERE `order` BETWEEN :from AND :to")
                .param("from", from)
                .param("to", to)
                .update();
    }

    @Override
    public void moveUpBetween(int from, int to) {
        client.sql("UPDATE `groups` SET `order` = `order` - 1 WHERE `order` BETWEEN :from AND :to")
                .param("from", from)
                .param("to", to)
                .update();
    }
}
