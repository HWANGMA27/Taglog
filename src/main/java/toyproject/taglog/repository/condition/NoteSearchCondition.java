package toyproject.taglog.repository.condition;

import lombok.Data;

@Data
public class NoteSearchCondition {
    private Long userId;
    private Long noteId;
    private Long categoryId;
    private Long tagId;
    private String delYN;
    private String keyword;

    //default 조회 조건은 삭제되지 않은 노트
    public NoteSearchCondition() {
        this.delYN = "N";
    }
}
