package com.revature.springcloud.product_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect  // tells Spring: this class contains cross-cutting logic
@Component  // registers it as a springbean
@Slf4j
public class LoggingAspect {
    /**
     * POINTCUT: match any method in any class inside the service package
     *
     */
    @Pointcut("execution(* com.revature.springcloud.product_service.service.*.*(..))")
    public void serviceLayer(){}
    //This method is empty - it's just a named pointcut declaration
    //We reference it by name in the advice annotation below

    /**
     * @AROUND: wraps the ENTIRE method execution
     * ProceeingJoinPoint gives us control in the advice annotations below
     */
    @Around("serviceLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("-> Entering {}.{}() args={} ",className,methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        try {
            /*
            This is where the real method actually runs
            Without this call, the target method never executes

             */
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;
            log.info("<- COMPLETED {}.{}() duration={}ms result{} ",className, methodName, duration,result);
            return result;
        }catch (Exception e){
            long duration = System.currentTimeMillis() - startTime;
            log.error("X Exception {}.{}() duration={}ms error={}", className, methodName, duration,e.getMessage());
            throw e;
        }
    }
}
