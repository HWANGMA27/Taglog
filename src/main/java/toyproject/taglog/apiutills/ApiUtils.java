package toyproject.taglog.apiutills;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import toyproject.taglog.exception.ApiExceptionEntity;

@Data
public class ApiUtils {
    public static <T>ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static <T>ApiResult<T> error(ResponseEntity<ApiExceptionEntity> error){
        return new ApiResult<>(false, null, error);
    }
}