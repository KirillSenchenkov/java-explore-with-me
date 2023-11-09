package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.request.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public final class ParticipationRequestDto {
    private final Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime created;

    private final Long event;

    private final Long requester;

    private final RequestStatus status;
}
