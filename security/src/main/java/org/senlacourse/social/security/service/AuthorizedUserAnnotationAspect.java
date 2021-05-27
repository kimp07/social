package org.senlacourse.social.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.dto.IAuthorizedUserDto;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j
public class AuthorizedUserAnnotationAspect {

    private final IAuthorizedUserService authorizedUserService;

    @Pointcut("@annotation(org.senlacourse.social.security.service.AuthorizedUser) && args(dto,..)")
    public void callAtAuthorizedUserWithDtoArg(IAuthorizedUserDto dto) {
        // empty
    }

    @Pointcut("@annotation(org.senlacourse.social.security.service.AuthorizedUser) && args(userId,..)")
    public void callAtAuthorizedUserWithLongArg(Long userId) {
        // empty
    }

    @Around(value = "callAtAuthorizedUserWithDtoArg(dto)", argNames = "pjp,dto")
    public Object aroundCallAt(ProceedingJoinPoint pjp, IAuthorizedUserDto dto) {
        authorizedUserService.injectAuthorizedUserId(dto);
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    @Around(value = "callAtAuthorizedUserWithLongArg(userId)", argNames = "pjp,userId")
    public Object aroundCallAt(ProceedingJoinPoint pjp, Long userId) {
        authorizedUserService.injectAuthorizedUserId(userId);
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e);
        }
    }

}
