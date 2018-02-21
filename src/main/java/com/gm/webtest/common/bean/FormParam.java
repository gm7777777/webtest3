package com.gm.webtest.common.bean;

public class FormParam {
    private String fileName;
    private Object fieldValue;

    public FormParam(String fieldName, Object fieldValue) {
        this.fileName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
