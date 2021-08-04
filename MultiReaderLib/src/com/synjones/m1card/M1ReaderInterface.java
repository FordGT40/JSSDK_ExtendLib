package com.synjones.m1card;

public interface M1ReaderInterface {
    enum KeyType{KeyA,KeyB};

    /**
     * 寻找M1卡
     * @Title findM1Card
     * @return 4字节卡序列号+ 1字节（保留）+1字节卡类型+2字节ATQ字节
     * 寻卡失败返回null
     */
    M1CardInfo findM1Card();

    /**
     * 验证密钥
     * @Title verifyKey
     * @param type 密钥类型 KeyType.KeyA | KeyType.KeyB
     * @param blockNo 验证块号
     * @param key 6字节密钥
     * @return true 成功
     */
    boolean verifyKey(KeyType type, int blockNo, byte key[]);

    /**
     * 读块值
     * @Title readBlock
     * @param blockNo 块号
     * @return 16字节数据
     */
    byte[] readBlock(int blockNo);

    /**
     * 写块
     * @Title writeBlock
     * @param blockNo 块号
     * @param writeData 16字节块数据
     * @return true 成功
     */
    boolean writeBlock(int blockNo, byte writeData[]);

    /**
     * 挂起M1卡
     * @Title pend
     * @return ture 成功
     */
    public boolean pend();
}
