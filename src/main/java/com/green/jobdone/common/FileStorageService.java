package com.green.jobdone.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    public String uploadFile(MultipartFile file) throws IOException;
}
