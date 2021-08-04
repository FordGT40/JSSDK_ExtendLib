package com.synjones.m1card;

public class ZkReaderSendData {
    private byte stx = -86;
    private byte stationid = 0;
    private byte len = 0;
    private byte cmd = 0;
    private byte[] data = null;
    private byte bcc = 0;
    private byte etx = -69;

    public ZkReaderSendData() {
    }

    public void free() {
        this.stx = -86;
        this.stationid = 0;
        this.len = 0;
        this.data = null;
        this.cmd = 0;
        this.bcc = 0;
        this.etx = -69;
    }

    public void setStationid(byte stationid) {
        this.stationid = stationid;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public void setData(byte[] data) {
        if (null != data && data.length > 0) {
            this.data = new byte[data.length];
            System.arraycopy(data, 0, this.data, 0, data.length);
        }

    }

    public int getPacketLen() {
        int ret = 0;
        if (null != this.data && this.data.length > 0) {
            ret += this.data.length;
        }
        return ret;
    }

    public boolean Packet(byte[] buffer) {
        if (null != buffer && buffer.length >= this.getPacketLen()) {
            this.len = 0;
            if (null != this.data && this.data.length > 0) {
                this.len = (byte)(this.len + this.data.length);
            }

            ++this.len;
            this.bcc = this.stationid;
            this.bcc ^= this.len;
            this.bcc ^= this.cmd;
            int nPos;
            if (null != this.data && this.data.length > 0) {
                for(nPos = 0; nPos < this.data.length; ++nPos) {
                    this.bcc ^= this.data[nPos];
                }
            }

            nPos = 0;
            buffer[nPos++] = this.stx;
            buffer[nPos++] = this.stationid;
            buffer[nPos++] = this.len;
            buffer[nPos++] = this.cmd;
            if (null != this.data && this.data.length > 0) {
                System.arraycopy(this.data, 0, buffer, nPos, this.data.length);
                nPos += this.data.length;
            }

            buffer[nPos++] = this.bcc;
            buffer[nPos++] = this.etx;
            return true;
        } else {
            return false;
        }
    }

}
