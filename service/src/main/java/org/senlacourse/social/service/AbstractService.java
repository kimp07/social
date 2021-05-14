package org.senlacourse.social.service;

import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;

@Log4j
public abstract class AbstractService<T> {

    abstract T findEntityById(Long id) throws ObjectNotFoundException;

    protected <E> E validateEntityNotNull(E entity, String exceptionMessage) throws ObjectNotFoundException {
        if (entity == null) {
            ObjectNotFoundException e = new ObjectNotFoundException(exceptionMessage);
            log.error(e.getMessage(), e);
            throw e;
        } else {
            return entity;
        }
    }
}
