package com.example.auth.Controller;

import com.example.auth.Service.IFileService;
import com.example.auth.model.Request.FormDataWithFile;
import com.example.auth.model.Response.SuccessResponse;
import org.apache.catalina.util.ResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final IFileService fileService;

    @Autowired
    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity upload(FormDataWithFile formDataWithFile){
        MultipartFile file = formDataWithFile.getFile();
        String filePath = fileService.uploadFile(file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success upload file", filePath));

    }

    @GetMapping
    public ResponseEntity download(@RequestParam String filename) throws MalformedURLException {
        Resource file = fileService.download(filename);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity showImage(@PathVariable("filename") String fileName) throws IOException{
        Resource file = fileService.download(fileName);

        byte[] imageBytes = StreamUtils.copyToByteArray(file.getInputStream());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
    }
}
