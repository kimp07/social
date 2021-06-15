package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.domain.Image;
import org.senlacourse.social.dto.ImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService extends IService<Image> {

    ImageDto findById(Long id) throws ObjectNotFoundException;

    Page<ImageDto> findAll(Pageable pageable);

    ImageDto save(String imgFileName);

    ImageDto save(MultipartFile multipartFile);

    Object getImageFileById(Long id) throws ObjectNotFoundException;
}
