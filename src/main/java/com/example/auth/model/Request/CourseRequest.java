package com.example.auth.model.Request;

import com.example.auth.model.Entities.CourseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CourseRequest {
    @NotBlank(message = "title cannot be blank")
    private String title;
    private String description;
    private MultipartFile file;
    @NotBlank(message = "duration cannot be blank")
    private String duration;
    @NotBlank(message = "level cannot be blank")
    private String level;
    @NotNull(message = "Type is required")
    private Long courseType;
}
