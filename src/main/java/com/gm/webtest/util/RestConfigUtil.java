package com.gm.webtest.util;

import com.gm.webtest.common.RestConstant;
import com.gm.webtest.helper.ConfigHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public final class RestConfigUtil {

    public static boolean isLog(){
        return PropsUtil.getBoolen(ConfigHelper.LOCAL_PROPS, RestConstant.LOG);
    }

    public static boolean isJsonp(){
        return PropsUtil.getBoolen(ConfigHelper.LOCAL_PROPS, RestConstant.JSONP);
    }

    public static String getJsonpFunction(){
        return PropsUtil.getString(ConfigHelper.LOCAL_PROPS, RestConstant.JSONP_FUNCTION);
    }

    public static boolean isCors(){
        return PropsUtil.getBoolen(ConfigHelper.LOCAL_PROPS, RestConstant.CORS);
    }

    public static List<String> getCorsOriginList(){
        String corsOrign = PropsUtil.getString(ConfigHelper.LOCAL_PROPS, RestConstant.CORS_ORIGIN);
        return Arrays.asList(StringUtils.split(corsOrign,","));
    }
}
