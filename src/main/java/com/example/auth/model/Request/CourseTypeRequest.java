package com.example.auth.model.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTypeRequest {
    @NotBlank(message = "type required")
    private String type;
}
