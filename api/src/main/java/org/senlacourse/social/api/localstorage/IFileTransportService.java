package org.senlacourse.social.api.localstorage;

import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface IFileTransportService {

    void uploadFile(MultipartFile file) throws ApplicationException, ObjectNotFoundException;
}
