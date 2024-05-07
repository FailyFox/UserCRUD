package com.usercrud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Email is missing")
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "First name is missing")
    private String firstName;
    @NotBlank(message = "Last name is missing")
    private String lastName;
    @NotNull(message = "Birth date is missing")
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}