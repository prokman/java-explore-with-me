package statdto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatDtoRequest {

    @NotBlank(message = "app не может быть пустым")
    private String app;

    @NotBlank(message = "uri не может быть пустым")
    private String uri;

    @NotBlank(message = "ip не может быть пустым")
    private String ip;

    @NotBlank(message = "timestamp не может быть пустым")
    private String timestamp;
}
