package toyproject.taglog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "카테고리")
@Data
public class CategoryDTO {

    @NotNull
    @Schema(description = "카테고리 id")
    private Long categoryId;

    @Schema(description = "카테고리 명")
    private String name;

    @Schema(description = "카테고리 정렬 순서")
    private int order;

    @Schema(description = "카테고리 소유 회원 id")
    @NotNull
    private Long userId;

    public CategoryDTO() {
    }

    @Builder
    public CategoryDTO(Long categoryId, String name, int order, Long userId) {
        this.categoryId = categoryId;
        this.name = name;
        this.order = order;
        this.userId = userId;
    }
}
