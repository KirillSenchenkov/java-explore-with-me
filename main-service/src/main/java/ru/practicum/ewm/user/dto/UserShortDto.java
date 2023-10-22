package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UserShortDto {
    private final Long id;
    private final String name;
}
