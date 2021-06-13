package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewSocietyDto;
import org.senlacourse.social.dto.SocietyDto;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISocietyService {

    SocietyDto findById(Long id) throws ObjectNotFoundException;

    Page<SocietyDto> findAll(Pageable pageable);

    Page<SocietyDto> findAll(String title, Pageable pageable);

    Page<SocietyMemberDto> findAllSocietyMembersBySocietyId(Long societyId, Pageable pageable);

    SocietyDto createNewSociety(NewSocietyDto dto) throws ObjectNotFoundException, ServiceException;

    void removeUserFromSociety(UserIdDto dto, Long societyId) throws ObjectNotFoundException, ServiceException;

    SocietyMemberDto addUserToSociety(UserIdDto dto, Long societyId) throws ObjectNotFoundException, ServiceException;

    SocietyMemberDto findSocietyMemberByUserIdAndSocietyId(UserIdDto dto, Long societyId)
            throws ServiceException;

    boolean isUserMemberOfSociety(Long userId, Long societyId);

    void deleteSocietyById(Long id) throws ObjectNotFoundException;
}
