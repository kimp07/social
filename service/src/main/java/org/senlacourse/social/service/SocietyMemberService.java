package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.Society;
import org.senlacourse.social.domain.SocietyMember;
import org.senlacourse.social.domain.SocietyMemberId;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.SocietyMemberDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.SocietyMemberDtoMapper;
import org.senlacourse.social.repository.SocietyMemberRepository;
import org.senlacourse.social.repository.SocietyRepository;
import org.senlacourse.social.repository.UserRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
public class SocietyMemberService {

    private final SocietyMemberRepository societyMemberRepository;
    private final UserRepository userRepository;
    private final SocietyRepository societyRepository;
    private final SocietyMemberDtoMapper societyMemberDtoMapper;

    private SocietyMember findByUserIdAndSocietyId(Long userId, Long societyId) throws ObjectNotFoundException {
        return societyMemberRepository
                        .findOneByIdUserIdAndIdSocietyId(userId, societyId)
                        .orElseThrow(() -> {
                            ObjectNotFoundException e = new ObjectNotFoundException();
                            log.error(e.getMessage(), e);
                            throw e;
                        });
    }

    private User findUserById(Long id) throws ObjectNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> {
                ObjectNotFoundException e = new ObjectNotFoundException();
                log.error(e.getMessage(), e);
                throw e;
        });
    }

    private Society findSocietyById(Long id) throws ObjectNotFoundException {
        return societyRepository.findById(id).orElseThrow(() -> {
            ObjectNotFoundException e = new ObjectNotFoundException();
            log.error(e.getMessage(), e);
            throw e;
        });
    }

    private boolean isMemberOfSociety(Long userId, Long societyId) {
        return societyMemberRepository.findOneByIdUserIdAndIdSocietyId(userId, societyId).isPresent();
    }

    @AuthorizedUser
    public SocietyMemberDto findByUserIdAndSocietyId(UserIdDto dto, Long societyId) throws ObjectNotFoundException {
        return societyMemberDtoMapper.fromEntity(findByUserIdAndSocietyId(dto.getAuthorizedUserId(), societyId));
    }

    public Page<SocietyMemberDto>  findAllBySocietyId(Long societyId, Pageable pageable) {
        return societyMemberDtoMapper.map(
                societyMemberRepository.findAllByIdSocietyId(societyId, pageable));
    }

    @AuthorizedUser
    public Page<SocietyMemberDto>  findAllByUserId(UserIdDto dto, Pageable pageable) {
        return societyMemberDtoMapper.map(
                societyMemberRepository.findAllByIdUserId(dto.getAuthorizedUserId(), pageable));
    }

    private void addUserToSociety(User user, Society society) {
        societyMemberRepository.save(
                new SocietyMember().setId(
                        new SocietyMemberId().setSociety(society).setUser(user)));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @AuthorizedUser
    public void addUserToSociety(UserIdDto dto, Long societyId) throws ObjectNotFoundException, ServiceException {
        User user = findUserById(dto.getAuthorizedUserId());
        Society society = findSocietyById(societyId);
        if (isMemberOfSociety(dto.getAuthorizedUserId(), societyId)) {
            ServiceException e = new ServiceException("User is member of society");
            log.warn(e.getMessage(), e);
            throw e;
        }
        addUserToSociety(user, society);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAllBySocietyId(Long id) {
        societyMemberRepository.deleteAllByIdSocietyId(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByUserIdAndSocietyId(Long userId, Long societyId) {
        societyMemberRepository.deleteByIdUserIdAndIdSocietyId(userId, societyId);
    }

}
