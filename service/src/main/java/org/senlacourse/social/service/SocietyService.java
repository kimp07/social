package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.domain.Society;
import org.senlacourse.social.domain.SocietyMember;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewSocietyDto;
import org.senlacourse.social.dto.SocietyDto;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.senlacourse.social.mapstruct.SocietyDtoMapper;
import org.senlacourse.social.mapstruct.SocietyMemberDtoMapper;
import org.senlacourse.social.repository.SocietyMemberRepository;
import org.senlacourse.social.repository.SocietyRepository;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class SocietyService extends AbstractService<Society> {

    private final SocietyRepository societyRepository;
    private final SocietyMemberRepository societyMemberRepository;
    private final UserRepository userRepository;
    private final SocietyDtoMapper societyDtoMapper;
    private final SocietyMemberDtoMapper societyMemberDtoMapper;

    @Override
    Society findEntityById(Long id) throws ObjectNotFoundException {
        Society society = societyRepository.findById(id).orElse(null);
        validateEntityNotNull(society, "Society not defined for id=" + id);
        return society;
    }

    public Page<SocietyDto> findAll(Pageable pageable) {
        return societyDtoMapper.map(societyRepository.findAll(pageable));
    }

    public Page<SocietyMemberDto> findAllSocietyMembersBySocietyId(Long societyId, Pageable pageable) {
        return societyMemberDtoMapper.map(
                societyMemberRepository.findAllBySocietyId(societyId, pageable));
    }

    public Optional<SocietyDto> createNewSociety(NewSocietyDto dto) throws ObjectNotFoundException {
        User owner = userRepository.findById(dto.getOwnerId()).orElse(null);
        validateEntityNotNull(owner, "User not defined for id=" + dto.getOwnerId());
        Society society = new Society()
                .setTitle(dto.getTitle())
                .setOwner(owner);
        societyRepository.save(society);
        SocietyMember societyMember = new SocietyMember()
                .setSociety(society)
                .setUser(owner);
        societyMemberRepository.save(societyMember);
        return Optional.ofNullable(societyDtoMapper.fromEntity(society));
    }
}
