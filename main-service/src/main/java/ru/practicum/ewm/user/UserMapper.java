package ru.practicum.ewm.user;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

@UtilityClass
public class UserMapper {

    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserShortDto userToUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public User dtoToUser(NewUserRequest newUserRequest) {
        return new User(newUserRequest.getName(), newUserRequest.getEmail());
    }
}
