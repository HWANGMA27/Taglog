package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.dto.TagDTO;
import toyproject.taglog.entity.*;
import toyproject.taglog.exception.invalid.InvalidateNoteException;
import toyproject.taglog.repository.NoteRepository;
import toyproject.taglog.repository.condition.NoteSearchCondition;
import toyproject.taglog.repository.querydsl.NoteDSLRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

    private final TagService tagService;
    private final NoteTagService noteTagService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final NoteRepository noteRepository;
    private final NoteDSLRepository noteDSLRepository;

    public Slice<NoteDTO> findAllNoteByUserId(Long userId, Pageable pageable) {
        NoteSearchCondition condition = new NoteSearchCondition();
        condition.setUserId(userId);
        List<Note> results = noteDSLRepository.findNotesWithCondition(condition, pageable);
        long listSize = noteDSLRepository.countNotesWithCondition(condition);

        List<NoteDTO> returnDTO = new ArrayList<>();

        for (Note result : results) {
            NoteDTO noteDTO = new NoteDTO(result);

            List<TagDTO> tagDTOList = findTagByNote(result);
            noteDTO.setTags(tagDTOList);
            returnDTO.add(noteDTO);
        }
        return new PageImpl<>(returnDTO, pageable, listSize);
    }

    public Slice<NoteDTO> findNoteByCategory(Long userId, Long categoryId, Pageable pageable) {
        NoteSearchCondition condition = new NoteSearchCondition();
        condition.setUserId(userId);
        condition.setCategoryId(categoryId);

        List<Note> results = noteDSLRepository.findNotesWithCondition(condition, pageable);
        long listSize = noteDSLRepository.countNotesWithCondition(condition);

        List<NoteDTO> returnDTO = new ArrayList<>();

        for (Note result : results) {
            NoteDTO noteDTO = new NoteDTO(result);

            List<TagDTO> tagDTOList = findTagByNote(result);
            noteDTO.setTags(tagDTOList);
            returnDTO.add(noteDTO);
        }

        return new PageImpl<>(returnDTO, pageable, listSize);
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
        Note findNote = noteRepository.findByIdAndDelYn(noteId, delYn).orElseThrow(InvalidateNoteException::new);
        return new NoteDTO(findNote);
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
                .map(tag -> TagDTO.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build())
                .collect(Collectors.toList()));
        return noteDTO;
    }

    @Transactional
    public void deleteNote(Long userId, Long noteId) {
        Note findNote = noteRepository.findByIdAndUserIdAndDelYn(noteId, userId, "N").orElseThrow(InvalidateNoteException::new);
        findNote.updateNoteStatus("Y");
        noteRepository.save(findNote);
        noteTagService.deleteNoteTag(noteId);
    }

    @Transactional
    public NoteDTO updateNote(Note updateNote, Long noteId, Long userId, Long categoryId, List<Tag> tags) {
        //원본 노트를 가져와서 request내용으로 변경 작업
        Note orgNote = noteRepository.findNoteById(noteId).orElseThrow(InvalidateNoteException::new);
        Category newCategory = categoryService.findCategoryById(categoryId);

        if (!orgNote.getCategory().equals(newCategory)) {
            orgNote.updateCategory(newCategory);
        }

        orgNote.updateContents(updateNote.getTitle(), updateNote.getContents());
        noteRepository.save(orgNote);

        //노트 태그 테이블에 노트id 전체 삭제
        noteTagService.deleteNoteTag(noteId);

        //bulk delete라 재조회
        Note note = noteRepository.findNoteAndUserById(noteId);

        //신규 태그인 경우 추가
        List<Tag> tagList = tagService.addTag(note, tags);
        NoteDTO noteDTO = new NoteDTO(note);
        noteDTO.setTags(tagList.stream()
                .map(tag -> TagDTO.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build())
                .collect(Collectors.toList()));
        return noteDTO;
    }

    @Transactional
    public void updateNoteCategory(Long noteId, Long categoryId) {
        Note findNote = noteRepository.findByIdAndDelYn(noteId, "N").orElseThrow(InvalidateNoteException::new);
        Category findCategory = categoryService.findCategoryById(categoryId);
        findNote.updateCategory(findCategory);
        noteRepository.save(findNote);
     }

    public List<NoteDTO> findNoteByTag(Long userId, Long tagId) {
        List<Note> notes = noteTagService.findNoteTagByUserIdAndTagId(userId, tagId);
        List<NoteDTO> noteDTOS = new ArrayList<>();

        for (Note note : notes) {
            NoteDTO noteDTO = new NoteDTO(note);
            List<Tag> tags = tagService.findTagByNoteId(note.getId());
            noteDTO.setTags(tags.stream()
                    .map(tag -> TagDTO.builder()
                                    .id(tag.getId())
                                    .name(tag.getName())
                                    .build())
                    .collect(Collectors.toList()));
            noteDTOS.add(noteDTO);
        }

        return noteDTOS;
    }

    public void bulkDeleteNoteByCategoryId(Long categoryId) {
        noteRepository.bulkDeleteNoteByCategoryId(categoryId);
    }
}