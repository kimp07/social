package org.senlacourse.social.domain.projection;


public interface IUnreadTalkMessagesGroupByTalkIdCountView {

    Long getTalkId();
    Integer getTalkMessagesCount();
}
