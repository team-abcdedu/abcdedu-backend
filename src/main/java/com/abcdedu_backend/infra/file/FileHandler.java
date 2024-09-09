package com.abcdedu_backend.infra.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {

    String upload(MultipartFile file, FileDirectory directory, String fileName);
    String getPresignedUrl(String objectKey);
    void delete(String objectKey);

}
