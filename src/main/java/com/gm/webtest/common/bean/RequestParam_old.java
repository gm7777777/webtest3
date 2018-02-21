package com.gm.webtest.common.bean;

import com.gm.webtest.util.TypeTransUtil;

import java.util.Map;

public class RequestParam_old {

    private Map<String , Object> paramMap;

    public RequestParam_old(Map<String , Object> paramMap){
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public long getLong(String name){
        return TypeTransUtil.castLong(paramMap.get(name));
    }

    public boolean isEmpty(){
        if(this.paramMap==null){
            return true;
        }
        return this.paramMap.isEmpty();
    }
}
