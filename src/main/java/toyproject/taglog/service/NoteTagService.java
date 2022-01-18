package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.NoteTag;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.repository.NoteTagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteTagService {

    private final NoteTagRepository noteTagRepository;

    @Transactional
    public void addNoteTage(Note note, Tag tag) {
        NoteTag noteTag = new NoteTag(note, tag, note.getUser());
        noteTagRepository.save(noteTag);
    }

    @Transactional
    public void deleteNoteTag(Long noteId) {
        noteTagRepository.bulkDelete(noteId);
    }

    public List<NoteTag> findNoteTagByNoteId (Long noteId){
        return noteTagRepository.findNoteTagByNoteId(noteId);
    }

    public List<NoteTag> findNoteTagByUserId(Long userId) {
        return noteTagRepository.findNoteTagByUserId(userId);
    }

    public List<Note> findNoteTagByUserIdAndTagId(Long userId, Long tagId) {
        return noteTagRepository.findNoteTagByUserIdAndTagId(userId, tagId);
    }
}
