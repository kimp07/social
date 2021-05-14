package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.dto.NewSocietyDto;
import org.senlacourse.social.dto.SocietyDto;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ISocietyService {

    Page<SocietyDto> findAll(Pageable pageable);

    Page<SocietyMemberDto> findAllSocietyMembersBySocietyId(Long societyId, Pageable pageable);

    Optional<SocietyDto> createNewSociety(NewSocietyDto dto) throws ObjectNotFoundException;

    void removeUserFromSociety(Long userId, Long societyId) throws ObjectNotFoundException;

    SocietyMemberDto addUserToSociety(Long userId, Long societyId) throws ObjectNotFoundException;

    void deleteSocietyById(Long id) throws ObjectNotFoundException;
}
