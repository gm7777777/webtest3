package com.gm.webtest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public final class StreamUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    public static String getString(InputStream is){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader((is)));
        String line ;
        try {
            while((line = reader.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get String failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void copySteram(InputStream inputStream ,OutputStream outputStream){
        try{
            int length;
            byte[] buffer = new byte[4*1024];
            while((length=inputStream.read(buffer,0,buffer.length))!=-1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
        }catch(Exception e){
            LOGGER.error("copy stream failure",e);
            throw new RuntimeException(e);
        }finally{
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("close stream failure",e);
            }
        }
    }
}
