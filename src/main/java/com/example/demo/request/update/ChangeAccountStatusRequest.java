package com.example.demo.request.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAccountStatusRequest {
    String setAccountStatus;
}
