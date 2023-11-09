package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public final class NewCompilationDto {

    private final List<Long> events;

    private boolean pinned = false;

    @NotBlank
    @Size(min = 1, max = 50)
    private final String title;

    public Boolean getPinned() {
        return pinned;
    }
}