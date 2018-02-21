package com.gm.webtest.helper;

import com.gm.webtest.common.bean.FormParam;
import com.gm.webtest.common.bean.RequestParam;
import com.gm.webtest.util.CodecUtil;
import com.gm.webtest.util.StreamUtil;
import com.gm.webtest.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class RequestHelper {

    public static RequestParam createParam(HttpServletRequest request)throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));

        return new RequestParam(formParamList);
    }

    private static List<FormParam> parseParameterNames(HttpServletRequest request){
        List<FormParam> formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if(ArrayUtils.isNotEmpty(fieldValues)){
                Object fieldValue = null;
                if(fieldValues.length == 1){
                    fieldValue = fieldValues[0];
                }else{
                    StringBuilder sb = new StringBuilder("");
                    for(int i=0 ; i<fieldValues.length;i++){
                        sb.append(fieldValues[i]);
                        if(i!= fieldValues.length-1){
                            sb.append(StringUtil.SEPARATOR);
                        }

                    }
                    fieldValue = sb.toString();
                 }
                formParamList.add(new FormParam(fieldName,fieldValue));

            }
        }
        return formParamList;
    }

    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if(StringUtils.isNotEmpty(body)){
            String[] kvs = StringUtils.split(body,"&");
            if(ArrayUtils.isNotEmpty(kvs)){
                for(String kv : kvs){
                    String[] array = StringUtils.split(kv,"=");
                    if(ArrayUtils.isNotEmpty(array)&&array.length ==2){
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParamList.add(new FormParam(fieldName,fieldValue));
                    }
                }
            }
        }
        return formParamList;
    }
}
