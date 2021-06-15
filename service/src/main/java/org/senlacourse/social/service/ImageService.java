package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.localstorage.IFileTransportService;
import org.senlacourse.social.api.service.IImageService;
import org.senlacourse.social.domain.Image;
import org.senlacourse.social.dto.ImageDto;
import org.senlacourse.social.mapstruct.ImageDtoMapper;
import org.senlacourse.social.repository.ImageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService extends AbstractService<Image> implements IImageService {

    private final ImageRepository imageRepository;
    private final ImageDtoMapper imageDtoMapper;
    private final IFileTransportService fileTransportService;

    @Override
    public Image findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(imageRepository.findById(id).orElse(null));
    }

    @Override
    public ImageDto findById(Long id) throws ObjectNotFoundException {
        return imageDtoMapper
                .fromEntity(
                        findEntityById(id));
    }

    @Override
    public Page<ImageDto> findAll(Pageable pageable) {
        return imageDtoMapper.map(
                imageRepository.findAll(pageable));
    }

    @Transactional
    @Override
    public ImageDto save(String imgFileName) {
        return imageDtoMapper.fromEntity(
                imageRepository.save(
                        new Image().setImgFileName(imgFileName)));
    }

    @Transactional
    @Override
    public ImageDto save(MultipartFile multipartFile) {
        return save(fileTransportService
                .uploadFile(multipartFile));
    }

    @Override
    public Object getImageFileById(Long id) throws ObjectNotFoundException {
        return fileTransportService
                .downloadFile(findById(id)
                        .getImgFileName());
    }
}
