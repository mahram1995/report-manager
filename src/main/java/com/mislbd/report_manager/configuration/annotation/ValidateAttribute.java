package com.mislbd.report_manager.configuration.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAttribute {
    String operation(); // e.g. "CREATE_NEW_USER"
}