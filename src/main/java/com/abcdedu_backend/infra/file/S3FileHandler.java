package com.abcdedu_backend.infra.file;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "prod"})
public class S3FileHandler implements FileHandler {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file, FileDirectory directory, String fileName) {
        try (InputStream inputStream = file.getInputStream()) {
            String extension = getExtension(file);
            String key = createUploadKey(directory, fileName, extension);
            log.info("S3 upload 성공 key: {}", key);
            amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, null));
            return key;
        } catch (Exception e) {
            log.error("S3 upload 실패 : {}", e.getLocalizedMessage());
            throw new ApplicationException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }

    @Override
    public String getPresignedUrl(String objectKey) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignUrlRequestForExtension(objectKey);
        log.info("generatePresignedUrlRequest 생성 완료");
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private GeneratePresignedUrlRequest getGeneratePresignUrlRequestForExtension(String objectKey) {

        Date expiration = getDateOneHourLater();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        log.info("확장자를 보여줍니다. {}", getExtension(objectKey));
        if (getExtension(objectKey).equals("pdf")) {
            log.info("pdf파일을 조회합니다.");
            return generatePresignedUrlRequest
                    .withResponseHeaders(new ResponseHeaderOverrides()
                            .withContentDisposition("inline")
                            .withContentType("application/pdf"));
        }
        else {
            return generatePresignedUrlRequest;
        }
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

    private String getExtension(String objectKey) {
        String extension = "";
        if (objectKey.contains(".")) {
            extension = objectKey.substring(objectKey.lastIndexOf("."));
            log.info("getExtension() {}", extension);
        }
        return extension;
    }

    @Override
    public void delete(String objectKey) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
        } catch (Exception e) {
            log.error("S3 deleted 실패 : {}", e.getLocalizedMessage());
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
