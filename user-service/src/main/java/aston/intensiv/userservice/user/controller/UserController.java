package aston.intensiv.userservice.user.controller;

import aston.intensiv.userservice.user.service.UserService;
import dto.EmailRequest;
import dto.NewUserRequest;
import dto.UpdateUserRequest;
import dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по Id",
               description = "Возвращает UserDto по Id")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);

        EntityModel<UserDto> dtoWithResource = EntityModel.of(userDto);

        dtoWithResource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        dtoWithResource.add(linkTo(methodOn(UserController.class).createUser((NewUserRequest) null)).withRel("Создание пользователя"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).updateUser(id, (UpdateUserRequest) null)).withRel("Обновление пользователя"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("Удалить"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Получить всех пользователей"));

        return ResponseEntity.ok(dtoWithResource);
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя",
            description = "Возвращает UserDto созданного пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации. Некорректные значения полей")
    @ApiResponse(responseCode = "409", description = "Email занят")
    public ResponseEntity<EntityModel<UserDto>> createUser(@Valid @RequestBody NewUserRequest request) {
        UserDto userDto = userService.createUser(request);

        EntityModel<UserDto> dtoWithResource = EntityModel.of(userDto);

        dtoWithResource.add(linkTo(methodOn(UserController.class).createUser((NewUserRequest) null)).withSelfRel());
        dtoWithResource.add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withRel("Получить пользователя по id"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).updateUser(userDto.getId(), (UpdateUserRequest) null)).withRel("Обновление пользователя"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).deleteUser(userDto.getId())).withRel("Удалить"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Получить всех пользователей"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoWithResource);
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей",
            description = "Возвращает лист UserDto существующих пользователей")
    @ApiResponse(responseCode = "200", description = "Получен список")
    public CollectionModel<EntityModel<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();

        List<EntityModel<UserDto>> listDtoWithResource = userDtoList.stream()
                .map(userDto ->  EntityModel.of(userDto)
                        .add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withRel("Получить пользователя по id"))
                        .add(linkTo(methodOn(UserController.class).updateUser(userDto.getId(), (UpdateUserRequest) null)).withRel("Обновление пользователя"))
                        .add(linkTo(methodOn(UserController.class).deleteUser(userDto.getId())).withRel("Удалить")))
                .toList();

        CollectionModel<EntityModel<UserDto>> userDtoCollection = CollectionModel.of(listDtoWithResource);

        userDtoCollection.add(linkTo(methodOn(UserController.class).createUser((NewUserRequest) null)).withRel("Создать пользователя"));

        return userDtoCollection;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить данные пользователя",
            description = "Возвращает UserDto обновленного пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь обновлен")
    @ApiResponse(responseCode = "409", description = "Email занят")
    public ResponseEntity<EntityModel<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        UserDto userDto = userService.updateUser(request, id);

        EntityModel<UserDto> dtoWithResource = EntityModel.of(userDto);

        dtoWithResource.add(linkTo(methodOn(UserController.class).updateUser(userDto.getId(), (UpdateUserRequest) null)).withSelfRel());
        dtoWithResource.add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withRel("Получить пользователя по id"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Получить всех пользователей"));
        dtoWithResource.add(linkTo(methodOn(UserController.class).deleteUser(userDto.getId())).withRel("Удалить"));

        return ResponseEntity.ok(dtoWithResource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя",
            description = "Возвращает без тела с статусом 204")
    @ApiResponse(responseCode = "204", description = "Пользователь удален")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/notification")
    @Operation(summary = "Получение сообщения от notification-service",
            description = "Получает EmailRequest с email пользователя и сообщением")
    public void getNotificationRequest(@RequestBody EmailRequest request) {
        log.info("Получен реквест, сообщение {}", request);
    }

}

