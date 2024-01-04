package tech.asari.todo.controller;

import org.springframework.web.bind.annotation.*;
import tech.asari.todo.controller.domain.RequestGroup;
import tech.asari.todo.controller.domain.ResponseGroup;
import tech.asari.todo.service.IGroupService;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final IGroupService groupService;

    public GroupController(IGroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("")
    List<ResponseGroup> getGroups(
            // NOTE: if deleted is true, archived is ignored
            @RequestParam(value = "archived", required = false, defaultValue = "false") boolean archived,
            @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted
    ) {
        return groupService.getGroups(archived, deleted);
    }

    @PostMapping("")
    ResponseGroup postGroup(@RequestBody RequestGroup requestGroup) {
        return groupService.postGroup(requestGroup);
    }

    @GetMapping("/{id}")
    ResponseGroup getGroup(@PathVariable int id) {
        return groupService.getGroup(id);
    }

    @PutMapping("/{id}")
    ResponseGroup putGroup(@PathVariable int id, @RequestBody RequestGroup requestGroup) {
        return groupService.putGroup(id, requestGroup);
    }

    @DeleteMapping("/{id}")
    void deleteGroup(@PathVariable int id) {
        groupService.deleteGroup(id);
    }

    @PutMapping("/{id}/restore")
    void restoreGroup(@PathVariable int id) {
        groupService.restoreGroup(id);
    }

    @PutMapping("/{id}/archived")
    void putGroupArchived(@PathVariable int id) {
        groupService.putGroupArchived(id, true);
    }

    @PutMapping("/{id}/unarchived")
    void putGroupUnarchived(@PathVariable int id) {
        groupService.putGroupArchived(id, false);
    }
}
