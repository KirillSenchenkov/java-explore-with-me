package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public final class NewUserRequest {

    @NotBlank
    @Size(min = 2, max = 250)
    private final String name;

    @NotEmpty
    @Email
    @Size(min = 6, max = 254)
    private final String email;
}
