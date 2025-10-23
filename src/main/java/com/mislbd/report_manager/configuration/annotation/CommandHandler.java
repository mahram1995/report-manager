package com.mislbd.report_manager.configuration.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface CommandHandler {
    String name() default "";
}