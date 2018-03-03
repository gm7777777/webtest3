package com.gm.webtest.helper;

import com.gm.webtest.util.SoapConfigUtil;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;

import java.util.ArrayList;
import java.util.List;

public final class SoapHelper {

    private static final List<Interceptor<? extends Message>> inInterceptorList =
            new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList =
            new ArrayList<Interceptor<? extends Message>>();

    static{

        if(SoapConfigUtil.isLog()){
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }


    public static void publicService(String wsdl ,Class<?> interfaceClass,
                                     Object implementInstance){
        ServerFactoryBean factory = new ServerFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setServiceBean(implementInstance);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
    }

    public static <T> T createClient(String wsdl,Class<? extends T>interfaceClass){
        ClientProxyFactoryBean factoryBean = new ClientProxyFactoryBean();
        factoryBean.setAddress(wsdl);
        factoryBean.setServiceClass(interfaceClass);
        factoryBean.setInInterceptors(inInterceptorList);
        factoryBean.setOutInterceptors(outInterceptorList);
        return factoryBean.create(interfaceClass);
    }
}
