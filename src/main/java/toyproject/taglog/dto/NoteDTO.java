package toyproject.taglog.dto;

import lombok.Data;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.Tag;

import javax.persistence.Column;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDTO {
    private Long noteId;
    private String title;
    private byte[] contents;
    private List<TagDTO> tags;
    private LocalDateTime createdTime;

    public NoteDTO() {
    }

    public NoteDTO (Note note)  {
        this.noteId = note.getId();
        this.title = note.getTitle();
        this.contents = note.getContents();
        this.createdTime = note.getCreatedTime();
    }

    public void setTag(List<Tag> tag){
    }
}
