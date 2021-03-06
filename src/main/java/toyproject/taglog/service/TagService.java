package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.repository.TagRepository;
import toyproject.taglog.repository.querydsl.TagDSLRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TagDSLRepository tagDSLRepository;
    private final NoteTagService noteTagService;

    @Transactional
    public List<Tag> addTag(Note note, List<Tag> tags) {
        List<Tag> tagList = new ArrayList<>();

        for (Tag tag : tags) {
            Optional<Tag> tagByName = tagRepository.findTagByName(tag.getName());
            if (tagByName.isEmpty()) {
                tagRepository.save(tag);
                tagList.add(tag);
                //중간 테이블에 매핑 데이터 추가
                noteTagService.addNoteTage(note, tag);
            } else {
                Tag findTag = tagByName.get();
                tagList.add(findTag);
                noteTagService.addNoteTage(note, findTag);
            }
        }
        return tagList;
    }

    public List<Tag> findTagByNoteId(Long noteId) {
        return tagRepository.findTagByNoteId(noteId);
    }


    public List<Tag> findTagByUserId(Long userId) {
        return tagDSLRepository.findTagByUserId(userId);
    }
}
