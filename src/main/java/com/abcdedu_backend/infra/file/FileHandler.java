package com.abcdedu_backend.infra.file;

import java.io.File;

public interface FileHandler {

    String upload(File file, FileDirectory directory, String fileName);
    String getPresignedUrl(String objectKey);
    void delete(String objectKey);

}
