package ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "title должно быть не короче 1 символов и не больше 50")
    private String title;

    private Set<Long> events;
}
