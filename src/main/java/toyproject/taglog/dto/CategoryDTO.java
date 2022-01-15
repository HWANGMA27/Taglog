package toyproject.taglog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDTO {

    @NotNull
    private Long categoryId;
    private String name;
    private int order;
    @NotNull
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
