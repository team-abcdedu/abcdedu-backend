package com.abcdedu_backend.utils;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Deprecated
@Slf4j
public class FileUtil {

    public static File convertToFile(MultipartFile multipartFile) {
        File tempFile = new File(multipartFile.getOriginalFilename());
        try {
            try (InputStream inputStream = multipartFile.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new ApplicationException(ErrorCode.FILE_ERROR);
        }
    }

}

