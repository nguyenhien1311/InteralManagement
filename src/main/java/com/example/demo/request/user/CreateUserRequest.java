package com.example.demo.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private MultipartFile file;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(regexp = ".+@ntq-solution.com.vn", message = "Email need to end with @ntq-solution.com.vn")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 20, message = "Password need at least 8 and not over 20 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password not valid")
    private String password;

    private String key;
}
