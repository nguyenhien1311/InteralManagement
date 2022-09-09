package com.example.demo.request.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    @NotBlank(message = "Title must not be blank")
    @Size(min = 20, max = 200, message = "Title length at least 30 and max is 200 character")
    private String title;
    @NotBlank(message = "Banner must not be blank")
    private String banner;
    @NotBlank(message = "Content must not be blank")
    private String content;
    @NotBlank(message = "timeBegin must not be blank")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String timeBegin;
    @NotBlank(message = "timeEnd must not be blank")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String timeEnd;
}
