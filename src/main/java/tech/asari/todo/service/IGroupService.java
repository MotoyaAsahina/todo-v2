package tech.asari.todo.service;

import tech.asari.todo.controller.domain.RequestGroup;
import tech.asari.todo.controller.domain.ResponseGroup;

import java.util.List;

public interface IGroupService {

    List<ResponseGroup> getGroups(boolean archived, boolean deleted);

    ResponseGroup getGroup(int id);

    ResponseGroup postGroup(RequestGroup requestGroup);

    ResponseGroup putGroup(int id, RequestGroup requestGroup);

    void deleteGroup(int id);

    void restoreGroup(int id);

    void putGroupArchived(int id, boolean archived);
}
