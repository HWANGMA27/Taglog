package toyproject.taglog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import toyproject.taglog.entity.Note;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "노트/게시물")
@Data
public class NoteDTO {

    @NotNull
    @Schema(description = "노트 id")
    private Long noteId;

    @NotNull
    @Schema(description = "노트가 속해있는 카테고리 id")
    private Long categoryId;

    @Schema(description = "노트 제목")
    private String title;

    @Schema(description = "노트 컨텐츠")
    private String contents;

    @Schema(description = "노트에 포함된 태그")
    private List<TagDTO> tags;

    @Schema(description = "생성일자")
    private LocalDateTime createdTime;

    @Schema(description = "변경일자")
    private LocalDateTime lastModifiedTime;

    @NotNull
    @Schema(description = "노트 작성 회원 id")
    private Long userId;

    public NoteDTO() {
    }

    public NoteDTO (Note note)  {
        this.noteId = note.getId();
        this.title = note.getTitle();
        this.contents = note.getContents();
        this.createdTime = note.getCreatedTime();
        this.userId = note.getUser().getId();
        this.categoryId = note.getCategory().getId();
        this.lastModifiedTime = note.getLastModifiedTime();
    }
}
