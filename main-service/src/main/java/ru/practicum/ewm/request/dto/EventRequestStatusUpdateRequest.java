package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public final class EventRequestStatusUpdateRequest {
    private final List<Long> requestIds;

    private final RequestStatus status;
}
