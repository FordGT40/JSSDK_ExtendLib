package com.synjones.m1card;

import com.synjones.cardutil.util;

/**
 * Created by zhaodianbo on 2017-2-23.
 */
public class M1CardInfo {
    public byte[] cardNo;//
    public byte reservedByte;//
    public byte cardType;//0x08=s50,0x18=s70
    public byte[] atq;
    public M1CardInfo(byte[] cardRet){
        if(cardRet!=null && cardRet.length>=8){
            cardNo=new byte[4];
            System.arraycopy(cardRet,0,cardNo,0,4);
            reservedByte=cardRet[4];
            cardType=cardRet[5];
            atq=new byte[2];
            System.arraycopy(cardRet,6,atq,0,2);
        }
    }

    public String getCardType(){
        if(cardType==(byte)0x08)
            return "S50";
        if(cardType==(byte)0x18)
            return "S70";
        return "未知";
    }

    public String getCardNoStr(){
        if(cardNo==null) return "";
        String ret= util.bytesToHexStringNo0x(cardNo,4);
        //ret=ret.substring(0,ret.length()-1);
        return ret;
    }

    public String getAtqStr(){
        if(atq==null) return "";
        String ret= util.bytesToHexStringNo0x(atq,2);
        ret=ret.substring(0,ret.length()-1);
        return ret;
    }

    @Override
    public String toString() {
        return "卡号："+getCardNoStr()+"，卡类型："+getCardType()+"，ATQ："+getAtqStr();
    }
}
