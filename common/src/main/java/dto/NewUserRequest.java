package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record NewUserRequest (
    @NotEmpty(message = "Имя должно быть указано")
    String name,
    @Email(message = "Некорректно введенный email")
    String email,
    @Positive(message = "Возраст должен быть положительным")
    @NotNull(message = "Возраст должен быть указан")
    Long age
) {

}
