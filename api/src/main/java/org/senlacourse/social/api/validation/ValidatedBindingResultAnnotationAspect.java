package org.senlacourse.social.api.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.BadRequestBodyException;
import org.senlacourse.social.api.util.ValidationErrorMessagesUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j
public class ValidatedBindingResultAnnotationAspect {

    @Pointcut("@annotation(org.senlacourse.social.api.validation.ValidatedBindingResult) && args(.., bindingResult)")
    public void callValidationBindingResult(BindingResult bindingResult) {
        // empty
    }

    @Around(value = "callValidationBindingResult(bindingResult)", argNames = "pjp,bindingResult")
    public Object aroundCallAt(ProceedingJoinPoint pjp, BindingResult bindingResult) throws BadRequestBodyException {
        if (bindingResult.hasErrors()) {
            throw new BadRequestBodyException(ValidationErrorMessagesUtil.getErrorsMessage(bindingResult));
        }
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e);
        }
    }
}
