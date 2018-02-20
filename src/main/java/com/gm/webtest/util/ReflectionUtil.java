package com.gm.webtest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
//            instance = cls.newInstance(); 原先的方法不能抛出异常
            instance = cls.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(" new instance class failure",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(" new instance class illegal param failure",e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(" new instance class no constructor method failure",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            LOGGER.error(" new instance class none target failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static Object invokeMethod(Object obj , Method method, Object ... args){
        Object result;
        try {

            method.setAccessible(true);
            result = method.invoke(obj,args);
        } catch (IllegalAccessException e) {
           LOGGER.error("invoke method failure",e);
           throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            LOGGER.error("invoke method failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void setField(Object obj, Field field, Object value){

        try {
            field.setAccessible(true);
            field.set(obj,value);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failure ",e);
            throw new RuntimeException(e);
        }
    }
}
