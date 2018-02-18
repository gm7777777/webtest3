package com.gm.webtest.common.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Request {

    private String requestMethod;

    private String requestPath;

    public Request(String requestMethod , String requesPath){
        this.requestMethod =requestMethod;
        this.requestPath = requesPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj){
        return EqualsBuilder.reflectionEquals(this,obj);
    }
}
