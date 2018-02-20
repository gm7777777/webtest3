package com.gm.webtest.helper;

import com.gm.webtest.annotation.Aspect;
import com.gm.webtest.annotation.Service;
import com.gm.webtest.proxy.aop.AspectProxy;
import com.gm.webtest.proxy.aop.Proxy;
import com.gm.webtest.proxy.aop.ProxyManager;
import com.gm.webtest.proxy.aop.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 切面方法
 */
public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static{
            try {
                Map<Class<?>,List<Proxy>> aopMap = createAopMap(createProxyMap());
                for(Map.Entry<Class<?>,List<Proxy>> aopEntry : aopMap.entrySet()){
                    Class<?> aopClazz = aopEntry.getKey();
                    List<Proxy> proxyList = aopEntry.getValue();
                    Object proxy = ProxyManager.createProxy(aopClazz,proxyList);
                    ContextBeanHelper.addBean(aopClazz,proxy);
                }
            } catch (Exception e) {
                LOGGER.info(" AOP MAP LOAD FAILURE",e);
            }


    }

    private static Set<Class<?>> getAspectClassSet(Aspect asp) throws Exception{
        Class<? extends Annotation> annotation = asp.value();
        if(annotation !=null && !annotation.equals(Aspect.class)){
            return ClassHelper.getClassSetByAnnotation(annotation);
        }
        return null;
    }

    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>,Set<Class<?>>>();

//        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuperClaszz(AspectProxy.class);
//        for(Class<?> proxyClass :proxyClassSet){
//            if(proxyClass.isAnnotationPresent(Aspect.class)){
//                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
//                Set<Class<?>> tempClassSet = getAspectClassSet(aspect);
//                proxyMap.put(proxyClass,tempClassSet);
//            }
//        }

        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    private static void addAspectProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuperClaszz(AspectProxy.class);
        for(Class<?> proxyClass :proxyClassSet){
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> tempClassSet = getAspectClassSet(aspect);
                proxyMap.put(proxyClass,tempClassSet);
            }
        }
    }

    private static void addTransactionProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuperClaszz(Service.class);

                proxyMap.put(TransactionProxy.class,proxyClassSet);

    }


    private static Map<Class<?>,List<Proxy>> createAopMap
            (Map<Class<?>,Set<Class<?>>> proxyMap)throws Exception{
        Map<Class<?>,List<Proxy>> aopMap = new HashMap<Class<?>,List<Proxy>>();
        if(proxyMap!=null){

            for(Map.Entry<Class<?>,Set<Class<?>>> proxyEntry : proxyMap.entrySet()){

                Class<?> proxyClass = proxyEntry.getKey();
                Set<Class<?>> tempClassSet = proxyEntry.getValue();
                for(Class<?> clazz : tempClassSet){
                    Proxy proxy = (Proxy) proxyClass.getDeclaredConstructor().newInstance();
                    if(aopMap.containsKey(clazz)){
                        aopMap.get(clazz).add(proxy);
                    }else{
                        List<Proxy> proxyList = new ArrayList<Proxy>();
                        proxyList.add(proxy);
                        aopMap.put(clazz,proxyList);
                    }
                }
            }

        }
        return aopMap;
    }
}
