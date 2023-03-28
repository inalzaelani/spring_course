package com.example.auth.model.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FormDataWithFile {

    private MultipartFile file;
}
