package toyproject.taglog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import toyproject.taglog.entity.Role;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "사용자/회원")
public class UserDTO {

    @NotNull
    @Schema(description = "회원 id")
    private Long userId;

    @Schema(description = "회원명")
    private String name;

    @Schema(description = "회원 사진")
    private String picture;

    @Schema(description = "회원 권한")
    private Role role;

    public UserDTO() {
    }

    @Builder
    public UserDTO(Long userId, String name, String picture, Role role) {
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }
}
