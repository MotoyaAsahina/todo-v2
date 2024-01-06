package tech.asari.todo.controller;

import org.springframework.web.bind.annotation.*;
import tech.asari.todo.controller.domain.RequestTag;
import tech.asari.todo.controller.domain.ResponseTag;
import tech.asari.todo.service.ITagService;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    List<ResponseTag> getTags(
            // NOTE: if deleted is true, archived is ignored
            @RequestParam(value = "archived", required = false, defaultValue = "false") boolean archived,
            @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted
    ) {
        return tagService.getTags(archived, deleted);
    }

    @GetMapping("/{id}")
    ResponseTag getTag(@PathVariable int id) {
        return tagService.getTag(id);
    }

    @PostMapping("")
    ResponseTag postTag(@RequestBody RequestTag requestTag) {
        return tagService.postTag(requestTag);
    }

    @PutMapping("/{id}")
    ResponseTag putTag(@PathVariable int id, @RequestBody RequestTag requestTag) {
        return tagService.putTag(id, requestTag);
    }

    @DeleteMapping("/{id}")
    void deleteTag(@PathVariable int id) {
        tagService.deleteTag(id);
    }

    @PutMapping("/{id}/restore")
    void restoreTag(@PathVariable int id) {
        tagService.restoreTag(id);
    }

    @PutMapping("/{id}/archived")
    void putTagArchived(@PathVariable int id) {
        tagService.putTagArchived(id, true);
    }

    @PutMapping("/{id}/unarchived")
    void putTagUnarchived(@PathVariable int id) {
        tagService.putTagArchived(id, false);
    }
}
