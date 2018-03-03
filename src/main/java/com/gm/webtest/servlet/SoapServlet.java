package com.gm.webtest.servlet;

import com.gm.webtest.annotation.Soap;
import com.gm.webtest.common.SoapConstant;
import com.gm.webtest.helper.ClassHelper;
import com.gm.webtest.helper.ContextBeanHelper;
import com.gm.webtest.helper.SoapHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

@WebServlet(urlPatterns= SoapConstant.SERVLET_URL,loadOnStartup = 0)
public class SoapServlet extends CXFNonSpringServlet{

    @Override
    protected void loadBus(ServletConfig sc){
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        publishSoapService();
    }

    private void publishSoapService(){
        Set<Class<?>> soapClassSet = ClassHelper.getClassSetByAnnotation(Soap.class);
        if(CollectionUtils.isNotEmpty(soapClassSet)){
            for(Class<?> soapClass:soapClassSet){
                String address = getAddress(soapClass);
                Class<?> soapInterfaceClazz = getSoapInterfaceClazz(soapClass);
                Object soapInstance = ContextBeanHelper.getBean(soapClass);

                SoapHelper.publicService(address,soapInterfaceClazz,soapInstance);
            }
        }
    }

    private Class<?> getSoapInterfaceClazz(Class<?> soapClazz){
        return soapClazz.getInterfaces()[0];
    }

    private String getAddress(Class<?> soapClass){
        String address;
        String soapValue = soapClass.getAnnotation(Soap.class).value();
        if(StringUtils.isNotEmpty(soapValue)){
            address = soapValue;
        }else{
            address = getSoapInterfaceClazz(soapClass).getSimpleName();
        }

        if(!address.startsWith("/")){
            address="/"+address;
        }
        address = address.replaceAll("\\/+","/");
        return address;
    }
}
