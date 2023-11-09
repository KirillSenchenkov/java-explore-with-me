package ru.practicum.ewm.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public final class LocationDto {
    @NotNull
    private final Float lat;

    @NotNull
    private final Float lon;
}
