package ewm.exceptions;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter

public class ErrorResponse {
    String status;
    String reason;
    String message;
    Timestamp timestamp;

    public ErrorResponse(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = Timestamp.from(Instant.now());
    }

}
