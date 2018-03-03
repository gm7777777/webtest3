package com.gm.webtest.helper;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.gm.webtest.util.RestConfigUtil;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpInInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPostStreamInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPreStreamInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.omg.PortableInterceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;

public final class RestHelper {

    private static final List<Object> providerList = new ArrayList<Object>();

    private static final List<org.apache.cxf.interceptor.Interceptor<? extends Message>> inInterceptorList =
            new ArrayList<org.apache.cxf.interceptor.Interceptor<? extends Message>>();
    private static final List<org.apache.cxf.interceptor.Interceptor<? extends Message>> outInterceptorList =
            new ArrayList<org.apache.cxf.interceptor.Interceptor<? extends Message>>();

    static{
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        providerList.add(jsonProvider);

        if(RestConfigUtil.isLog()){
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }

        if(RestConfigUtil.isJsonp()){
            JsonpInInterceptor jsonpInInterceptor = new JsonpInInterceptor();
            jsonpInInterceptor.setCallbackParam(RestConfigUtil.getJsonpFunction());
            inInterceptorList.add(jsonpInInterceptor);
            JsonpPreStreamInterceptor jsonpPreStreamInterceptor = new JsonpPreStreamInterceptor();
            outInterceptorList.add(jsonpPreStreamInterceptor);
            JsonpPostStreamInterceptor jsonpPostStreamInterceptor = new JsonpPostStreamInterceptor();
            outInterceptorList.add(jsonpPostStreamInterceptor);
        }

        if(RestConfigUtil.isCors()){
            CrossOriginResourceSharingFilter crossOriginResourceSharingFilter = new CrossOriginResourceSharingFilter();
            crossOriginResourceSharingFilter.setAllowOrigins(RestConfigUtil.getCorsOriginList());
            providerList.add(crossOriginResourceSharingFilter);
        }
    }

    public static void publicService(String wadl ,Class<?> interfaceClass){
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress(wadl);
        factory.setResourceClasses(interfaceClass);
        factory.setResourceProvider(interfaceClass, new SingletonResourceProvider(ContextBeanHelper.getBean(interfaceClass)));
        factory.setProviders(providerList);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
    }

    public static <T> T createClient(String wadl ,Class<? extends T> resourceClass){
        return JAXRSClientFactory.create(wadl,resourceClass,providerList);
    }

}
