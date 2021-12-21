package com.wisdom.jsextendlib.Handler;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.synjones.idcard.IDCard;
import com.wisdom.jsextendlib.Model.BaseModel;
import com.wisdom.jsextendlib.utils.IDCardUtil;

/**
 * @author $
 * @ProjectName project：
 * @class package：$
 * @class describe：$
 * @time $
 * @change
 */
public class GetIDCardHandler  extends BridgeHandler {
    @Override
    public void handler(Context context, String data, final CallBackFunction function) {
        IDCardUtil idCardUtil = new IDCardUtil();
        idCardUtil.initReadCard(context);
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
                    LogUtils.i("handler:onError");
                }else{
                    model= new BaseModel(string,3,string);
                }
                function.onCallBack(GsonUtils.toJson(model));
            }

            @Override
            public void fail(String string) {
                BaseModel model = new BaseModel(string,2,string);
                LogUtils.i("handler:onFail");
                function.onCallBack(GsonUtils.toJson(model));
            }

        });
    }

}
