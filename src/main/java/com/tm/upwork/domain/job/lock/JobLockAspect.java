package com.tm.upwork.domain.job.lock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
@Slf4j
public class JobLockAspect {

    private final Map<Integer, ReentrantLock> jobLocks = new ConcurrentHashMap<>();
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(jobLock)")
    public Object lock(ProceedingJoinPoint joinPoint, JobLock jobLock) throws Throwable {
        Integer jobId = resolveKey(joinPoint, jobLock.key());
        ReentrantLock lock = jobLocks.computeIfAbsent(jobId, k -> new ReentrantLock());

        if (!lock.tryLock()) {
            throw new IllegalStateException("A chat operation is already in progress for job ID: " + jobId);
        }

        try {
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    private Integer resolveKey(ProceedingJoinPoint joinPoint, String expression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        return parser.parseExpression(expression).getValue(context, Integer.class);
    }
}
