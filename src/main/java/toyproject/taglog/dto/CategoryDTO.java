package toyproject.taglog.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private Long categoryId;
    private String name;
    private int order;
    private Long userId;

    public CategoryDTO() {
    }

    public CategoryDTO(Long categoryId, String name, int order, Long userId) {
        this.categoryId = categoryId;
        this.name = name;
        this.order = order;
        this.userId = userId;
    }
}
