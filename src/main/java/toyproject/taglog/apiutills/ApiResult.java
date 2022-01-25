package toyproject.taglog.apiutills;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import toyproject.taglog.exception.ApiExceptionEntity;

@Data
public class ApiResult<T>{
    private final boolean success;
    private final T response;
    private final ResponseEntity<ApiExceptionEntity> error;

    public ApiResult(boolean success, T response, ResponseEntity<ApiExceptionEntity> error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }
}