package toyproject.taglog.dto;

import lombok.Data;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.Tag;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDTO {

    @NotNull
    private Long noteId;

    @NotNull
    private Long categoryId;

    @NotNull
    private String title;

    @NotNull
    private String contents;

    private List<TagDTO> tag;

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
