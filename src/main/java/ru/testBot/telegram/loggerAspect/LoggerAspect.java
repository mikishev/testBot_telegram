package ru.testBot.telegram.loggerAspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


import java.util.ArrayList;
import java.util.List;

/**
 * LoggerAspect
 */

@Slf4j
@Controller
@Aspect
@Component
public class LoggerAspect {


    /**
     * pointcut
     */
    @Pointcut("execution(* ru.testBot.telegram.*.*.*(..))")
    public void pointcut() {
    }

    /**
     * beforeMethod
     *
     * @param joinPoint
     */
    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<String> args = getArgs(joinPoint);
        log.info("Method={} args={} started", methodName, args);
    }

    /**
     * afterMethod
     *
     * @param joinPoint
     */
    @AfterReturning("pointcut()")
    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<String> args = getArgs(joinPoint);
        log.info("Method={} args={} finished", methodName, args);
    }

    /**
     * afterException
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        List<String> args = getArgs(joinPoint);
        log.warn("Warn!!! Failed to method={} args={} error={}", methodName, args, ex);
    }

    /**
     * getArgs
     *
     * @param joinPoint
     * @return
     */
    private List<String> getArgs(JoinPoint joinPoint) {
        List<String> args = new ArrayList<>();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            Object argValue = joinPoint.getArgs()[i];
            args.add("arg." + i + "= " + argValue);
        }
        return args;
    }
}
