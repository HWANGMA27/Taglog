package toyproject.taglog.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TagDTO {
    @NotNull
    private Long id;
    private String name;

    public TagDTO() {
    }

    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
