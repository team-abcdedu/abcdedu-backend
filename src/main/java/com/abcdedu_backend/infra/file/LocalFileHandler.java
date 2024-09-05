package com.abcdedu_backend.infra.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

//@Component
public class LocalFileHandler implements FileHandler{

    @Override
    public String upload(MultipartFile file, FileDirectory directory, String fileName){
        //Todo s3생성 후 작업 예정
        return "imageUrl";
    }

    @Override
    public String getPresignedUrl(String objectKey) {
        return "";
    }

    @Override
    public void delete(String imageUrl) {

    }
}
