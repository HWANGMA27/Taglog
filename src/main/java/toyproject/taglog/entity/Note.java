package toyproject.taglog.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.taglog.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Note extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "note_id")
    private Long id;

    @Column(name = "note_title")
    private String title;

    @Column(name = "note_contents")
    private Blob contents;

    @Column(length = 2)
    private String del_yn;

    @OneToMany(mappedBy = "note")
    private List<NoteTag> noteTag = new ArrayList<>();
}
