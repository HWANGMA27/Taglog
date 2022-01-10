package toyproject.taglog.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.taglog.entity.base.BaseEntity;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private Role role;
}

