package com.wisdom.jsextendlib.Handler;

import static android.content.Context.USB_SERVICE;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.synjones.idcard.IDCard;
import com.wisdom.jsextendlib.Model.BaseModel;
import com.wisdom.jsextendlib.utils.IDCardUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author $
 * @ProjectName project：
 * @class package：$
 * @class describe：$
 * @time $
 * @change
 */
public class GetIDCardHandler  extends BridgeHandler {
    IDCardUtil idCardUtil;
    public GetIDCardHandler(Context context) {

        boolean isUsbOk = checkUsbConnect(context, 1024, 50010);
        if(!isUsbOk){
            isUsbOk = checkUsbConnect(context, 790, 20512);
        }

        idCardUtil = new IDCardUtil();
        idCardUtil.initReadCard(context);
    }

    //检测USB权限，true授权成功，false未授权
    private boolean checkUsbConnect(Context context, int vid, int pid) {
        UsbManager usbManager = (UsbManager) context.getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        UsbDevice usbDevice = null;
        for (Map.Entry<String, UsbDevice> entry : usbDeviceList.entrySet()) {
            UsbDevice dev = entry.getValue();
            if (dev.getProductId() == pid && dev.getVendorId() == vid) {
                usbDevice = dev;
            }
        }
        if (usbDevice == null) return false;
        if (!usbManager.hasPermission(usbDevice)) {
            // 外置监听
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            new Intent("com.eseid.hidusbmanager.USB_PERMISSION"),
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
            usbManager.requestPermission(usbDevice, pendingIntent);
            return false;
        }
        return true;
    }

    @Override
    public void handler(Context context, String data, final CallBackFunction function) {
        boolean isUsbOk = checkUsbConnect(context, 1024, 50010);
        if(!isUsbOk){
            isUsbOk = checkUsbConnect(context, 790, 20512);
        }
        IDCardUtil.stopRead();
        idCardUtil.startReadThread();
        idCardUtil.setOnChangeListener(new IDCardUtil.GetCardListener() {
            @Override
            public void succeed(IDCard idcard) {
                BaseModel model = new BaseModel("成功",0,idcard);
                LogUtils.i("handler:onSucceed");
                function.onCallBack(GsonUtils.toJson(model));
            }

            @Override
            public void error(String string) {
                BaseModel model;
                if("e2".equals(string)||"e1".equals(string)){
                    //读卡器打开失败
                    model= new BaseModel(string,1,string);
//                    LogUtils.i("handler:onError");
                }else{
                    model= new BaseModel(string,3,string);
                }
                function.onCallBack(GsonUtils.toJson(model));
            }

            @Override
            public void fail(String string) {
                BaseModel model = new BaseModel(string,2,string);
//                LogUtils.i("handler:onFail");
                function.onCallBack(GsonUtils.toJson(model));
            }

        });
    }


}
