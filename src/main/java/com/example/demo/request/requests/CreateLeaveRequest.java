package com.example.demo.request.requests;

import com.example.demo.constant.OffType;
import com.example.demo.constant.Reasons;
import com.example.demo.constant.Shift;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeaveRequest {

    @Email(regexp = ".+@ntq-solution.com.vn", message = "Email need to end with @ntq-solution.com.vn")
    @NotBlank(message = "Receiver email must not be blank or null")
    private String receiverEmail;

    @NotBlank(message = "Content must not be blank or null")
    private String content;

    private Reasons reason;

    @NotBlank(message = "Day start must not be blank or null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dayStart;

    private Shift shiftStart;

    @NotBlank(message = "Day end must not be blank or null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dayEnd;

    private Shift shiftEnd;

    private OffType offType;
}
