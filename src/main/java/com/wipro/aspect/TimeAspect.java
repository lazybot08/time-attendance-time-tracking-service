package com.wipro.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class TimeAspect {

    @Pointcut("execution(* com.wipro.controller.TimeRestController.*(..))")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        
        if (log.isDebugEnabled()) {
            log.debug("Entering: {} with arguments = {}", methodName, Arrays.toString(joinPoint.getArgs()));
        }
        
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exiting: {} with result = {}", methodName, result);
            }
            return result;
        } catch (Exception e) {
            log.error("Error in {}: {}", methodName, e.getMessage());
            throw e;
        }
    }
}

