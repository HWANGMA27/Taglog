package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.dto.TagDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.entity.User;
import toyproject.taglog.repository.NoteRepository;
import toyproject.taglog.repository.NoteTagRepository;
import toyproject.taglog.repository.condition.NoteSearchCondition;
import toyproject.taglog.repository.querydsl.NoteDSLRepository;
import toyproject.taglog.repository.querydsl.TagDSLRepository;
import toyproject.taglog.service.common.ValidateService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

    private final TagService tagService;
    private final NoteTagRepository noteTagRepository;
    private final ValidateService validateService;
    private final NoteRepository noteRepository;
    private final NoteDSLRepository noteDSLRepository;
    private final TagDSLRepository tagDSLRepository;

    public Slice<NoteDTO> findAllNoteByUserId(Long userId, Pageable pageable) {
        NoteSearchCondition condition = new NoteSearchCondition();
        condition.setUserId(userId);
        List<Note> results = noteDSLRepository.findNotesWithCondition(condition, pageable);
        long listSize = noteDSLRepository.countNotesWithCondition(condition);

        List<NoteDTO> returnDTO = setNoteTags(results);
        return new PageImpl<>(returnDTO, pageable, listSize);
    }

    private List<NoteDTO> setNoteTags(List<Note> notes) {
        List<NoteDTO> resultDTO = new ArrayList<>();
        for (Note result : notes) {
            NoteDTO noteDTO = new NoteDTO(result);

            List<TagDTO> tagDTOList = findTagByNote(result);
            noteDTO.setTags(tagDTOList);
            resultDTO.add(noteDTO);
        }
        return resultDTO;
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
        return tagDSLRepository.findTagByNoteId(note.getId())
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    public NoteDTO findNoteByIdAndDelYn(Long noteId, String delYn) {
        Note findNote = validateService.validateNote(noteId, delYn);
        return new NoteDTO(findNote);
    }

    @Transactional
    public NoteDTO addNote(Note note, Long userId, Long categoryId, List<Tag> tagList) {
        //?????? ??????
        User user = validateService.validateUser(userId);
        Category category = validateService.validateCategory(categoryId);
        note.updateUser(user);
        note.updateCategory(category);
        noteRepository.save(note);

        //?????? ??????
        List<Tag> tags = tagService.addTag(note, tagList);

        //DTO??? ???????????? ??????
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
        Note findNote = validateService.validateNote(noteId, "N");
        findNote.updateNoteStatus("Y");
        noteTagRepository.bulkDelete(noteId);
    }

    @Transactional
    public NoteDTO updateNote(Note updateNote, Long noteId, Long userId, Long categoryId, List<Tag> tags) {
        //?????? ????????? ???????????? request???????????? ?????? ??????
        Note orgNote = validateService.validateNote(noteId, "N");
        Category newCategory = validateService.validateCategory(categoryId);

        if (!orgNote.getCategory().equals(newCategory)) {
            orgNote.updateCategory(newCategory);
        }

        orgNote.updateContents(updateNote.getTitle(), updateNote.getContents());

        //?????? ?????? ???????????? ??????id ?????? ??????
        noteTagRepository.bulkDelete(noteId);

        //bulk delete??? ?????????
        Note note = noteRepository.findNoteAndUserById(noteId);

        //?????? ????????? ?????? ??????
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
        Note findNote = validateService.validateNote(noteId, "N");
        Category findCategory = validateService.validateCategory(categoryId);
        findNote.updateCategory(findCategory);
    }

    public Slice<NoteDTO> findNoteByTag(Long userId, Long tagId, Pageable pageable) {
        NoteSearchCondition condition = new NoteSearchCondition();
        condition.setUserId(userId);
        condition.setTagId(tagId);

        List<Note> notes = findNoteByUserIdAndTagId(userId, tagId, pageable);
        long listSize = noteDSLRepository.countNotesWithCondition(condition);
        List<NoteDTO> noteDTOS = setNoteTags(notes);

        return new PageImpl<>(noteDTOS, pageable, listSize);
    }

    public List<Note> findNoteByUserIdAndTagId(Long userId, Long tagId, Pageable pageable) {
        return noteDSLRepository.findNoteByUserIdAndTagId(userId, tagId, pageable);
    }

    public void bulkDeleteNoteByCategoryId(Long categoryId) {
        noteRepository.bulkDeleteNoteByCategoryId(categoryId);
    }
}