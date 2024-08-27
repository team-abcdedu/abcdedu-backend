package com.abcdedu_backend.infra.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileHandler {

    String upload(MultipartFile file, FileDirectory directory);
    void delete(String imageUrl);

}
