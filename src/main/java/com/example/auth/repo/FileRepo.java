package com.example.auth.repo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepo {

    String store(MultipartFile file);

    Resource load(String filename);
}
