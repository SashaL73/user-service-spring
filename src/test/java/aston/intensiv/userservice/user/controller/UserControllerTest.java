package aston.intensiv.userservice.user.controller;

import aston.intensiv.userservice.exception.ConflictException;
import aston.intensiv.userservice.user.dto.NewUserRequest;
import aston.intensiv.userservice.user.dto.UpdateUserRequest;
import aston.intensiv.userservice.user.dto.UserDto;
import aston.intensiv.userservice.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper mapper;

    private UserDto responseUserDto = UserDto.builder()
            .id(1L)
            .name("Test")
            .email("test@test.com")
            .age(20L)
            .createdAt(LocalDateTime.now())
            .build();

    private NewUserRequest request = NewUserRequest.builder()
            .name("Test")
            .email("test@test.com")
            .age(20L)
            .build();

    @Test
    void createUserShouldReturnUserDto() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(NewUserRequest.class)))
                .thenReturn(responseUserDto);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()))
                .andExpect(jsonPath("$.age").value(responseUserDto.getAge()));
    }

    @Test
    void getUserByIdShouldReturnUser() throws Exception {
        Mockito.when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(responseUserDto);

        mockMvc.perform(get(PATH + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()))
                .andExpect(jsonPath("$.age").value(responseUserDto.getAge()));
    }

    @Test
    void getAllUsersShouldReturnListUsersDto() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(responseUserDto));

        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$[0].name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$[0].email").value(responseUserDto.getEmail()))
                .andExpect(jsonPath("$[0].age").value(responseUserDto.getAge()));
    }

    @Test
    void updateUserShouldReturnUserDto() throws Exception {
        Mockito.when(userService.updateUser(Mockito.any(UpdateUserRequest.class), Mockito.anyLong()))
                .thenReturn(responseUserDto);

        mockMvc.perform(patch(PATH + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()))
                .andExpect(jsonPath("$.age").value(responseUserDto.getAge()));
    }

    @Test
    void deleteUserShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyLong());

        mockMvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void createUserShouldReturnConflict() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(NewUserRequest.class)))
                .thenThrow(new ConflictException("Пользователь с email " + request.getEmail() + " уже существует"));

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void createUserShouldReturnBadRequest() throws Exception {
        NewUserRequest newUserRequest = NewUserRequest.builder()
                .name("Test")
                .email("test.com")
                .age(20L)
                .build();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserShouldReturnBadRequest() throws Exception {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .name("Test")
                .email("test.com")
                .age(20L)
                .build();

        mockMvc.perform(patch(PATH + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isBadRequest());
    }

}
