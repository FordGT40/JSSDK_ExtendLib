package com.wisdom.jsextendlib.Handler;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
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
        IDCardUtil.initReadCard(context);
        IDCardUtil.startReadThread();
        IDCardUtil idCardUtil = new IDCardUtil();
        idCardUtil.setOnChangeListener(new IDCardUtil.GetCardListener() {
            @Override
            public void succeed(IDCard idcard) {
                BaseModel model = new BaseModel("成功",0,idcard);
                function.onCallBack(GsonUtils.toJson(model));
            }

            @Override
            public void error(String string) {
                BaseModel model = new BaseModel("成功",1,string);
                function.onCallBack(GsonUtils.toJson(model));
            }

        });

    }

}
