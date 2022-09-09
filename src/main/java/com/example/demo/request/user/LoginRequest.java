package com.example.demo.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(regexp = ".+@ntq-solution.com.vn", message = "Email need to end with @ntq-solution.com.vn")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "Password must not to be null")
    @Size(min = 8, max = 20, message = "Password need at least 8 and not over 20 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password not valid")
    private String password;
}
