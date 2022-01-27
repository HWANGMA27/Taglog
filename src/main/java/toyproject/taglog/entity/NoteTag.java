package toyproject.taglog.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoteTag {

    @Id @GeneratedValue
    @Column(name = "note_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public NoteTag(Note note, Tag tag, User user) {
        this.note = note;
        this.tag = tag;
        this.user = user;
    }
}
