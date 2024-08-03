package tech.asari.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tech.asari.todo.controller.domain.RequestGroup;
import tech.asari.todo.controller.domain.ResponseGroup;
import tech.asari.todo.reposiotry.IGroupRepository;
import tech.asari.todo.reposiotry.domain.Group;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GroupService implements IGroupService {

    private final IGroupRepository groupRepo;

    public GroupService(IGroupRepository groupRepo) {
        this.groupRepo = groupRepo;
    }

    @Override
    public List<ResponseGroup> getGroups(boolean archived, boolean deleted) {
        if (deleted)
            return groupRepo.getAllDeleted().stream().map(ResponseGroup::new).toList();
        else
            return groupRepo.getAll(archived).stream().map(ResponseGroup::new).toList();
    }

    @Override
    public ResponseGroup getGroup(int id) {
        Group group = groupRepo.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseGroup(group);
    }

    @Override
    @Transactional
    public ResponseGroup postGroup(RequestGroup requestGroup) {
        int newOrder;
        int maxOrder = groupRepo.getLastOrder(false);
        if (requestGroup.order() == null || requestGroup.order() < 0 || requestGroup.order() > maxOrder) {
            newOrder = maxOrder + 1;
        } else {
            newOrder = requestGroup.order();
            groupRepo.moveDownAfter(newOrder);
        }
        Group group = groupRepo.create(Group.of(requestGroup, newOrder, new Timestamp(System.currentTimeMillis())));
        return new ResponseGroup(group);
    }

    @Override
    @Transactional
    public ResponseGroup putGroup(int id, RequestGroup requestGroup) {
        int nowOrder = groupRepo.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).order();

        if (requestGroup.order() < nowOrder) {
            groupRepo.moveDownBetween(requestGroup.order(), nowOrder - 1);
        } else if (requestGroup.order() > nowOrder) {
            groupRepo.moveUpBetween(nowOrder + 1, requestGroup.order());
        }

        Group group = groupRepo.update(id, Group.of(requestGroup));

        return new ResponseGroup(group);
    }

    @Override
    public void deleteGroup(int id) {
        int order = groupRepo.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).order();
        int lastActiveOrder = groupRepo.getLastOrder(true);

        groupRepo.moveUpBetween(order + 1, lastActiveOrder);
        groupRepo.setOrder(id, lastActiveOrder);

        groupRepo.delete(id);
    }

    @Override
    public void restoreGroup(int id) {
        int order = groupRepo.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).order();
        int lastActiveOrder = groupRepo.getLastOrder(true);

        groupRepo.moveDownBetween(lastActiveOrder + 1, order - 1);
        groupRepo.setOrder(id, lastActiveOrder + 1);

        groupRepo.restore(id);
    }

    @Override
    public void putGroupArchived(int id, boolean archived) {
        int lastActiveOrder = groupRepo.getLastOrder(true);
        int order = groupRepo.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).order();
        if (archived) {
            groupRepo.moveUpBetween(order + 1, lastActiveOrder);
            groupRepo.setOrder(id, lastActiveOrder);
        } else {
            groupRepo.moveDownBetween(lastActiveOrder + 1, order - 1);
            groupRepo.setOrder(id, lastActiveOrder + 1);
        }
        groupRepo.setArchived(id, archived);
    }
}
