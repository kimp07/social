package org.senlacourse.social.service;

import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.service.IService;
import org.senlacourse.social.domain.AbstractEntity;

@Log4j
public abstract class AbstractService<T extends AbstractEntity> implements IService<T> {

    public static final String OBJECT_NOT_FOUND_FOR_ID = "Object not found for id=";
    public static final String OBJECT_NOT_FOUND_FOR_NAME = "Object not found for name=";

    protected <E extends AbstractEntity> E validateEntityNotNull(E entity) throws ObjectNotFoundException {
        if (entity == null) {
            ObjectNotFoundException e = new ObjectNotFoundException("Object not found");
            log.error(e.getMessage(), e);
            throw e;
        } else {
            return entity;
        }
    }
}
