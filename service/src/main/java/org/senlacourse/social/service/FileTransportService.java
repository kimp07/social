package org.senlacourse.social.service;

import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.localstorage.IFileTransportService;
import org.senlacourse.social.service.util.MultipartInputStreamFileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Log4j
public class FileTransportService implements IFileTransportService {

    @Value("${files.dir:http://localhost:8083/images}")
    private String uri;

    private InputStreamResource getMultipartStreamResource(MultipartFile file) throws ApplicationException {
        if (file.isEmpty()) {
            ApplicationException e = new ApplicationException("Resource is empty");
            log.error(e.getMessage(), e);
            throw e;
        }
        try {
            return new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws ApplicationException, ObjectNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", getMultipartStreamResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .postForEntity(uri, requestEntity, String.class).getBody();
    }

    @Override
    public ResponseEntity<Object> downloadFile(String imgFileName) throws ObjectNotFoundException, ApplicationException {
        return new ResponseEntity<>(uri + "/" + imgFileName, HttpStatus.OK);
    }
}
