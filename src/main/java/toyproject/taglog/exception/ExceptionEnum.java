package toyproject.taglog.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    INVALIDATE_USER(HttpStatus.BAD_REQUEST, "E1000", "사용자가 존재하지 않습니다."),
    INVALIDATE_NOTE(HttpStatus.BAD_REQUEST, "E1001", "노트가 존재하지 않습니다."),
    INVALIDATE_CATEGORY(HttpStatus.BAD_REQUEST, "E1002", "카테고리가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
