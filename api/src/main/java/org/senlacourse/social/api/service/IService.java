package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.domain.AbstractEntity;

public interface IService<T extends AbstractEntity> {

    T findEntityById(Long id) throws ObjectNotFoundException;
}
