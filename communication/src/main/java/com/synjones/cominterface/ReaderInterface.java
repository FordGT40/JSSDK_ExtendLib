package com.synjones.cominterface;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.synjones.cominterface.DataTransChannel;

/**
 * 读卡器通用接口，读卡器和主程序之间使用handler通信
 *
 * @author zhaodianbo@Synjones
 * @ClassName ReaderInterface
 * @date 2015-7-16 下午3:41:21
 */
public interface ReaderInterface {
    /**
     * 初始化读卡器
     *
     * @param act
     * @Title setContextAndHandler
     */
    void setActivityAndHandler(Activity act, Handler mHandler);

    void setContext(Context context);

    /**
     * 释放读卡器
     *
     * @Title release
     */
    void release();

    /**
     * 打开读卡器，请求对应的硬件资源，并开启读卡线程
     *
     * @Title open
     */
    boolean open();

    /**
     * 关闭读卡线程，释放资源
     *
     * @Title close
     */
    void close();

    boolean isOpen();

    DataTransChannel getDataTransChannel();

    String getDevName();

    String getSamvID();

}
