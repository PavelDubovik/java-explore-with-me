package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(NewUserRequest newUserRequest);
}
