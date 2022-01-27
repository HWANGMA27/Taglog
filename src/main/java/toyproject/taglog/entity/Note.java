package toyproject.taglog.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.taglog.entity.base.BaseEntity;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Note extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "note_id")
    private Long id;

    @Column(name = "note_title")
    private String title;

    @Column(name = "note_contents", columnDefinition = "Text")
    private String contents;

    @Column(length = 2)
    private String delYn;

//    @OneToMany(mappedBy = "note")
//    private List<NoteTag> noteTag = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Note(String title, String contents) {
        this.title = title;
        this.contents = contents;
        this.delYn = "N";
    }

    public void updateUser(User user){
        this.user = user;
    }

    public void updateCategory(Category category){
        this.category = category;
    }

    public void updateNoteStatus(String delYn){
        this.delYn = delYn;
    }

    public void updateContents(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
}
