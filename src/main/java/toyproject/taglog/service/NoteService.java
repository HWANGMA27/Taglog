package toyproject.taglog.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.dto.TagDTO;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.NoteTag;
import toyproject.taglog.entity.QNoteTag;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.repository.NoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static toyproject.taglog.entity.QNote.note;
import static toyproject.taglog.entity.QNoteTag.noteTag;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;
    private final EntityManager em;

    public Slice<NoteDTO> findAllNoteByUserId(Long userId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.selectFrom(note).fetch();

        List<Note> results = queryFactory
                .selectFrom(note)
                .leftJoin(note.noteTag, noteTag).fetchJoin()
                .where(note.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<NoteDTO> returnDTO = new ArrayList<>();

        for (Note result : results) {
            NoteDTO noteDTO = new NoteDTO(result);

            List<TagDTO> tagDTOList = findTagByNote(result);
            noteDTO.setTags(tagDTOList);
            returnDTO.add(noteDTO);
        }

        return new PageImpl<>(returnDTO, pageable, returnDTO.size());
    }

    private List<TagDTO> findTagByNote(Note note) {
        List<Tag> tags = note.getNoteTag()
                .stream()
                .map(noteTag1 -> noteTag1.getTags())
                .collect(Collectors.toList());
        List<TagDTO> tagDTOList = tags
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        return tagDTOList;
    }
}
