package com.abcdedu_backend.infra.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalFileHandler implements FileHandler{

    @Override
    public String upload(MultipartFile file, FileDirectory directory){
        //Todo s3생성 후 작업 예정
        return "imageUrl";
    }

    @Override
    public void delete(String imageUrl) {

    }
}
