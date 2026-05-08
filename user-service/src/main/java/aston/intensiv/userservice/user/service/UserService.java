package aston.intensiv.userservice.user.service;

import dto.NewUserRequest;
import dto.UpdateUserRequest;
import dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest request);

    UserDto updateUser(UpdateUserRequest request, Long id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUser(Long id);
}
