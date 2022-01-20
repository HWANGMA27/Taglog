package toyproject.taglog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "태그")
public class TagDTO {

    @NotNull
    @Schema(description = "태그 id")
    private Long id;

    @Schema(description = "태그명")
    private String name;

    public TagDTO() {
    }

    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
