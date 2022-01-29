package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.NoteTag;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.repository.NoteTagRepository;
import toyproject.taglog.repository.querydsl.TagDSLRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteTagService {

    private final NoteTagRepository noteTagRepository;
    private final TagDSLRepository noteTagDSLRepository;

    @Transactional
    public void addNoteTage(Note note, Tag tag) {
        NoteTag noteTag = NoteTag.builder()
                                .note(note)
                                .tag(tag)
                                .build();
        noteTagRepository.save(noteTag);
    }

    @Transactional
    public void deleteNoteTag(Long noteId) {
        noteTagRepository.bulkDelete(noteId);
    }

    public List<NoteTag> findNoteTagByNoteId (Long noteId){
        return noteTagRepository.findNoteTagByNoteId(noteId);
    }
}
