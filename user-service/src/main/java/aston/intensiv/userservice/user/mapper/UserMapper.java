package aston.intensiv.userservice.user.mapper;

import aston.intensiv.userservice.user.User;
import dto.NewUserRequest;
import dto.UpdateUserRequest;
import dto.UserDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.haseName()) {
            user.setName(request.getName());
        }

        if (request.haseEmail()) {
            user.setEmail(request.getEmail());
        }

        if (request.hasAge()) {
            user.setAge(request.getAge());
        }

        return user;
    }
}
