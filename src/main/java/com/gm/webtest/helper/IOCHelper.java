package com.gm.webtest.helper;

import com.gm.webtest.annotation.Inject;
import com.gm.webtest.util.ReflectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

public final class IOCHelper {
    static{
        Map<Class<?>,Object> beanMap = ContextBeanHelper.getCtxBeanMap();
        if(MapUtils.isNotEmpty(beanMap)){
            for(Map.Entry<Class<?>,Object>beanEntry :beanMap.entrySet()){
                Class<?> clazz = beanEntry.getKey();
                Object instance = beanEntry.getValue();
                Field[] fields = clazz.getDeclaredFields();
                if(ArrayUtils.isNotEmpty(fields)){
                    for(Field fd : fields){
                        if(fd.isAnnotationPresent(Inject.class)){
                            Class<?> fieldclazz = fd.getType();
                            Object fdinstance = beanMap.get(fieldclazz);
                            if(fdinstance!=null){
                                ReflectionUtil.setFiled(instance,fd,fdinstance);
                            }
                        }
                    }
                }

            }
        }
    }
}
