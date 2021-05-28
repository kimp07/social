package org.senlacourse.social.localstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.localstorage.IFileTransportService;
import org.senlacourse.social.api.service.IUserImageService;
import org.senlacourse.social.dto.NewUserImageDto;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
@RequiredArgsConstructor
@Log4j
public class FileTransportService implements IFileTransportService {

    private final IUserImageService userImageService;

    @Value("${files.dir:/imgstorage}")
    private String localStorageDir;

    private String getFileNameSuffix(MultipartFile file) {
        String origFileName = file.getOriginalFilename();
        if (origFileName != null && origFileName.lastIndexOf(".") > 0) {
            return origFileName.substring(
                    origFileName.lastIndexOf("."));
        } else {
            return "";
        }
    }

    private String generateFileName(Long userId, MultipartFile file, Long currentMillis) {
        return "/f_" + userId.toString() + "_" + currentMillis.toString() +
                getFileNameSuffix(file);
    }

    private File getConvertFile(Long userId, MultipartFile file) {
        return new File(localStorageDir,
                generateFileName(
                        userId,
                        file,
                        System.currentTimeMillis()));
    }

    private boolean storageDirExists() {
        File directory = new File(localStorageDir);
        if (!directory.exists()) {
            return directory.mkdir();
        }
        return true;
    }

    private String uploadFileToLocalStorage(Long userId, MultipartFile file) throws ApplicationException {
        File convertFile = getConvertFile(userId, file);
        try (FileOutputStream fout = new FileOutputStream(convertFile)) {
            fout.write(file.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), e);
        }
        return convertFile.getName();
    }

    @AuthorizedUser
    @Override
    public void uploadFile(Long userId, MultipartFile file) throws ApplicationException, ObjectNotFoundException {
        if (!storageDirExists()) {
            ApplicationException e = new ApplicationException("Directory " + localStorageDir + " not exixts");
            log.error(e.getMessage(), e);
            throw e;
        }
        String fileName = uploadFileToLocalStorage(userId, file);
        userImageService.save(
                new NewUserImageDto()
                        .setImgFileName(fileName));
    }

    private File getImageFile(Long imageId) throws ObjectNotFoundException {
        return new File(
                localStorageDir,
                userImageService
                        .findById(imageId)
                        .getImgFileName());
    }

    @Override
    public ResponseEntity<Object> downloadFile(Long imageId) throws ObjectNotFoundException, ApplicationException {
        File file = getImageFile(imageId);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", String.format("attachment; filename=`%s`", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(
                            MediaType.parseMediaType("application/txt"))
                    .body(resource);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e);
        }
    }
}
