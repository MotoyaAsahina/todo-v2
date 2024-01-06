package tech.asari.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.asari.todo.controller.domain.RequestTag;
import tech.asari.todo.controller.domain.ResponseTag;
import tech.asari.todo.reposiotry.ITagRepository;
import tech.asari.todo.reposiotry.domain.Tag;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TagService implements ITagService {

    private final ITagRepository tagRepo;

    public TagService(ITagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public List<ResponseTag> getTags(boolean archived, boolean deleted) {
        if (deleted)
            return tagRepo.getAllDeletedTags().stream().map(ResponseTag::new).toList();
        else
            return tagRepo.getAllTags(archived).stream().map(ResponseTag::new).toList();
    }

    @Override
    public ResponseTag getTag(int id) {
        Tag tag = tagRepo.getTag(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseTag(tag);
    }

    @Override
    public ResponseTag postTag(RequestTag requestTag) {
        Tag tag = tagRepo.createTag(Tag.of(requestTag, new Timestamp(System.currentTimeMillis())));
        return new ResponseTag(tag);
    }

    @Override
    public ResponseTag putTag(int id, RequestTag requestTag) {
        Tag tag = tagRepo.updateTag(id, Tag.of(requestTag));
        return new ResponseTag(tag);
    }

    @Override
    public void deleteTag(int id) {
        tagRepo.deleteTag(id);
    }

    @Override
    public void restoreTag(int id) {
        tagRepo.restoreTag(id);
    }

    @Override
    public void putTagArchived(int id, boolean archived) {
        tagRepo.setArchivedTag(id, archived);
    }
}
