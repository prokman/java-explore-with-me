package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "name должно быть не короче 20 символов и не больше 2000")
    private String annotation;

    @NotNull
    @Positive
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "name должно быть не короче 20 символов и не больше 7000")
    private String description;

    @NotNull(message = "description не может быть NULL")
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @Min(value = 0, message = "participantLimit не может быть отрицательным")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull(message = "title не может быть NULL")
    @Size(min = 3, max = 120, message = "title должно быть не короче 3 символов и не больше 120")
    private String title;
}
