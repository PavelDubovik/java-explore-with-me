package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        UserDto userDto = userMapper.toUserDto(newUserRequest);
        userDto = userRepository.save(userDto);
        log.info("User with id = {} is created", userDto.getId());
        return userDto;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        // TODO: проверить на положительное значение from и size
        Pageable pageable = PageRequest.of(from, size);
        List<UserDto> userDtoList;
        if (ids.isEmpty()) {
            userDtoList = userRepository.findAllByOrderById(pageable);
        } else {
            userDtoList = userRepository.findAllByIdInOrderById(ids, pageable);
        }
        return userDtoList;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("User with id = {} is deleted", userId);
    }
}
