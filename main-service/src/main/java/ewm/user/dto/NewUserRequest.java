package ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Name не может быть пустым")
    @Size(min = 2, max = 250)
    private String name;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @Size(min = 6, max = 254)
    private String email;
}
