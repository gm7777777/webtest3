package com.gm.webtest.util;

import com.gm.webtest.common.SoapConstant;
import com.gm.webtest.helper.ConfigHelper;

public final class SoapConfigUtil {

    public static boolean isLog(){
        return PropsUtil.getBoolen(ConfigHelper.LOCAL_PROPS, SoapConstant.LOG);
    }
}
