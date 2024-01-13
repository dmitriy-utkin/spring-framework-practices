package ru.example.news.web.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.news.model.RoleType;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertUserRequest {

//    @NotBlank(message = "User name should be field")
//    @Size(min = 3, max = 20, message = "User name length should be between 3 and 20 characters")
    private String username;

//    @NotBlank(message = "User email name should be field")
//    @Size(min = 10, max = 40, message = "User email length should be between 10 and 40 characters")
    private String email;

//    @NotBlank(message = "Password should be field")
//    @Size(min = 4, max = 18, message = "Password length should be between 4 and 18 characters")
    private String password;

    @Builder.Default
    private Set<RoleType> roles = Collections.singleton(RoleType.ROLE_USER);
}
