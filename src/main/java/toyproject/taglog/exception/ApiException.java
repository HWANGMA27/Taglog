package toyproject.taglog.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiException extends RuntimeException{

    private ExceptionEnum error;

    public ApiException(ExceptionEnum e){
        super(e.getMessage());
        this.error = e;
    }
}
