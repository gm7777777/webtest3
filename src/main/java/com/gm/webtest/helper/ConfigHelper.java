package com.gm.webtest.helper;

import com.gm.webtest.common.ConfigConstant;
import com.gm.webtest.util.PropsUtil;

import java.util.Properties;

public final class ConfigHelper {

    public static final Properties LOCAL_PROPS
            = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    public static String getBasePackage(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.BASE_PACKAGE);
    }

    public static String getJspPath(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.JSP_PATH);
    }

    public static String getAssetPath(){
        return PropsUtil.getString(LOCAL_PROPS,ConfigConstant.ASSET_PATH);
    }

    public static int getUploadLimit(){
        return PropsUtil.getInt(LOCAL_PROPS,ConfigConstant.UPLOAD_LIMIT,10);
    }

}
