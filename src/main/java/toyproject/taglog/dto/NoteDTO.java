package toyproject.taglog.dto;

import lombok.Data;
import toyproject.taglog.entity.Note;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDTO {

    @NotNull
    private Long noteId;

    @NotNull
    private Long categoryId;

    private String title;

    private String contents;

    private List<TagDTO> tags;

    private LocalDateTime createdTime;

    private LocalDateTime lastModifiedTime;

    @NotNull
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
