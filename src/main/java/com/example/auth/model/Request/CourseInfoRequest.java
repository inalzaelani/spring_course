package com.example.auth.model.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseInfoRequest {
    @NotBlank(message = "duration required")
    private String duration;

    @NotBlank(message = "level required")
    private String level;

}
