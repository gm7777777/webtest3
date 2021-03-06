package com.gm.webtest.common;

import com.gm.webtest.helper.ClassHelper;
import com.gm.webtest.helper.ContextBeanHelper;
import com.gm.webtest.helper.IOCHelper;
import com.gm.webtest.util.ClassUtil;

public final class AppHelperLoader {

    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,
                ContextBeanHelper.class,
                //添加aophelper但是要在IOC之前这样ioc才能获取到
                IOCHelper.class,
                ContextBeanHelper.class
        };
        for(Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName());
        }
    }
}
