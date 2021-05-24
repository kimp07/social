package org.senlacourse.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileTransport {

    @Value("${files.dir:/localstorage}")
    private String imgDirectory;
    @Value("${files.max-size:1024KB}")
    private String maxFileSize;
    @Value("${files.max-request-size:1024KB}")
    private String maxRequestSize;

    public String uploadFile(MultipartFile file) {
        return "img.jpg";
    }

}
