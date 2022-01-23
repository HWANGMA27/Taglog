package toyproject.taglog.repository.condition;

import lombok.Data;

@Data
public class CategorySearchCondition {
    private Long categoryId;
    private Long userId;
    private int order;

    public CategorySearchCondition() {
    }
}
