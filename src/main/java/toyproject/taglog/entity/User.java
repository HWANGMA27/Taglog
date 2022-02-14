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
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_picture")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role role;

    @Builder
    public User(String email, String name, String picture, Role role) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }

    public User updateUser(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return role.getKey();
    }
}

