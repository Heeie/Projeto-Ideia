package Ideias.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistoDto(@NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 2, max = 80) String nome,
        @NotBlank @Size(min = 6, max = 200) String password) {}
