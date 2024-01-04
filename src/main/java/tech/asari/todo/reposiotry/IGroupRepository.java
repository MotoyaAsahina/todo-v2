package tech.asari.todo.reposiotry;

import tech.asari.todo.reposiotry.domain.Group;

import java.util.List;
import java.util.Optional;

public interface IGroupRepository {

    List<Group> getAll(boolean archived);

    List<Group> getAllDeleted();

    Optional<Group> get(int id);

    Group create(Group group);

    Group update(int id, Group group);

    void delete(int id);

    void restore(int id);

    void setArchived(int id, boolean archived);

    int getLastOrder();

    void moveDownAfter(int order);

    void moveDownBetween(int from, int to);

    void moveUpBetween(int from, int to);
}
