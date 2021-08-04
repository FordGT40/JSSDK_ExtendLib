package com.synjones.reader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.synjones.cominterface.ReaderInterface;
import com.synjones.cominterface.ReaderInterfaceFactory;
import com.synjones.idcard.IDCardReaderRetInfo;
import com.synjones.idcard.IDcardReader;
import com.synjones.m1card.M1CardInfo;
import com.synjones.m1card.M1CardReader;
import com.synjones.m1card.M1ReaderInterface;
import com.synjones.multireaderlib.MultiReader;


/**
 * 二代证模块操作封装
* @ClassName IdCardReaderModule 
* @author zhaodianbo@Synjones
* @date 2015-7-22 下午3:56:44 
*
 */
public class IDCardReaderModule {
	private static volatile IDCardReaderModule mInstance;
	/**
	 * 二代证命令封装
	 */
	MultiReader multiReader=MultiReader.getReader();
	/**
	 * 二代证读卡器对象
	 */
	IDcardReader idCardReader;
	Context mContext;
	ReaderInterface readerInterface;
	boolean isOpen=false;
	M1ReaderInterface m1ReaderInterface=null;

	
	private IDCardReaderModule(Context context){
		mContext=context;
		//readerInterface=ReaderInterfaceFactory.create();
		// readerDriver.setMaxRFByte((byte) 0x50);
	}

	public static IDCardReaderModule getInstance(Context context) {
		if (mInstance == null) {
			synchronized (IDCardReaderModule.class) {
				if (mInstance == null) {
					mInstance = new IDCardReaderModule(context);
				}
			}
		}
		return mInstance;
	}


	public synchronized void resetSerialReader(String serialName,String serialBaudrate){
		if(readerInterface!=null)
			readerInterface.release();
		if(!TextUtils.isEmpty(serialName))
			ReaderInterfaceFactory.setSerialName(serialName);
		if(!TextUtils.isEmpty(serialBaudrate))
			ReaderInterfaceFactory.setSerialBaudrate(serialBaudrate);
		ReaderInterfaceFactory.setWhichReader(ReaderInterfaceFactory.READER_SERIAL);
		readerInterface=null;
	}

	public synchronized void resetUSBReader(){
		if(readerInterface!=null)
			readerInterface.release();
		ReaderInterfaceFactory.setWhichReader(ReaderInterfaceFactory.READER_USB);
		readerInterface=null;
	}

	public synchronized void open(){
		if(readerInterface==null){
			readerInterface=ReaderInterfaceFactory.create();
		}
		readerInterface.setContext(mContext);
		isOpen=readerInterface.open();

		if(isOpen){
			multiReader.setDataTransInterface(readerInterface.getDataTransChannel());
			idCardReader=new IDcardReader(multiReader);
			idCardReader.open(mContext);
		}else {
			Log.e("IDCardReaderModule", "open reader failed");
		}

	}

	public synchronized boolean isOpen(){
		if(readerInterface==null) return false;
		return readerInterface.isOpen();
	}
	
	
	/** 结束读卡  关闭读卡器
	* @Title: CloseSerialPort    
	* @return void 
	*/
	public synchronized  void close(){
		try {
			idCardReader.close();
		} catch (Exception e) {}

		isOpen = false;
		if(readerInterface!=null)
			readerInterface.close();
		m1ReaderInterface=null;
	}
	
	
	/**  获取身份证信息，此函数将阻塞直到返回身份证或读卡超时
	* @Title: getIDcardBlocking   
	* @return IDCard 
	*/
	public synchronized IDCardReaderRetInfo getIDcardBlockingNoFp(boolean readOnce, boolean needDecodePhoto){
		return idCardReader.getIDcardBlockingNoFpRetInfo(readOnce, needDecodePhoto);
	}
	
	/**
	 * 获取身份证信息
	* @Title getIDcardInfo 
	* @param sameCardReadOnce 设置为true时，卡不移开，读同一张卡时，只有第一次返回信息，后续都返回寻卡错误。
	* @param needPhoto 是否需要照片信息
	* @param needFp 是否需要指纹信息
	* @return
	 */
	public synchronized IDCardReaderRetInfo getIDcardInfo(boolean sameCardReadOnce, boolean needPhoto, boolean needFp){
		return idCardReader.getIDCardInfo(sameCardReadOnce, needPhoto, needFp);
	}
	/**
	 * 获取身份证信息
	* @Title getIDcardInfo 
	* @return
	 */
	public synchronized IDCardReaderRetInfo getIDcardInfo(){
		return idCardReader.getIDCardInfo(false, true, false);
	}
	
	/**
	 * 获取身份证安全模块编号
	* @Title getSamvIDString 
	* @return
	 */
	public synchronized String getSamvIDString(){
		return idCardReader.getSamvIDStr();
	}
	/**
	 * 获取身份证中追加地址信息
	* @Title getAppendInfo 
	* @return
	 */
	public synchronized String getAppendAddress(){
		return idCardReader.readAppendAddress();
	}

	public synchronized byte[] MF_GET_SNR(){

		M1CardInfo m1CardInfo=getDevice().findM1Card();
		if(m1CardInfo==null) return null;
		return m1CardInfo.cardNo;
	}

	private M1ReaderInterface getDevice(){
		if(m1ReaderInterface!=null) return m1ReaderInterface;
	/*	ZkM1Reader zkM1Reader=new ZkM1Reader(multiReader);
		if(zkM1Reader.getVersion()!=null)
			m1ReaderInterface=zkM1Reader;
		else*/
			m1ReaderInterface=new M1CardReader(multiReader);

		return m1ReaderInterface;
	}

	public synchronized M1CardInfo findM1Card(){
		return getDevice().findM1Card();
	}

	public synchronized boolean writeBlock(byte blockNo, byte[] data) {
		return getDevice().writeBlock(blockNo,data);
	}

	public synchronized boolean pend() {
		return getDevice().pend();
	}

	public synchronized boolean verifyKey(M1ReaderInterface.KeyType keytype, byte block, byte[] key) {
		return getDevice().verifyKey(keytype,block,key);
	}

	public synchronized byte[] readBlock(byte block) {
		return getDevice().readBlock(block);
	}

	/**
	 * 获取二代证读卡器对象
	 * @Title getIDcardReader
	 * @return
	 */
	public IDcardReader getIDcardReader(){
		return idCardReader;
	}

	public MultiReader getBaseMultiReader() {
		return multiReader;
	}




}
