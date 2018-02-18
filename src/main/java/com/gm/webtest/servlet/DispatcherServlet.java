package com.gm.webtest.servlet;

import com.gm.webtest.common.AppHelperLoader;
import com.gm.webtest.common.bean.Data;
import com.gm.webtest.common.bean.Handler;
import com.gm.webtest.common.bean.RequestParam;
import com.gm.webtest.common.bean.View;
import com.gm.webtest.helper.ConfigHelper;
import com.gm.webtest.helper.ContextBeanHelper;
import com.gm.webtest.helper.ControllerHelper;
import com.gm.webtest.util.CodecUtil;
import com.gm.webtest.util.JsonUtil;
import com.gm.webtest.util.ReflectionUtil;
import com.gm.webtest.util.StreamUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{

    @Override
    public void init(ServletConfig servletConfig) throws ServletException{
        AppHelperLoader.init();

        ServletContext servletContext = servletConfig.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getJspPath()+"*");

        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAssetPath()+"*");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,IOException{
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler !=null){
            Class<?> controllerClazz = handler.getControllerClazz();
            Object  controllerBean = ContextBeanHelper.getBean(controllerClazz);
            Map<String,Object> paramMap = new HashMap<String,Object>();
            Enumeration<String> paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if(StringUtils.isNotEmpty(body)){
                String[] params = StringUtils.split(body,"&");
                if(ArrayUtils.isNotEmpty(params)){
                    for(String param:params){
                        String[] array = StringUtils.split(param,"=");
                        if(ArrayUtils.isNotEmpty(array)&&array.length==2){
                            String paraName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paraName,paramValue);
                        }
                    }
                }
            }

            RequestParam param = new RequestParam(paramMap);
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            if(result instanceof View){
                View view = (View) result;
                String path = view.getPath();
                if(StringUtils.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        response.sendRedirect(request.getContextPath()+path);
                    }else{
                        Map<String,Object> model = view.getModel();
                        for(Map.Entry<String,Object>entry : model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                        request.getRequestDispatcher(ConfigHelper.getJspPath()+path).forward(request,response);
                    }
                }
            }else if (result instanceof Data){
                Data data = (Data) result;
                Object model =data.getModel();
                if(model != null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }


        }
    }
}
