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
import toyproject.taglog.entity.*;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.NoteRepository;
import toyproject.taglog.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static toyproject.taglog.entity.QNote.note;
import static toyproject.taglog.entity.QNoteTag.noteTag;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

    private final TagService tagService;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
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

        long listSize = noteRepository.countByUserId(userId);

        List<NoteDTO> returnDTO = new ArrayList<>();

        for (Note result : results) {
            NoteDTO noteDTO = new NoteDTO(result);

            List<TagDTO> tagDTOList = findTagByNote(result);
            noteDTO.setTag(tagDTOList);
            returnDTO.add(noteDTO);
        }

        return new PageImpl<>(returnDTO, pageable, listSize);
    }

    private List<TagDTO> findTagByNote(Note note) {
        List<Tag> tags = note.getNoteTag()
                .stream()
                .map(NoteTag::getTag)
                .collect(Collectors.toList());
        List<TagDTO> tagDTOList = tags
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        return tagDTOList;
    }

    @Transactional
    public void deleteNote(NoteDTO noteDTO) {

    }

    @Transactional
    public NoteDTO addNote(Note note, Long userId, Long categoryId, List<TagDTO> tagDTOS) {
        //노트 추가
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(categoryId).get();
        note.updateUser(user);
        note.updateCategory(category);
        noteRepository.save(note);

        //태그 추가
        List<Tag> tags = tagService.addTag(note, tagDTOS.stream()
                                        .map(tagDTO -> new Tag(tagDTO.getName(), user))
                                        .collect(Collectors.toList()));
        //DTO로 변환해서 반환
        NoteDTO noteDTO = new NoteDTO(note);
        noteDTO.setTag(tags.stream()
                            .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                            .collect(Collectors.toList()));
        return noteDTO;
    }

}
