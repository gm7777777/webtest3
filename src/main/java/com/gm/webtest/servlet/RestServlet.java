package com.gm.webtest.servlet;

import com.gm.webtest.annotation.Rest;
import com.gm.webtest.common.RestConstant;
import com.gm.webtest.helper.ClassHelper;
import com.gm.webtest.helper.RestHelper;
import com.gm.webtest.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

@WebServlet(urlPatterns = RestConstant.SERVLET_URL,loadOnStartup = 0)
public class RestServlet extends CXFNonSpringServlet{
    @Override
    protected void loadBus(ServletConfig sc){
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        publishRestService();
    }

    private void publishRestService(){
        Set<Class<?>> restClassSet = ClassHelper.getClassSetByAnnotation(Rest.class);

        if(CollectionUtils.isNotEmpty(restClassSet)){
            for(Class<?> restClass : restClassSet){
                String address = getAddress(restClass);
                RestHelper.publicService(address,restClass);
            }
        }
    }

    private String getAddress(Class<?> restClass){
        String address;
        String value =restClass.getAnnotation(Rest.class).vlaue();
        if(StringUtils.isNotEmpty(value)){
            address = value;
        }else{
            address = restClass.getSimpleName();
        }

        if(!address.startsWith("/")){
            address = "/"+address;
        }
        address = address.replaceAll("\\/+","/");
        return address;
    }
}
