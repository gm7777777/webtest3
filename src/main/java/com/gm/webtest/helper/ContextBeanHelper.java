package com.gm.webtest.helper;

import com.gm.webtest.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ContextBeanHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextBeanHelper.class);

    private static final Map<Class<?>,Object> CTX_BEAN_MAP
            = new HashMap<Class<?>,Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for(Class<?> beanClass :beanClassSet){
            Object obj = ReflectionUtil.newInstance(beanClass);
            CTX_BEAN_MAP.put(beanClass,obj);
        }
    }

    public static Map<Class<?>,Object> getCtxBeanMap(){
        return CTX_BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls){
        if(!CTX_BEAN_MAP.containsKey(cls)){
//            找不到不报错
//            throw new RuntimeException("can not get bean from ctx map" + cls);
            LOGGER.error("can not get bean from ctx map" + cls);
            return null;
        }
        return (T) CTX_BEAN_MAP.get(cls);
    }

    public static <T> T addBean(Class<?> cls,T t){
        return (T)CTX_BEAN_MAP.put(cls,t);
    }

}
