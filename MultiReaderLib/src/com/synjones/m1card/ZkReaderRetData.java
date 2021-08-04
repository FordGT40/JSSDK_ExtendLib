package com.synjones.m1card;

public class ZkReaderRetData {
    private byte stx = -86;
    private byte stationid = 0;
    private byte len = 0;
    private byte status = -1;
    private byte[] data = null;
    private byte bcc = 0;
    private byte etx = -69;
    public static final int MIN_PACKET_LEN = 6;

    public ZkReaderRetData() {
    }

    public void free() {
        this.stx = -86;
        this.stationid = 0;
        this.len = 0;
        this.data = null;
        this.status = -1;
        this.bcc = 0;
        this.etx = -69;
    }

    public byte getStatus() {
        return this.status;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getDataLen() {
        return this.len & 255;
    }

    public int PreGetLen(byte[] buffer) {
        return null != buffer && buffer.length >= 6 && -86 == buffer[0] ? (buffer[2] & 255) + 5 : 0;
    }

    public boolean UnPacket(byte[] buffer) {
        if (null != buffer && buffer.length >= 6) {
            int nTotalLen = this.PreGetLen(buffer);
            if (nTotalLen > 0 && nTotalLen <= buffer.length && buffer[nTotalLen - 1] == -69) {
                int nPos = 0;
                this.stx = buffer[nPos++];
                this.stationid = buffer[nPos++];
                this.len = buffer[nPos++];
                this.status = buffer[nPos++];
                if ((this.len & 255) > 1) {
                    this.data = new byte[(this.len & 255) - 1];
                    System.arraycopy(buffer, nPos, this.data, 0, (this.len & 255) - 1);
                    nPos += (this.len & 255) - 1;
                }

                this.bcc = buffer[nPos++];
                this.etx = buffer[nPos++];
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public byte getChecksum() {
        return this.bcc;
    }

    public byte calcCheckSum() {
        byte cs = 0;
        cs = (byte)(cs ^ this.stationid);
        cs ^= this.len;
        cs ^= this.status;
        if (null != this.data && this.data.length > 0) {
            for(int i = 0; i < this.data.length; ++i) {
                cs ^= this.data[i];
            }
        }

        return cs;
    }
}
