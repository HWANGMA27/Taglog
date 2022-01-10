package toyproject.taglog.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name")
    private String name;

    @Column(length = 2)
    private String del_yn;

    @OneToMany(mappedBy = "tags")
    private List<NoteTag> noteTag = new ArrayList<>();
}
