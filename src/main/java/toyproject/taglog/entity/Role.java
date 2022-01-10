package toyproject.taglog.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "ADMIN"),
    USER("ROLE_USER", "USER"),
    GUEST("ROLE_GUEST", "GUEST");

    private final String key;
    private final String title;
}
