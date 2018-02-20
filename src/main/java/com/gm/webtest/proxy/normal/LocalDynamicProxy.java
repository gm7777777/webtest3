package com.gm.webtest.proxy.normal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LocalDynamicProxy implements InvocationHandler {

    private Object proxyTarget;
    public LocalDynamicProxy(Object proxyTarget){
        this.proxyTarget = proxyTarget;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //TODO before
        Object result = method.invoke(proxyTarget,args);
        //TODO after

        return result;
    }

    public <T> T getProxyInstance(){
        return (T) Proxy.newProxyInstance(proxyTarget.getClass().getClassLoader(),
                proxyTarget.getClass().getInterfaces(),
                this);
    }
}
