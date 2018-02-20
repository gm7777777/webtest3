package com.gm.webtest.proxy.normal;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxy implements MethodInterceptor {


    public static class CGLibHolder{
        private final static CGLibProxy INSTANCE = new CGLibProxy();
    }

    public CGLibProxy getInstance(){
        return CGLibHolder.INSTANCE;
    }

    public <T> T getProxy(Class<T> cls){
        return (T) Enhancer.create(cls,this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        //TODO before
        Object result = methodProxy.invokeSuper(o,objects);
        //TODO after

        return result;
    }
}
