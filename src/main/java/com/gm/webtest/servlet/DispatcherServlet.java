package com.gm.webtest.servlet;

import com.gm.webtest.common.AppHelperLoader;
import com.gm.webtest.common.bean.Data;
import com.gm.webtest.common.bean.Handler;
import com.gm.webtest.common.bean.RequestParam;
import com.gm.webtest.common.bean.View;
import com.gm.webtest.helper.*;
import com.gm.webtest.util.JsonUtil;
import com.gm.webtest.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        AppHelperLoader.init();

        ServletContext servletContext = servletConfig.getServletContext();

        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getJspPath() + "*");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAssetPath() + "*");
        //添加上传文件初始化
        UploadFileHelper.init(servletContext);


    }

//    优化前
//    @Override
//    public void service(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException,IOException{
//        String requestMethod = request.getMethod().toLowerCase();
//        String requestPath = request.getPathInfo();
//        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
//        if(handler !=null){
//            Class<?> controllerClazz = handler.getControllerClazz();
//            Object  controllerBean = ContextBeanHelper.getBean(controllerClazz);
//            Map<String,Object> paramMap = new HashMap<String,Object>();
//            Enumeration<String> paramNames = request.getParameterNames();
//            while(paramNames.hasMoreElements()){
//                String paramName = paramNames.nextElement();
//                String paramValue = request.getParameter(paramName);
//                paramMap.put(paramName,paramValue);
//            }
//            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
//            if(StringUtils.isNotEmpty(body)){
//                String[] params = StringUtils.split(body,"&");
//                if(ArrayUtils.isNotEmpty(params)){
//                    for(String param:params){
//                        String[] array = StringUtils.split(param,"=");
//                        if(ArrayUtils.isNotEmpty(array)&&array.length==2){
//                            String paraName = array[0];
//                            String paramValue = array[1];
//                            paramMap.put(paraName,paramValue);
//                        }
//                    }
//                }
//            }
//
//            RequestParam param = new RequestParam(paramMap);
//            Method actionMethod = handler.getActionMethod();
//            Object result = null;
//            //优化传入参数
//            if(param.isEmpty()){
//                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
//            }else{
//                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
//            }
//            if(result instanceof View){
//                View view = (View) result;
//                String path = view.getPath();
//                if(StringUtils.isNotEmpty(path)){
//                    if(path.startsWith("/")){
//                        response.sendRedirect(request.getContextPath()+path);
//                    }else{
//                        Map<String,Object> model = view.getModel();
//                        for(Map.Entry<String,Object>entry : model.entrySet()){
//                            request.setAttribute(entry.getKey(),entry.getValue());
//                        }
//                        request.getRequestDispatcher(ConfigHelper.getJspPath()+path).forward(request,response);
//                    }
//                }
//            }else if (result instanceof Data){
//                Data data = (Data) result;
//                Object model =data.getModel();
//                if(model != null){
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter writer = response.getWriter();
//                    String json = JsonUtil.toJson(model);
//                    writer.write(json);
//                    writer.flush();
//                    writer.close();
//                }
//            }
//        }
//    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        ServletHelper.init(request,response);
        try {
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();
            if(requestPath.equals("/favicon.ico")){
                return;
            }
            Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);

            if(handler !=null) {
                Class<?> controllerClazz = handler.getControllerClazz();
                Object objBean = ContextBeanHelper.getBean(controllerClazz);
                RequestParam param = null;
                //判断是否文件请求类型
                try {
                    if (UploadFileHelper.isMultipart(request)) {
                        param = UploadFileHelper.createParam(request);
                    } else {
                        param = RequestHelper.createParam(request);
                    }
                } catch (IOException e) {
                    LOGGER.error("create Action Param failure", e);
                }

                Object result;
                Method actionMethod = handler.getActionMethod();
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(objBean,actionMethod);
                }else{
                    result = ReflectionUtil.invokeMethod(objBean,actionMethod,param);
                }

                if(result instanceof View){
                    handleViewResult((View) result,request,response);
                }else if(result instanceof  Data){
                    handleDataResult((Data) result,response);
                }
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    private void handleViewResult(View view,HttpServletRequest request,HttpServletResponse response)
    throws IOException,ServletException{
        String path = view.getPath();
        if(StringUtils.isNotEmpty(path)){
            if(path.startsWith("/")){
                response.sendRedirect(request.getContextPath()+path);
            }else{
                Map<String,Object> model = view.getModel();
                for(Map.Entry<String,Object> entry : model.entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getJspPath()+path).forward(request,response);
            }
        }
    }

    private void handleDataResult(Data data , HttpServletResponse response)throws IOException{
        Object model = data.getModel();
        if(model!=null){
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
