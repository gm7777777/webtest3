package com.gm.webtest.helper;

import com.gm.webtest.common.bean.FileParam;
import com.gm.webtest.common.bean.FormParam;
import com.gm.webtest.common.bean.RequestParam;
import com.gm.webtest.util.FileUtil;
import com.gm.webtest.util.StreamUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UploadFileHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileHelper.class);

    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext){
        File repository = (File) servletContext
                .getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
        int uploadLimit = ConfigHelper.getUploadLimit();
                if(uploadLimit!=0){
                    servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
                }
    }

    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    public static RequestParam createParam(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();

        try {
            Map<String,List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if(MapUtils.isNotEmpty(fileItemListMap)){
                for(Map.Entry<String,List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if(CollectionUtils.isNotEmpty(fileItemList)){
                        for(FileItem item:fileItemList){
                            if(item.isFormField()){
                                String fieldValue = item.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));

                            }else{
                                String filename = FileUtil.getRealFileName(new String(
                                        item.getName().getBytes(),"UTF-8"
                                ));
                                if(StringUtils.isNotEmpty(filename)){
                                    long fileSize = item.getSize();
                                    String contentType = item.getContentType();
                                    InputStream inputStream = item.getInputStream();
                                    fileParamList.add(new FileParam(fieldName,filename,fileSize,contentType,inputStream));
                                }
                            }
                            }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create param failure",e);
            throw new RuntimeException(e);
        }
        return new RequestParam(formParamList,fileParamList);
    }

    public static void uploadFile(String basePath,FileParam fileParam){
        try {
        if(fileParam!=null){
            String filePath = basePath + fileParam.getFieldName();
            FileUtil.createFile(filePath);
            InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            StreamUtil.copySteram(inputStream,outputStream);
        }
        } catch (Exception e) {
            LOGGER.error("upload file failure",e);
            throw new RuntimeException(e);
        }
    }

    public static void uploadFile(String basePath,List<FileParam> fileParamList){

        if(CollectionUtils.isNotEmpty(fileParamList)){
            for(FileParam fileParam: fileParamList){
                uploadFile(basePath,fileParam);
            }
        }
    }
}
