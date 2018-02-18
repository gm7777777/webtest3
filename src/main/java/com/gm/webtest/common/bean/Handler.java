package com.gm.webtest.common.bean;

import java.lang.reflect.Method;

public class Handler {

    private Class<?> controllerClazz;

    private Method actionMethod;

    public Handler(Class<?> controllerClazz , Method actionMethod){
        this.controllerClazz = controllerClazz;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClazz() {
        return controllerClazz;
    }

    public void setControllerClazz(Class<?> controllerClazz) {
        this.controllerClazz = controllerClazz;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}
