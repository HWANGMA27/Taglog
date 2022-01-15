package toyproject.taglog.dto;

import lombok.Data;

@Data
public class TagDTO {
    private Long tagId;
    private String name;

    public TagDTO() {
    }

    public TagDTO(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

}
