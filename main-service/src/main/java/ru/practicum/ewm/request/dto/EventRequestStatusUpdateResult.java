package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public final class EventRequestStatusUpdateResult {
    private final List<ParticipationRequestDto> confirmedRequests;

    private final List<ParticipationRequestDto> rejectedRequests;
}
