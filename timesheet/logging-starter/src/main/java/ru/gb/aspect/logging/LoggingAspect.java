package ru.gb.aspect.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j // Slf4j - Simple logging facade for java
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    private final LoggingProperties properties;

    @Pointcut("@annotation(ru.gb.aspect.logging.Logging)") // method
    public void loggingMethodsPointcut() {
    }

    @Pointcut("@within(ru.gb.aspect.logging.Logging)") // class
    public void loggingTypePointcut() {
    }

    @Around(value = "loggingMethodsPointcut() || loggingTypePointcut()")
    public Object loggingMethod(ProceedingJoinPoint pjp) throws Throwable {
        CodeSignature codeSignature = (CodeSignature) pjp.getSignature();
        String ParametrType = codeSignature.getParameterTypes()[0].toString();

        List<String> lstParam = new ArrayList<>();
        lstParam.add("Before");
        lstParam.add(codeSignature.getDeclaringTypeName());
        lstParam.add(pjp.getSignature().getName());
        lstParam.add(ParametrType.substring((ParametrType.lastIndexOf(".") + 1)));

        Optional<Object> args = Optional.ofNullable(pjp.getArgs()[0]);
        //String ParametrArgs = pjp.getArgs()[0].toString();
        if (properties.isPrintArgs())
            argsConditions(lstParam, args);
        else {
            log.atLevel(properties.getLevel()).log("{} -> {}#{}", lstParam.get(0), lstParam.get(1), lstParam.get(2));
        }
        try {
            return pjp.proceed();
        } finally {
            if (properties.isPrintArgs()) {
                lstParam.set(0, "After");
                argsConditions(lstParam, args);
            } else
                log.atLevel(properties.getLevel()).log("{} -> {}#{}", lstParam.get(0), lstParam.get(1), lstParam.get(2));
        }
    }

    private final void argsConditions(List<String> lstParam, Optional<Object> obj) {
        if (obj.isPresent()) {
            log.atLevel(properties.getLevel()).log("{} -> {}#{} ({}={})",
                    lstParam.get(0),
                    lstParam.get(1),
                    lstParam.get(2),
                    lstParam.get(3),
                    obj.get());
        } else {
            log.atLevel(properties.getLevel()).log("{} -> {}#{}()",
                    lstParam.get(0),
                    lstParam.get(1),
                    lstParam.get(2));
        }
    }
}