package com.gm.webtest.aspect;

import com.gm.webtest.annotation.Aspect;
import com.gm.webtest.annotation.Controller;
import com.gm.webtest.proxy.aop.AspectProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.info("----------proxybegin----------");
        LOGGER.info(String.format(" class: %s ", cls.getName()));
        LOGGER.info(String.format(" method: %s ", method.getName()));
        begin = System.currentTimeMillis();

    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
        LOGGER.info(String.format(" time : %dms",System.currentTimeMillis() - begin));
        LOGGER.info("------------end------------");
    }
}