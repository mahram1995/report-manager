package com.mislbd.report_manager.configuration.aopConfig.service;

import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public interface CommandProcessor {
    public Object executeCommand(Object command);

}
