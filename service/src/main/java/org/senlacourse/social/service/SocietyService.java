package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.service.ISocietyService;
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
public class SocietyService extends AbstractService<Society> implements ISocietyService {

    private static final String USER_NOT_DEFINED_FOR_ID = "User not defined for id=";

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

    @Override
    public Page<SocietyDto> findAll(Pageable pageable) {
        return societyDtoMapper.map(societyRepository.findAll(pageable));
    }

    @Override
    public Page<SocietyMemberDto> findAllSocietyMembersBySocietyId(Long societyId, Pageable pageable) {
        return societyMemberDtoMapper.map(
                societyMemberRepository.findAllBySocietyId(societyId, pageable));
    }

    @Override
    public Optional<SocietyDto> createNewSociety(NewSocietyDto dto) throws ObjectNotFoundException {
        User owner = userRepository.findById(dto.getOwnerId()).orElse(null);
        validateEntityNotNull(owner, USER_NOT_DEFINED_FOR_ID + dto.getOwnerId());
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

    @Override
    public void removeUserFromSociety(Long userId, Long societyId) throws ObjectNotFoundException {
        Society society = findEntityById(societyId);
        if (!society.getOwner().getId().equals(userId)) {
            SocietyMember societyMember = societyMemberRepository
                    .findByUserIdAndSocietyId(userId, societyId)
                    .orElse(null);
            validateEntityNotNull(societyMember,
                    "Society member not defined for user id=" + userId + " and society id=" + societyId);
            assert societyMember != null;
            societyMemberRepository.deleteById(societyMember.getId());
        }
    }

    @Override
    public SocietyMemberDto addUserToSociety(Long userId, Long societyId) throws ObjectNotFoundException {
        Society society = findEntityById(societyId);
        User user = userRepository.findById(userId).orElse(null);
        validateEntityNotNull(user, USER_NOT_DEFINED_FOR_ID + userId);
        return societyMemberDtoMapper
                .fromEntity(
                        societyMemberRepository.save(
                                new SocietyMember()
                                        .setSociety(society)
                                        .setUser(user)));
    }

    @Override
    public void deleteSocietyById(Long id) throws ObjectNotFoundException {
        // TODO after WallService
    }
}
