package ru.example.news.web.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertUserRequest {

    @NotBlank(message = "User name should be field")
    @Size(min = 3, max = 20, message = "User name length should be between 3 and 20 characters")
    private String name;

    @NotBlank(message = "User email name should be field")
    @Size(min = 10, max = 40, message = "User email length should be between 10 and 40 characters")
    private String email;
}
