package toyproject.taglog.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
@ApiOperation(value = "Note API")
public class NoteController {

    private final NoteService noteService;

    @Operation(summary = "전체 노트 조회", description = "회원 id로 전체 노트를 조회합니다.")
    @GetMapping("/all/{id}")
    public Slice<NoteDTO> findNote(@Parameter(description = "회원 Id", in = ParameterIn.PATH) @PathVariable("id") Long userId, Pageable pageable) {
        return noteService.findAllNoteByUserId(userId, pageable);
    }

    @Operation(summary = "단일 노트 조회", description = "노트 id로 단일 노트를 조회합니다.")
    @GetMapping("/{note_id}")
    public NoteDTO findNoteById(@Parameter(description = "노트 Id", in = ParameterIn.PATH) @PathVariable("note_id") Long noteId){
        return noteService.findNoteByIdAndDelYn(noteId, "N");
    }

    @Operation(summary = "새노트 추가", description = "새로운 노트를 추가합니다.")
    @PostMapping
    public NoteDTO addNote(@RequestBody @Valid AddNoteRequest request){
        Note note = new Note(request.getTitle(), request.getContents());
        List<Tag> tags = request.getTags()
                .stream()
                .map(tagDTO -> new Tag(tagDTO.getName()))
                .collect(Collectors.toList());
        return noteService.addNote(note, request.getUserId(), request.getCategoryId(), tags);
    }

    @Operation(summary = "노트 업데이트", description = "노트 제목, 컨텐츠, 태그를 업데이트합니다.")
    @PatchMapping
    public NoteDTO updateNote(@RequestBody @Valid NoteDTO noteDTO){
        Note note = new Note(noteDTO.getTitle(), noteDTO.getContents());
        List<Tag> tags = noteDTO.getTags()
                .stream()
                .map(tagDTO -> new Tag(tagDTO.getName()))
                .collect(Collectors.toList());
        return noteService.updateNote(note, noteDTO.getNoteId(), noteDTO.getUserId(), noteDTO.getCategoryId(), tags);
    }

    @Operation(summary = "노트 카테고리 변경", description = "노트가 속한 카테고리를 변경합니다.")
    @PatchMapping("/category")
    public void updateNoteCategory(@RequestBody @Valid NoteDTO noteDTO){
        noteService.updateNoteCategory(noteDTO.getNoteId(), noteDTO.getCategoryId());
    }

    @Operation(summary = "노트를 삭제합니다.", description = "노트 id로 노트를 삭제합니다.")
    @DeleteMapping
    public void deleteNote(@RequestBody @Valid DeleteNoteRequest request){
        noteService.deleteNote(request.getUserId(), request.getNoteId());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "노트 추가 양식")
    static class AddNoteRequest {
        @NotNull
        @Schema(description = "카테고리 Id")
        private Long categoryId;

        @NotNull
        @Schema(description = "노트 제목")
        private String title;

        @NotNull
        @Schema(description = "노트 내용")
        private String contents;

        @Schema(description = "노트에 포함될 태그")
        private List<TagDTO> tags ;

        @NotNull
        @Schema(description = "노트 작성자 Id")
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "노트 삭제 양식")
    static class DeleteNoteRequest {
        @NotNull
        @Schema(description = "노트 Id")
        private Long noteId;

        @NotNull
        @Schema(description = "노트 작성자 Id")
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "노트 업데이트 양식")
    static class UpdateNoteRequest {
        @NotNull
        @Schema(description = "카테고리 Id")
        private Long categoryId;

        @NotNull
        @Schema(description = "노트 제목")
        private String title;

        @NotNull
        @Schema(description = "노트 내용")
        private String contents;

        @Schema(description = "노트에 포함될 태그")
        private List<TagDTO> tags ;

        @NotNull
        @Schema(description = "노트 Id")
        private Long noteId;

        @NotNull
        @Schema(description = "노트 작성자 Id")
        private Long userId;
    }
}
