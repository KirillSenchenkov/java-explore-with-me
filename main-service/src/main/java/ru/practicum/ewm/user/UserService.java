package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(NewUserRequest newUserRequest) {
        return UserMapper.userToUserDto(userRepository.save(UserMapper.dtoToUser(newUserRequest)));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (userIds == null) {
            return userRepository.findAll(pageable).map(UserMapper::userToUserDto).getContent();
        } else {
            return userRepository.findAllByIdIn(userIds, pageable).stream()
                    .map(UserMapper::userToUserDto)
                    .collect(Collectors.toList());
        }
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(userId);
    }
}

