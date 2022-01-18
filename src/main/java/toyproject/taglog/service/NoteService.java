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
import toyproject.taglog.exception.NoteNotFoundException;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.NoteRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static toyproject.taglog.entity.QNote.note;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

    private final TagService tagService;
    private final NoteTagService noteTagService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final EntityManager em;

    public Slice<NoteDTO> findAllNoteByUserId(Long userId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<Note> results = queryFactory
                .selectFrom(note)
                .where(note.user.id.eq(userId), note.delYn.eq("N"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long listSize = noteRepository.countByUserId(userId);

        List<NoteDTO> returnDTO = new ArrayList<>();

        for (Note result : results) {
            NoteDTO noteDTO = new NoteDTO(result);

            List<TagDTO> tagDTOList = findTagByNote(result);
            noteDTO.setTags(tagDTOList);
            returnDTO.add(noteDTO);
        }

        return new PageImpl<>(returnDTO, pageable, listSize);
    }

    public NoteDTO findNoteById(Long noteId) {
        Optional<Note> findNote = noteRepository.findByIdAndDelYn(noteId, "N");
        if (findNote.isEmpty()) {
            throw new NoteNotFoundException("노트가 존재하지 않습니다.");
        } else {
            Note note = findNote.get();
            List<TagDTO> tagByNote = findTagByNote(note);
            NoteDTO noteDTO = new NoteDTO(note);
            noteDTO.setTags(tagByNote);
            return noteDTO;
        }
    }


    private List<TagDTO> findTagByNote(Note note) {
        List<NoteTag> noteTag = noteTagService.findNoteTagByNoteId(note.getId());
        List<Tag> tags = noteTag
                .stream()
                .map(NoteTag::getTag)
                .collect(Collectors.toList());
        List<TagDTO> tagDTOList = tags
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        return tagDTOList;
    }

    public NoteDTO findNoteByIdAndDelYn(Long noteId, String delYn) {
        Optional<Note> findNote = noteRepository.findByIdAndDelYn(noteId, delYn);
        if (findNote.isEmpty()) {
            if (delYn.equals("Y")) {
                throw new NoteNotFoundException("삭제된 노트가 없습니다.");
            } else {
                throw new NoteNotFoundException("노트가 존재하지 않습니다.");
            }
        } else {
            return new NoteDTO(findNote.get());
        }
    }

    @Transactional
    public NoteDTO addNote(Note note, Long userId, Long categoryId, List<Tag> tagList) {
        //노트 추가
        User user = userService.findUserByid(userId);
        Category category = categoryService.findCategoryById(categoryId);
        note.updateUser(user);
        note.updateCategory(category);
        noteRepository.save(note);

        //태그 추가
        List<Tag> tags = tagService.addTag(note, tagList);
        //DTO로 변환해서 반환
        NoteDTO noteDTO = new NoteDTO(note);
        noteDTO.setTags(tags.stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList()));
        return noteDTO;
    }

    @Transactional
    public void deleteNote(Long userId, Long noteId) {
        Optional<Note> findNote = noteRepository.findByIdAndUserIdAndDelYn(noteId, userId, "N");
        if (findNote.isEmpty()) {
            throw new NoteNotFoundException("노트가 존재하지 않습니다.");
        } else {
            Note note = findNote.get();
            note.updateNoteStatus("Y");
            noteRepository.save(note);
            noteTagService.deleteNoteTag(noteId);
        }
    }

    @Transactional
    public NoteDTO updateNote(Note updateNote, Long noteId, Long userId, Long categoryId, List<Tag> tags) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        //원본 노트를 가져와서 request내용으로 변경 작업
        Note orgNote = noteRepository.getById(noteId);
        Category newCategory = categoryService.findCategoryById(categoryId);

        if (!orgNote.getCategory().equals(newCategory)) {
            orgNote.updateCategory(newCategory);
        }

        orgNote.updateContents(updateNote.getTitle(), updateNote.getContents());
        noteRepository.save(orgNote);

        //노트 태그 테이블에 노트id 전체 삭제
        noteTagService.deleteNoteTag(noteId);

        //bulk delete라 재조회
        Note note = queryFactory
                .selectFrom(QNote.note)
                .join(QNote.note.user).fetchJoin()
                .where(QNote.note.id.eq(noteId))
                .fetchOne();

        //신규 태그인 경우 추가
        List<Tag> tagList = tagService.addTag(note, tags);
        NoteDTO noteDTO = new NoteDTO(note);
        noteDTO.setTags(tagList.stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList()));
        return noteDTO;
    }

    @Transactional
    public void updateNoteCategory(Long noteId, Long categoryId) {
        Optional<Note> findNote = noteRepository.findByIdAndDelYn(noteId, "N");
        Category findCategory = categoryService.findCategoryById(categoryId);

        if (findNote.isEmpty()) {
            throw new NoteNotFoundException("변경할 노트가 존재하지 않습니다.");
        } else {
            Note note = findNote.get();
            note.updateCategory(findCategory);
            noteRepository.save(note);
        }
    }

    public List<NoteDTO> findNoteByTag(Long userId, Long tagId) {
        List<Note> notes = noteTagService.findNoteTagByUserIdAndTagId(userId, tagId);
        List<NoteDTO> noteDTOS = new ArrayList<>();

        for (Note note : notes) {
            NoteDTO noteDTO = new NoteDTO(note);
            List<Tag> tags = tagService.findTagByNoteId(note.getId());
            noteDTO.setTags(tags.stream()
                    .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                    .collect(Collectors.toList()));
            noteDTOS.add(noteDTO);
        }

        return noteDTOS;
    }
}