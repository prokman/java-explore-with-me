package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.model.StateAction;
import ewm.location.dto.LocationDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "name должно быть не короче 20 символов и не больше 2000")
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000, message = "name должно быть не короче 20 символов и не больше 7000")
    private String description;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @Min(value = 0, message = "participantLimit не может быть отрицательным")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "title должно быть не короче 3 символов и не больше 120")
    private String title;

    private StateAction stateAction;
}
