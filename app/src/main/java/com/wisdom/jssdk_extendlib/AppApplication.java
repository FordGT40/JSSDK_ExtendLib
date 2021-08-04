package com.wisdom.jssdk_extendlib;

import android.app.Application;

import com.wisdom.jsextendlib.utils.InitJsExtendSDKUtil;

/**
 * @author HanXueFeng
 * @ProjectName project： JSSDK_ExtendLib
 * @class package：com.wisdom.jssdk_extendlib
 * @class describe：
 * @time 2021/8/4 0004 17:27
 * @change
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitJsExtendSDKUtil.init(this);
    }
}
