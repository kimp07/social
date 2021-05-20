package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewSocietyDto;
import org.senlacourse.social.dto.SocietyDto;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ISocietyService {

    Page<SocietyDto> findAll(Pageable pageable);

    Page<SocietyMemberDto> findAllSocietyMembersBySocietyId(Long societyId, Pageable pageable);

    Optional<SocietyDto> createNewSociety(NewSocietyDto dto) throws ObjectNotFoundException, ServiceException;

    void removeUserFromSociety(Long userId, Long societyId) throws ObjectNotFoundException, ServiceException;

    SocietyMemberDto addUserToSociety(Long userId, Long societyId) throws ObjectNotFoundException, ServiceException;

    public Optional<SocietyMemberDto> findSocietyMemberByUserIdAndSocietyId(Long userId, Long societyId)
            throws ServiceException;

    public boolean isUserMemberOfSociety(Long userId, Long societyId);

    void deleteSocietyById(Long id) throws ObjectNotFoundException;
}
