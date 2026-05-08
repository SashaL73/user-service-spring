package aston.intensiv.userservice.user.service;

import aston.intensiv.userservice.exception.ConflictException;
import aston.intensiv.userservice.exception.NotFoundException;
import aston.intensiv.userservice.user.User;
import aston.intensiv.userservice.user.repository.UserRepository;
import aston.intensiv.userservice.user.service.UserService;
import dto.NewUserRequest;
import dto.UpdateUserRequest;
import dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private NewUserRequest newUserRequest = NewUserRequest.builder()
            .name("Test")
            .email("test@test.com")
            .age(20L)
            .build();

    @Test
    void createUserShouldReturnSavedUser() {
        UserDto userDto = userService.createUser(newUserRequest);

        Assertions.assertNotNull(userDto.getId());
        Assertions.assertEquals("Test", userDto.getName());
        Assertions.assertEquals("test@test.com", userDto.getEmail());
        Assertions.assertEquals(20L, userDto.getAge());
        Assertions.assertNotNull(userDto.getCreatedAt());
    }

    @Test
    void deleteUserShouldRemoveUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20L);
        user = userRepository.save(user);

        userService.deleteUser(user.getId());

        Assertions.assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void deleteUserShouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void updateUserShouldUpdateOnlyName() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user = userRepository.save(user);

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .name("Update")
                .email("updated@test.com")
                .age(40L)
                .build();


        UserDto userDto = userService.updateUser(updateUserRequest, user.getId());

        Assertions.assertEquals("Update", userDto.getName());
        Assertions.assertEquals("updated@test.com", userDto.getEmail());
        Assertions.assertEquals(40L, userDto.getAge());
    }

    @Test
    void updateUserWithExistEmailShouldReturnException() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user = userRepository.save(user);
        Long userId = user.getId();

        User user1 = new User();
        user1.setName("Test1");
        user1.setEmail("existEmail@test.com");
        user1.setAge(20L);
        userRepository.save(user1);

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .name("Update")
                .email("existEmail@test.com")
                .age(40L)
                .build();

        Assertions.assertThrows(ConflictException.class, () -> userService.updateUser(updateUserRequest, userId));
    }

}

