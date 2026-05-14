package aston.intensiv.userservice.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Long age;
    private LocalDateTime createdAt;
}
