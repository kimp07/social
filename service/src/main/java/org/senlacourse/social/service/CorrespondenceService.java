package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.service.ICorrespondenceService;
import org.senlacourse.social.dto.CorrespondenceDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.mapstruct.CorrespondenceDtoMapper;
import org.senlacourse.social.projection.IUnreadTalkMessagesView;
import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdCount;
import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdDto;
import org.senlacourse.social.repository.CorrespondenceRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j
public class CorrespondenceService implements ICorrespondenceService {

    private final CorrespondenceRepository correspondenceRepository;
    private final CorrespondenceDtoMapper correspondenceDtoMapper;

    @AuthorizedUser
    @Override
    public Page<CorrespondenceDto> findAllByUserIdAndTalkId(UserIdDto dto, Long talkId, Pageable pageable) {
        return correspondenceDtoMapper.map(
                correspondenceRepository
                        .findAllByIdUserIdAndIdTalkMessageTalkIdOrderByIdTalkMessageId(dto.getAuthorizedUserId(),
                                talkId, pageable));
    }

    @AuthorizedUser
    @Override
    public Page<UnreadTalkMessagesGroupByTalkIdCount> getCountUnreadMessagesByUserIdGroupByTalkId(UserIdDto dto,
                                                                                                  Pageable pageable) {
        return correspondenceRepository
                .findCountUnreadMessagesByUserIdGroupByTalkId(dto.getAuthorizedUserId(), pageable);
    }

    @AuthorizedUser
    @Override
    public Page<UnreadTalkMessagesGroupByTalkIdDto> getCountUnreadMessagesByUserIdGroupByTalkIdCriteria(UserIdDto dto,
                                                                                                        Pageable pageable) {
        return correspondenceRepository
                .findCountUnreadMessagesByUserIdGroupByTalkIdCriteria(dto.getAuthorizedUserId(), pageable);
    }

    @Override
    public IUnreadTalkMessagesView getCountUnreadMessagesByUserId(UserIdDto dto) {
        return correspondenceRepository.findCountUnreadMessagesByUserId(dto.getAuthorizedUserId());
    }

    @AuthorizedUser
    @Override
    public void updateMessagesSetUnreadFalseByRecipientId(UserIdDto dto) {
        correspondenceRepository.updateAllSetUnreadFalseByUserId(dto.getAuthorizedUserId());
    }

    @AuthorizedUser
    @Override
    public void updateMessagesSetUnreadFalseByRecipientIdAndTalkId(UserIdDto dto, Long talkId) {
        correspondenceRepository.updateAllSetUnreadFalseByUserIdAndTalkId(dto.getAuthorizedUserId(), talkId);
    }
}
