package aston.intensiv.userservice.user.service;

import aston.intensiv.userservice.exception.ConflictException;
import aston.intensiv.userservice.exception.NotFoundException;
import aston.intensiv.userservice.user.User;
import aston.intensiv.userservice.user.dto.NewUserRequest;
import aston.intensiv.userservice.user.dto.UpdateUserRequest;
import aston.intensiv.userservice.user.dto.UserDto;
import aston.intensiv.userservice.user.mapper.UserMapper;
import aston.intensiv.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDto createUser(NewUserRequest request) {
        log.info("Создание пользователя с email={}", request.getEmail());
        try {
            User user = userRepository.save(UserMapper.mapToUser(request));
            return UserMapper.mapToUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Пользователь с email " + request.getEmail() + " уже существует");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UpdateUserRequest request, Long userId) {
        log.info("Обновление пользователя id={}", userId);
        User user = findUserOrTrow(userId);
        chekEmail(request.getEmail(), userId);
        User updatedUser = UserMapper.updateUserFields(user, request);
        updatedUser = userRepository.save(updatedUser);
        log.info("Пользователь обновлён id={}", userId);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Получение списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.mapToUserDto(findUserOrTrow(id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя id={}", id);
        findUserOrTrow(id);
        userRepository.deleteById(id);
        log.info("Пользователь удалён id={}", id);
    }

    private User findUserOrTrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("Пользователь не неайден с id={}", id);
            return new NotFoundException("Пользователься с id " + id + " ненайден");
        });
    }

    private void chekEmail(String email, Long id) {
        if (userRepository.findByEmailAndIdNot(email, id).isPresent()) {
            log.warn("Пользователь с email={} существует", email);
            throw new ConflictException("Пользователь с email " + email + " существует");
        }
    }
}
