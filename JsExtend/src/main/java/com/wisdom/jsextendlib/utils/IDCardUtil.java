package com.wisdom.jsextendlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.synjones.idcard.IDCard;
import com.synjones.idcard.IDCardReaderRetInfo;
import com.synjones.reader.IDCardReaderModule;

/**
 * @author $
 * @ProjectName project：
 * @class package：$
 * @class describe：$
 * @time $
 * @change
 */
public class IDCardUtil {
    private static IDCard idCard;
   // static TextView tvReaderStatus;
   // Button btnRead;
    public static IDCardReaderModule idCardReaderModule;
    private static volatile boolean reading=false;
    private static final int ReadOnceDone=0x01;
    private static final int UpdateStatus=0x02;
    private static final int ReadThreadDone=0x09;
    private  static ReadCardThread ReadCardThreadhandler;
    // 状态变化监听
   public interface GetCardListener {
        // 回调方法
        void succeed(IDCard idcard);
        void error(String string);
    }
    // 持有一个接口对象
   static GetCardListener getCardListener;

    public  void initReadCard(Context context){
        idCardReaderModule=IDCardReaderModule.getInstance(context);
    }

    // 提供注册事件监听的方法
    public void setOnChangeListener(GetCardListener getCardListener) {
        this.getCardListener = getCardListener;
    }

    private static Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ReadOnceDone:
                    idCard = (IDCard) msg.obj;
                   // Log.i("TAG", "photo:------- "+idCard.getPhoto());
                    getCardListener.succeed(idCard);
                    CloseReader();
                    break;
                case UpdateStatus:
                    String stutes = (String)msg.obj;
                    if(stutes.contains("读卡器打开失败")){
                        getCardListener.error("读卡器打开失败");
                    }
//                    tvReaderStatus.setText((String) msg.obj);
                    break;
                case ReadThreadDone:
                   // btnRead.setText("读卡");
                    break;
            }
            super.handleMessage(msg);
        }
    };;


    /**
     * 关闭读卡器
     * @Title CloseReader
     */
    private static void CloseReader(){
        stopRead();
        if (idCardReaderModule!=null) {
            idCardReaderModule.close();
        }
    }

    public  synchronized void startReadThread(){
        reading=true;
        ReadCardThreadhandler=new ReadCardThread();
        ReadCardThreadhandler.start();
    }

    static synchronized void stopRead(){
        //停止读卡线程
        if(ReadCardThreadhandler!=null){
            ReadCardThreadhandler.StopRead();
            try {
                ReadCardThreadhandler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ReadCardThreadhandler=null;
        }
    }


    void initViews(){
//
//        btnRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (reading) {
//                    stopRead();
//                } else {
//                    btnRead.setText("停止");
//                    startReadThread();
//                }
//            }
//        });



    }

    private static void OpenReader(){
        if(!idCardReaderModule.isOpen())
            idCardReaderModule.open();
    }


    //读卡线程
    static class ReadCardThread extends Thread{
        public void run() {
            OpenReader();
            if(!idCardReaderModule.isOpen()){
                reading=false;
                idCardReaderModule.close();
                Log.e("ReadCardThread","读卡器打开失败");
                mHandler.obtainMessage(UpdateStatus,"读卡器打开失败").sendToTarget();
            }
            int count=0;
            int success=0;

            while(reading)
            {
                try{
                    IDCardReaderRetInfo retInfo;
                    if(true)//只读身份证文字和照片
                        retInfo = idCardReaderModule.getIDcardInfo();
                    else//读身份证文字，照片和指纹
                        retInfo = idCardReaderModule.getIDcardInfo(false,true,true);
                    count++;
                    //samvID=idCardReaderModule.getSamvIDString();//获取公安部二代证模块编号
                    //String appendAddress=idCardReaderModule.getAppendAddress();//获取身份证内的追加地址信息
                    if(retInfo.errCode==IDCardReaderRetInfo.ERROR_OK)
                    {
                        success++;
                        mHandler.obtainMessage(ReadOnceDone,retInfo.card).sendToTarget();
                        //mHandler.obtainMessage(UpdateStatus,String.format("读卡总数=%s,成功=%s",count,success)).sendToTarget();
                        Thread.sleep(500);
                    }
                    else if(retInfo.errCode==IDCardReaderRetInfo.ERROR_RECV_FINDCARD){
                        Log.e("ReadCardThread","通信错误,重新打开读卡器");
                        idCardReaderModule.close();
                        Thread.sleep(3000);
                        idCardReaderModule.open();
                        if(!idCardReaderModule.isOpen()){
                            reading=false;
                            idCardReaderModule.close();
                            Log.e("ReadCardThread","通信错误,重新读卡器打开失败");
                            mHandler.obtainMessage(UpdateStatus,"通信错误,重新读卡器打开失败").sendToTarget();
                        }
                    }
                    else{
                        String status= util.bytesToHexString(new byte[]{retInfo.sw1,retInfo.sw2,retInfo.sw3});
                       // if()
                       // mHandler.obtainMessage(UpdateStatus,String.format("读卡总数=%s,成功=%s,状态=%s",count,success,status)).sendToTarget();
                        Thread.sleep(200);
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }//while
            mHandler.obtainMessage(ReadThreadDone).sendToTarget();
        }//run

        public void StopRead(){
            reading=false;
            interrupted();
        }

    }
}
