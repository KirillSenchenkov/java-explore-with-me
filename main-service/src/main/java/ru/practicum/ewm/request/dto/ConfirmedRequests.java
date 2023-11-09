package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ConfirmedRequests {
    private final Long count;
    private final Long event;
}