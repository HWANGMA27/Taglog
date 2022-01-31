package toyproject.taglog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toyproject.taglog.apiutills.ApiResult;
import toyproject.taglog.apiutills.ApiUtils;
import toyproject.taglog.exception.invalid.InvalidateCategoryException;
import toyproject.taglog.exception.invalid.InvalidateNoteException;
import toyproject.taglog.exception.invalid.InvalidateUserException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionAdvice {
    @ExceptionHandler({ApiException.class})
    public ApiResult<ResponseEntity<ApiExceptionEntity>> exceptionHandler(HttpServletRequest request, final ApiException e) {
        //e.printStackTrace();
        log.info(e.getMessage());
        return ApiUtils.error(ResponseEntity
                .status(e.getError().getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(e.getError().getCode())
                        .errorMessage(e.getError().getMessage())
                        .build()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidateNoteException.class)
    public ApiResult<ResponseEntity<ApiExceptionEntity>> noteExceptionHandler(HttpServletRequest request, final RuntimeException e) {
        log.info(e.getMessage());
        return ApiUtils.error(ResponseEntity
                .status(ExceptionEnum.INVALIDATE_NOTE.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.INVALIDATE_NOTE.getCode())
                        .errorMessage(ExceptionEnum.INVALIDATE_NOTE.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidateCategoryException.class)
    public ApiResult<ResponseEntity<ApiExceptionEntity>> categoryExceptionHandler(HttpServletRequest request, final RuntimeException e) {
        log.info(e.getMessage());
        return ApiUtils.error(ResponseEntity
                .status(ExceptionEnum.INVALIDATE_CATEGORY.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.INVALIDATE_CATEGORY.getCode())
                        .errorMessage(ExceptionEnum.INVALIDATE_CATEGORY.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidateUserException.class)
    public ApiResult<ResponseEntity<ApiExceptionEntity>> userExceptionHandler(HttpServletRequest request, final RuntimeException e) {
        log.info(e.getMessage());
        return ApiUtils.error(ResponseEntity
                .status(ExceptionEnum.INVALIDATE_USER.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.INVALIDATE_USER.getCode())
                        .errorMessage(ExceptionEnum.INVALIDATE_USER.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()));
    }
}
