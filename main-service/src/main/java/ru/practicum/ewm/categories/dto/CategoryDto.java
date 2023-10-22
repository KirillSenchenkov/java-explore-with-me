package ru.practicum.ewm.categories.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public final class CategoryDto {
    private final Long id;

    @Size(min = 1, max = 50)
    @NotBlank
    private final String name;
}
