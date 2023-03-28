package com.example.auth.Service;

import com.example.auth.repo.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileService implements IFileService{

    private FileRepo fileRepo;

    @Autowired
    public FileService(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return fileRepo.store(file);
    }

    @Override
    public Resource download(String filename) {
        return fileRepo.load(filename);
    }
}
