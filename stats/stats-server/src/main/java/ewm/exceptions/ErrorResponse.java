package ewm.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    HttpStatus status;
    String errDescription;
    String exMessage;
    String stackTrace;

    public ErrorResponse(HttpStatus status, String errDescription, String exMessage, String stackTrace) {
        this.status = status;
        this.errDescription = errDescription;
        this.exMessage = exMessage;
        this.stackTrace = stackTrace;
    }
}
