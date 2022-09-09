package com.example.demo.request.news;

import com.example.demo.domain.Comment;
import com.example.demo.domain.HashTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNewsRequest {

    private String id;

    private String createUserId;

}
