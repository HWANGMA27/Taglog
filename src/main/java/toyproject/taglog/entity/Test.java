package toyproject.taglog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Test {

    @Id @GeneratedValue
    private Long id;
    private String content;
}
