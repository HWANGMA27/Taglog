package toyproject.taglog.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.dto.TagDTO;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.Tag;
import toyproject.taglog.service.NoteService;
import toyproject.taglog.service.NoteTagService;
import toyproject.taglog.service.TagService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final TagService tagService;
    private final NoteTagService noteTagService;

    @GetMapping("/all/{id}")
    public Slice<NoteDTO> findNote(@PathVariable("id") Long userId, Pageable pageable) {
        return noteService.findAllNoteByUserId(userId, pageable);
    }

//    @GetMapping("/{user_id}/{category_id}")
//    public Slice<NoteDTO> findNoteByCategory(@PathVariable("user_id") Long userId,
//                                             @PathVariable("category_id") Long categoryId){
//        noteService.find
//    }

    @PostMapping
    public NoteDTO addNote(@RequestBody @Valid AddNoteRequest request){
        Note note = new Note(request.getTitle(), request.getContents());
        List<Tag> tags = request.getTags()
                .stream()
                .map(tagDTO -> new Tag(tagDTO.getName()))
                .collect(Collectors.toList());
        return noteService.addNote(note, request.getUserId(), request.getCategoryId(), tags);
    }

    @PatchMapping
    public NoteDTO updateNote(@RequestBody @Valid NoteDTO noteDTO){
        Note note = new Note(noteDTO.getTitle(), noteDTO.getContents());
        List<Tag> tags = noteDTO.getTags()
                .stream()
                .map(tagDTO -> new Tag(tagDTO.getName()))
                .collect(Collectors.toList());
        return noteService.updateNote(note, noteDTO.getNoteId(), noteDTO.getUserId(), noteDTO.getCategoryId(), tags);
    }

    @PatchMapping("/category")
    public void updateNoteCategory(@RequestBody @Valid NoteDTO noteDTO){
        noteService.updateNoteCategory(noteDTO.getNoteId(), noteDTO.getCategoryId());
    }

    @DeleteMapping
    public void deleteNote(@RequestBody @Valid DeleteNoteRequest request){
        noteService.deleteNote(request.getUserId(), request.getNoteId());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AddNoteRequest {
        @NotNull
        private Long categoryId;

        @NotNull
        private String title;

        @NotNull
        private String contents;

        private List<TagDTO> tags ;

        @NotNull
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class DeleteNoteRequest {
        @NotNull
        private Long noteId;

        @NotNull
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class UpdateNoteRequest {
        @NotNull
        private Long categoryId;

        @NotNull
        private String title;

        @NotNull
        private String contents;

        private List<TagDTO> tags ;

        @NotNull
        private Long noteId;

        @NotNull
        private Long userId;
    }
}
