package com.abcdedu_backend.infra.file;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
@Profile({"local", "test"})
public class LocalFileHandler implements FileHandler{

    private final String UPLOAD_DIR = "./uploads/";

    @Override
    public String upload(MultipartFile file, FileDirectory directory, String fileName) {
        try (InputStream inputStream = file.getInputStream()){
            Path directoryPath = Paths.get(UPLOAD_DIR + directory.getDirectoryName());
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(fileName+getExtension(file));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            log.info(e.getLocalizedMessage());
            throw new ApplicationException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }
    @Override
    public String getPresignedUrl(String objectKey) {
        return objectKey;
    }
    @Override
    public void delete(String objectKey) {
        try {
            Path filePath = Paths.get(objectKey);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.S3_OBJECT_NOT_FOUND);
        }
    }

    private String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        log.info(originalFilename);
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return extension;
    }
}
