package org.senlacourse.social.localstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.localstorage.IFileTransportService;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.api.service.IUserImageService;
import org.senlacourse.social.dto.NewUserImageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j
public class FileTransportService implements IFileTransportService {

    private final IAuthorizedUserService authorizedUserService;
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

    private String generateFileName(MultipartFile file, Long userId, Long currentMillis) {
        return "/f_" + userId.toString() + "_" + currentMillis.toString() +
                getFileNameSuffix(file);
    }

    private File getConvertFile(MultipartFile file, Long userId) {
        return new File(
                localStorageDir
                        + generateFileName(
                        file,
                        userId,
                        System.currentTimeMillis()));
    }

    private boolean storageDirExists() {
        File directory = new File(localStorageDir);
        if (!directory.exists()){
            return directory.mkdir();
        }
        return true;
    }

    private String uploadFileToLocalStorage(MultipartFile file, Long userId) throws ApplicationException {
        File convertFile = getConvertFile(file, userId);
        try (FileOutputStream fout = new FileOutputStream(convertFile)) {
            fout.write(file.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), e);
        }
        return convertFile.getName();
    }

    @Override
    public void uploadFile(MultipartFile file) throws ApplicationException, ObjectNotFoundException {
        if (!storageDirExists()) {
            ApplicationException e = new ApplicationException("Directory " + localStorageDir + " not exixts");
            log.error(e.getMessage(), e);
            throw e;
        }
        String fileName = uploadFileToLocalStorage(file, authorizedUserService.getAuthorizedUserId());
        userImageService.save(
                new NewUserImageDto()
                .setImgFileName(fileName));
    }
}
