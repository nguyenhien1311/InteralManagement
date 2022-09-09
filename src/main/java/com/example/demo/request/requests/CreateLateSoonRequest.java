package com.example.demo.request.requests;

import com.example.demo.constant.LateSoonType;
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
public class CreateLateSoonRequest {
    @Email(regexp = ".+@ntq-solution.com.vn", message = "Email need to end with @ntq-solution.com.vn")
    @NotBlank(message = "Receiver email must not be blank or null")
    private String receiverEmail;

    @NotBlank(message = "Content must not be blank or null")
    private String content;

    private Reasons reason;

    private LateSoonType lateSoonType;

    @NotBlank(message = "timeBegin must not be blank")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dayRequest;

    private Shift shift;

    @NotBlank(message = "Write the time you need")
    private String timeLateOrSoon;
}
