package com.tm.upwork.domain.job.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JobLock {
    /**
     * SpEL expression to resolve the jobId (e.g., "#jobId")
     */
    String key();
}
