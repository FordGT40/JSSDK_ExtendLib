package com.synjones.cominterface.serial;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import com.synjones.cominterface.DataTransChannel;
import com.synjones.cominterface.ReaderInterface;

import java.io.File;

import android_serialport_api.SerialPort;

/**
 * 使用了手持机718D中所带的SerialPort jar和so库
 */

/**
 * Created by zhaodianbo on 2016-4-12.
 */
public class SerialInterface  implements ReaderInterface {
    DataTransChannel dataTransChannel;
    SerialPort serialPort;
    Activity activity;
    Context context;
    boolean isOpen=false;
    String serialName;
    String serialBaudrate;
    public static final String[] Baudrate=new String[]{"9600","38400","57600","115200"};
    public SerialInterface (String serialName,String serialBaudrate){
        this.serialName=serialName;
        this.serialBaudrate=serialBaudrate;
        dataTransChannel=new DataTransChannel();
    }
    @Override
    public void setActivityAndHandler(Activity act, Handler mHandler) {
        this.activity=act;
        this.context=activity.getApplicationContext();
    }

    @Override
    public void setContext(Context context) {
        this.context=context.getApplicationContext();
    }

    @Override
    public void release() {
        close();
        serialPort=null;
    }

    @Override
    public boolean open() {
        if(serialPort!=null)
            serialPort.close();
        int baudrate=-1;

        try{
            if(serialBaudrate==null||serialBaudrate.isEmpty()) return false;
            baudrate=Integer.decode(serialBaudrate);
        }catch (Exception e){
            return false;
        }


        /* Check parameters */
        if ( (serialName == null) ||serialName.isEmpty()|| (baudrate == -1)) {
            return false;
        }


        /* Open the serial port */
        try {
            serialPort = new SerialPort(new File(serialName), baudrate, 400);
            dataTransChannel.setInOutStream(serialPort.getInputStream(),serialPort.getOutputStream());
            dataTransChannel.open();
            isOpen=dataTransChannel.canReadSamv();;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isOpen;
    }


    @Override
    public void close() {
       if(serialPort!=null) {
           try {
               serialPort.close();
           }catch (Exception e){}
       }
        isOpen = false;
        if(dataTransChannel!=null){
            try{
                dataTransChannel.close();
            }catch (Exception e){}
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public DataTransChannel getDataTransChannel() {
        return dataTransChannel;
    }

    @Override
    public String getDevName() {
        return serialName;
    }

    @Override
    public String getSamvID() {
        return null;
    }
}
