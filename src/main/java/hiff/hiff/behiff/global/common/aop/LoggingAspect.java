package hiff.hiff.behiff.global.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.yourpackage.YourService.getNewHiffMatching(..))")
    public void matchingMethod() {}

    @Before("matchingMethod()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Method {} is about to start", joinPoint.getSignature().getName());
    }

    @Around(value = "matchingMethod()", argNames = "pjp,joinPoint")
    public Object logAround(ProceedingJoinPoint pjp, JoinPoint joinPoint) throws Throwable {
        log.info("Entering method: {}", joinPoint.getSignature().toShortString());
        Object result = null;
        try {
            result = ((org.aspectj.lang.ProceedingJoinPoint) joinPoint).proceed();
        } catch (Throwable e) {
            log.error("Exception in method: {}", joinPoint.getSignature().toShortString(), e);
            throw e;
        }
        log.info("Exiting method: {}", joinPoint.getSignature().toShortString());
        return result;
    }

    @After("matchingMethod()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Method {} has finished", joinPoint.getSignature().getName());
    }
}
