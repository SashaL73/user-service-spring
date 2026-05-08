package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String name;
    @Email(message = "Некоректно введенный email")
    private String email;
    @Positive(message = "Возраст должен быть положительным")
    private Long age;


    public boolean haseName() {
        return !(name == null || name.isBlank());
    }

    public boolean haseEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasAge() {
        return !(age == null);
    }
}
