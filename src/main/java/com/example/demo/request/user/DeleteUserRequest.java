package com.example.demo.request.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeleteUserRequest {
    @NotNull(message = "Id User to Delete must not be null")
    @NotEmpty(message = "Id User to Delete must not be empty")
    @NotBlank(message = "Id User to Delete must not be blank")
    private String id;
    @NotNull(message = "Email User to Delete must not be null")
    @NotEmpty(message = "Email User to Delete must not be empty")
    @NotBlank(message = "Email User to Delete must not be blank")
    @Email(regexp = ".+@ntq-solution.com.vn", message = "Email need to end with @ntq-solution.com.vn")
    private String email;
}
