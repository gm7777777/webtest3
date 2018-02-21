package com.gm.webtest.common.bean;

import com.gm.webtest.util.StringUtil;
import com.gm.webtest.util.TypeTransUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParam {

    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public RequestParam(List<FormParam> formParamList){
        this.formParamList = formParamList;

    }
    public RequestParam(List<FormParam> formParamList,List<FileParam> fileParamList){
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        if(CollectionUtils.isNotEmpty(formParamList)){
            for(FormParam formParam : formParamList){
                String fieldName = formParam.getFileName();
                Object fieldValue = formParam.getFieldValue();
                if(fieldMap.containsKey(fieldName)){
                    fieldValue = fieldMap.get(fieldName)+ StringUtil.SEPARATOR+fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<String,List<FileParam>>();
        if(CollectionUtils.isNotEmpty(fileParamList)){
            for(FileParam fileParam: fileParamList){
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;
                if(fileMap.containsKey(fieldName)){
                    fileParamList = fileMap.get(fieldName);
                }else{
                    fileParamList = new ArrayList<FileParam>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName,fileParamList);
            }
        }
        return fileMap;
    }

    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fileName){
        List<FileParam> fileParamList = getFileList(fileName);
        if(CollectionUtils.isNotEmpty(fileParamList)&&fileParamList.size()==1){
            return fileParamList.get(0);
        }
        return null;
    }
//    优化
//    private Map<String , Object> paramMap;
//
//    public RequestParam(Map<String , Object> paramMap){
//        this.paramMap = paramMap;
//    }
//
//    public Map<String, Object> getParamMap() {
//        return paramMap;
//    }
//
//    public void setParamMap(Map<String, Object> paramMap) {
//        this.paramMap = paramMap;
//    }
//
//    public long getLong(String name){
//        return TypeTransUtil.castLong(paramMap.get(name));
//    }

    public boolean isEmpty() {
//        if(this.paramMap==null){
////            return true;
////        }
////        return this.paramMap.isEmpty();
//    }
        //只判断formparam
       return CollectionUtils.isEmpty(formParamList);
    }

    public String getString(String name){
        return TypeTransUtil.castString(getFieldMap().get(name));
    }
    public double getDouble(String name){
        return TypeTransUtil.castDouble(getFieldMap().get(name));
    }
    public long getLong(String name){
        return TypeTransUtil.castLong(getFieldMap().get(name));
    }
    public int getInt(String name){
        return TypeTransUtil.castInt(getFieldMap().get(name));
    }
    public boolean getBoolean(String name){
        return TypeTransUtil.castBoolean(getFieldMap().get(name));
    }
}
