package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewUserRequest {
    @NotEmpty(message = "Имя должно быть указано")
    private String name;
    @Email(message = "Некорректно введенный email")
    private String email;
    @Positive(message = "Возраст должен быть положительным")
    @NotNull(message = "Возраст должен быть указан")
    private Long age;
}
