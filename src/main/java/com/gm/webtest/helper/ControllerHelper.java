package com.gm.webtest.helper;

import com.gm.webtest.annotation.Action;
import com.gm.webtest.common.bean.Handler;
import com.gm.webtest.common.bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ControllerHelper {

    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request,Handler>();

    static{
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtils.isNotEmpty(controllerClassSet)){
            for(Class<?> controllerClazz : controllerClassSet){
                Method[] methods = controllerClazz.getDeclaredMethods();
                if(ArrayUtils.isNotEmpty(methods)){
                    for(Method method : methods){
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if(ArrayUtils.isNotEmpty(array)&&array.length==2){
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(controllerClazz,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }

                }
            }
        }

    public static Handler getHandler(String requestMethod,String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
