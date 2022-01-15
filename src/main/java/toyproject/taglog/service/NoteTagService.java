package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.NoteTag;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.repository.NoteTagRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteTagService {

    private final NoteTagRepository noteTagRepository;

    @Transactional
    public void addNoteTage(Note note, Tag tag) {
        NoteTag noteTag = new NoteTag(note, tag);
        noteTagRepository.save(noteTag);
    }
}
