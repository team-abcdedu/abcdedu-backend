package com.abcdedu_backend.infra.file;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3FileHandler implements FileHandler {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile multipartFile, FileDirectory directory, String fileName){
        log.info(multipartFile.getOriginalFilename());
        try {
            String extension = getExtension(multipartFile);
            InputStream inputStream = multipartFile.getInputStream();
            String key = createUploadKey(directory, fileName, extension);
            amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream,null));
            return key;
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
            throw new ApplicationException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }

    @Override
    public String getPresignedUrl(String objectKey) {
        Date expiration = getDateOneHourLater();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private String createUploadKey(FileDirectory directory, String fileName, String extension) {
        return directory.getDirectoryName() + fileName + extension;
    }

    private String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return extension;
    }

    @Override
    public void delete(String objectKey) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.S3_OBJECT_NOT_FOUND);
        }
    }

    private static Date getDateOneHourLater() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += Duration.ofHours(1).toMillis();
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
