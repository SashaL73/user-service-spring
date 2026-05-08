package aston.intensiv.userservice.user.service;

import aston.intensiv.userservice.user.dto.NewUserRequest;
import aston.intensiv.userservice.user.dto.UpdateUserRequest;
import aston.intensiv.userservice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest request);

    UserDto updateUser(UpdateUserRequest request, Long id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUser(Long id);
}
