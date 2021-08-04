package com.synjones.m1card;

import com.synjones.multireaderlib.MultiReader;

public class ZkM1Reader implements M1ReaderInterface {

    MultiReader reader;
    KeyType keyType;
    byte[]key=new byte[6];

    public ZkM1Reader(MultiReader reader){
        this.reader=reader;
    }

    @Override
    public M1CardInfo findM1Card() {
        try{
            byte mode = (byte)0x26;//寻卡模式 0x52一次可以操作多张卡，0x26一次只对一张卡操作
            byte halt = (byte)0x00;//0x00不需要执行halt命令，0x01读写器执行halt命令
            ZkReaderSendData zkReaderSendData=new ZkReaderSendData();
            zkReaderSendData.free();
            zkReaderSendData.setCmd((byte)37);
            byte[] data = new byte[]{mode, halt};
            zkReaderSendData.setData(data);
            byte send[]=new byte[data.length+6];
            zkReaderSendData.Packet(send);
            byte recv[]=reader.getIop().sendAndRecvZkData(send);
            if(recv==null) return null;
            byte cardNum[]=new byte[8];
            System.arraycopy(recv, 1, cardNum, 0, 4);
            return new M1CardInfo(cardNum);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean verifyKey(KeyType type, int blockNo, byte[] key) {
        try{
            this.keyType=type;
            System.arraycopy(key,0,this.key,0,6);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public byte[] readBlock(int blockNo) {
        try{
            byte mode = (byte)0x01;//写操作模式0或1
            if(this.keyType== KeyType.KeyB)
                mode=0x11;
            byte blockCount = (byte)0x01;//要读多少块1-4
            byte startAddress = (byte)blockNo;//16进制0x00-0x3F 即0到63块

            ZkReaderSendData zkReaderSendData=new ZkReaderSendData();
            zkReaderSendData.free();
            zkReaderSendData.setCmd((byte)32);
            byte[] data = new byte[9];
            data[0] = mode;
            data[1] = blockCount;
            data[2] = startAddress;
            System.arraycopy(key, 0, data, 3, 6);
            zkReaderSendData.setData(data);
            byte send[]=new byte[data.length+6];
            zkReaderSendData.Packet(send);
            byte recv[]=reader.getIop().sendAndRecvZkData(send);
            if(recv==null) return null;
            byte[]cardNum=new byte[4];
            System.arraycopy(recv, 0, cardNum, 0, 4);
            byte ret[]=new byte[16];
            System.arraycopy(recv, 4, ret, 0, 16);
            return ret;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean writeBlock(int blockNo, byte[] writeData) {
        try{
            byte mode = (byte)0x01;//写操作模式0或1
            if(this.keyType== KeyType.KeyB)
                mode=0x11;
            byte blockCount = (byte)0x01;//要读多少块1-4
            byte startAddress = (byte)blockNo;//16进制0x00-0x3F 即0到63块

            ZkReaderSendData zkReaderSendData=new ZkReaderSendData();
            zkReaderSendData.free();
            zkReaderSendData.setCmd((byte)33);
            byte[] data = new byte[9+16];
            data[0] = mode;
            data[1] = blockCount;
            data[2] = startAddress;
            System.arraycopy(key, 0, data, 3, 6);
            System.arraycopy(writeData, 0, data, 9, 16);
            zkReaderSendData.setData(data);
            byte send[]=new byte[data.length+6];
            zkReaderSendData.Packet(send);
            byte recv[]=reader.getIop().sendAndRecvZkData(send);
            if(recv==null) return false;
            byte[]cardNum=new byte[4];
            System.arraycopy(recv, 0, cardNum, 0, 4);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean pend() {
        return true;
    }


    public String getVersion(){
        try{
            ZkReaderSendData zkReaderSendData=new ZkReaderSendData();
            zkReaderSendData.free();
            zkReaderSendData.setCmd((byte)-122);
            byte send[]=new byte[6];
            zkReaderSendData.Packet(send);
            byte recv[]=reader.getIop().sendAndRecvZkData(send);
            if(recv==null) return null;
            return new String(recv);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
