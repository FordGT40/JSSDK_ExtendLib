package com.wisdom.jsextendlib.utils;

import android.app.Application;

import com.smallbuer.jsbridge.core.Bridge;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.wisdom.jsextendlib.Handler.GetIDCardHandler;
import com.wisdom.jsextendlib.JsInterfaceBridge;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HanXueFeng
 * @ProjectName project： JsbridjeTest
 * @class package：com.wisdom.jsinterfacelib.utils
 * @class describe：
 * @time 2021/7/17 0017 12:31
 * @change
 */
public class InitJsExtendSDKUtil {

 /**
  * 初始化jssdk方法，在主项目的Application中进行调用
  * @param application
  */
   public static void init(Application application){
       Bridge.INSTANCE.openLog();
        Map<String, BridgeHandler> bridgeHandlerMap = new HashMap();
        bridgeHandlerMap.put("WISDOM.app.readIDCard", new GetIDCardHandler());

        JsInterfaceBridge.init(application, bridgeHandlerMap);
   }
}
