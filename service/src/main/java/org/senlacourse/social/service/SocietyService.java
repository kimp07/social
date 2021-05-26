package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.ISocietyService;
import org.senlacourse.social.api.service.IUserService;
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
import org.senlacourse.social.repository.WallMessageCommentRepository;
import org.senlacourse.social.repository.WallMessageRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
@Log4j
public class SocietyService extends AbstractService<Society> implements ISocietyService {

    private final SocietyRepository societyRepository;
    private final SocietyMemberRepository societyMemberRepository;
    private final IUserService userService;
    private final WallMessageRepository wallMessageRepository;
    private final WallMessageCommentRepository wallMessageCommentRepository;
    private final SocietyDtoMapper societyDtoMapper;
    private final SocietyMemberDtoMapper societyMemberDtoMapper;

    private SocietyMember findEntitySocietyMemberByUserIdAndSocietyId(Long userId, Long societyId)
            throws ObjectNotFoundException {
        return validateEntityNotNull(
                societyMemberRepository
                        .findByUserIdAndSocietyId(userId, societyId).orElse(null));
    }

    @Override
    public Society findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                societyRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public Page<SocietyDto> findAll(Pageable pageable) {
        return societyDtoMapper.map(
                societyRepository.findAll(pageable));
    }

    @Override
    public Page<SocietyDto> findAll(String title, Pageable pageable) {
        if (title == null || title.isEmpty()) {
            return societyDtoMapper.map(
                    societyRepository.findAll(pageable));
        } else {
            return societyDtoMapper.map(
                    societyRepository.findAllByTitle(title, pageable));
        }
    }

    @Override
    public Page<SocietyMemberDto> findAllSocietyMembersBySocietyId(Long societyId, Pageable pageable) {
        return societyMemberDtoMapper.map(
                societyMemberRepository.findAllBySocietyId(societyId, pageable));
    }

    @AuthorizedUser
    @Override
    public SocietyDto createNewSociety(NewSocietyDto dto) throws ObjectNotFoundException, ServiceException {
        User owner = userService.findEntityById(dto.getOwnerId());
        Society society = new Society()
                .setTitle(dto.getTitle())
                .setOwner(owner);
        societyRepository.save(society);
        SocietyMember societyMember = new SocietyMember()
                .setSociety(society)
                .setUser(owner);
        societyMemberRepository.save(societyMember);
        return societyDtoMapper.fromEntity(society);
    }

    @AuthorizedUser
    @Override
    public void removeUserFromSociety(Long userId, Long societyId) throws ObjectNotFoundException, ServiceException {
        Society society = findEntityById(societyId);
        if (!society.getOwner().getId().equals(userId)) {
            SocietyMember societyMember = findEntitySocietyMemberByUserIdAndSocietyId(userId, societyId);
            societyMemberRepository.deleteById(societyMember.getId());
        }
    }

    @AuthorizedUser
    @Override
    public SocietyMemberDto addUserToSociety(Long userId, Long societyId)
            throws ObjectNotFoundException, ServiceException {
        Society society = findEntityById(societyId);
        User user = userService.findEntityById(userId);
        if (isUserMemberOfSociety(userId, societyId)) {
            return societyMemberDtoMapper
                    .fromEntity(
                            findEntitySocietyMemberByUserIdAndSocietyId(userId, societyId));
        } else {
            return societyMemberDtoMapper
                    .fromEntity(
                            societyMemberRepository.save(
                                    new SocietyMember()
                                            .setSociety(society)
                                            .setUser(user)));
        }
    }

    @AuthorizedUser
    @Override
    public SocietyMemberDto findSocietyMemberByUserIdAndSocietyId(Long userId, Long societyId)
            throws ServiceException {
        return societyMemberDtoMapper
                .fromEntity(
                        findEntitySocietyMemberByUserIdAndSocietyId(userId, societyId));
    }

    @AuthorizedUser
    @Override
    public boolean isUserMemberOfSociety(Long userId, Long societyId) {
        return societyMemberRepository.findByUserIdAndSocietyId(userId, societyId).isPresent();
    }

    @Override
    public void deleteSocietyById(Long id) throws ObjectNotFoundException {
        Society society = findEntityById(id);
        societyMemberRepository.deleteAllBySocietyId(society.getId());
        wallMessageCommentRepository.deleteAllByWallId(society.getId());
        wallMessageRepository.deleteAllByWallId(society.getId());
    }
}
