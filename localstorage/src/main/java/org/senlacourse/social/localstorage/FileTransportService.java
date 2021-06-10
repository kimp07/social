package org.senlacourse.social.localstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.localstorage.IFileTransportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private String generateFileName(MultipartFile file, Long currentMillis) {
        return "/f_" + currentMillis.toString() + getFileNameSuffix(file);
    }

    private File getConvertFile(MultipartFile file) {
        return new File(localStorageDir,
                generateFileName(
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

    private String uploadFileToLocalStorage(MultipartFile file) throws ApplicationException {
        File convertFile = getConvertFile(file);
        try (FileOutputStream fout = new FileOutputStream(convertFile)) {
            fout.write(file.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), e);
        }
        return convertFile.getName();
    }

    @Override
    public String uploadFile(MultipartFile file) throws ApplicationException, ObjectNotFoundException {
        if (!storageDirExists()) {
            ApplicationException e = new ApplicationException("Directory " + localStorageDir + " not exixts");
            log.error(e.getMessage(), e);
            throw e;
        }
        return uploadFileToLocalStorage(file);
    }

    private File getImageFile(String imgFileName) throws ObjectNotFoundException {
        return new File(
                localStorageDir,
                imgFileName);
    }

    @Override
    public ResponseEntity<Object> downloadFile(String imgFileName) throws ObjectNotFoundException, ApplicationException {
        File file = getImageFile(imgFileName);
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
