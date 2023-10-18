package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConfirmedRequests {
    private Long count;
    private Long event;
}