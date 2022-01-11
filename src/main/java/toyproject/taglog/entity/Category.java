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
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category")
    private List<Note> note = new ArrayList<>();

    public Category(String name, int order, User user) {
        this.name = name;
        this.order = order;
        this.user = user;
    }

    public Category(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void updateCategory(String name, int order){
        this.name = name;
        this.order = order;
    }

    public void reOrderCategory(int order){
        this.order = order;
    }
}
