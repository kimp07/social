package org.senlacourse.social.api.localstorage;

import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileTransportService {

    void uploadFile(Long userId, MultipartFile file) throws ApplicationException, ObjectNotFoundException;

    ResponseEntity<Object> downloadFile(Long imageId) throws ObjectNotFoundException, ApplicationException;
}
