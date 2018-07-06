package com.jingyun.wisdom.service;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ByteUtil {

	public static String fnGetDoorState(byte[] frame)
	{
		byte doorState=(byte) (frame[0]&0x01);
		String door="";
		Log.d("UnpackFrame","箱门开关状态："+ String.valueOf(doorState));
		if(doorState==(byte)0x00)
			door="已关闭";
		else if(doorState==(byte)0x01)
			door="已开启";
		return door;
	}
	public static String fnGetLockState(byte[] frame)
	{
		byte lockState=(byte) ((frame[0] >> 1) & 0x01);
		String lock="";
		Log.d("UnpackFrame","锁状态："+ String.valueOf(lockState));
		if(lockState==(byte)0x00)
			lock="已关闭";
		else if(lockState==(byte)0x01)
			lock="已开启";
		return lock;
	}
	public static String fnGeBodyState(byte[] frame)
	{
		byte bodyState=(byte) ((frame[0] >> 2) & 0x01);

		String body="";
		Log.d("UnpackFrame","体感状态："+ String.valueOf(bodyState));
		if(bodyState==(byte)0x00)
			body="无人";
		else if(bodyState==(byte)0x01)
			body="有人";
		return body;
	}
	/**
	 * 文件转换为byte数组
	 * @param filePath 文件路径
	 * */
	public static byte[] File2byte(String filePath)
	{
		byte[] buffer = null;
		try
		{
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return buffer;
	}
	public static String makeChecksum(String data) {
		if (data == null || data.equals("")) {
			return "";
		}
		int total = 0;
		int len = data.length();
		int num = 0;
		while (num < len) {
			String s = data.substring(num, num + 2);
			System.out.println(s);
			total += Integer.parseInt(s, 16);
			num = num + 2;
		}
		/**
		 * 用256求余最大是255，即16进制的FF
		 */
		int mod = total % 256;
		String hex = Integer.toHexString(mod);
		len = hex.length();
		// 如果不够校验位的长度，补0,这里用的是两位校验
		if (len < 2) {
			hex = "0" + hex;
		}
		return hex;
	}
	/**
	 * 将byte数组中的元素倒序排列
	 */
	public static byte[] bytesReverseOrder(byte[] b) {
		int length = b.length;
		byte[] result = new byte[length];
		for(int i=0; i<length; i++) {
			result[length-i-1] = b[i];
		}
		return result;
	}

	/**
	 * 将字符串以前面补“0”的方式修正到指定长度
	 *
	 * @param s   字符串
	 * @param len 指定长度
	 * @return 修正后的字符串
	 */
	public static String fixString2Len(@NonNull String s, int len) {
		final int strLen = s.length();
		if (strLen == len) {
			return s;
		}
		if (strLen > len) {
			return s.substring(strLen - len, strLen);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = strLen; i < len; i++) {
			sb.append("0");
		}
		return sb.append(s).toString();
	}

	/**
	 * 获取文件帧的文件时间
	 */
	public static String fnGetTimeByFileFrame(byte[] frame)
	{
		String time="";
		byte[] data=new byte[6];
		data[0]=frame[21];
		data[1]=frame[22];
		data[2]=frame[23];
		data[3]=frame[24];
		data[4]=frame[25];
		data[5]=frame[26];
		time=ByteUtil.A1BytetoString(data);
		return time;
	}
	/**
	 * 获取FTP文件帧的文件时间
	 */
	public static String fnGetTimeByFileFTPFrame(byte[] frame)
	{
		String time="";
		byte[] data=new byte[6];
		data[0]=frame[61];
		data[1]=frame[62];
		data[2]=frame[63];
		data[3]=frame[64];
		data[4]=frame[65];
		data[5]=frame[66];
		time=ByteUtil.A1BytetoString(data);
		return time;
	}
	/**
	 * bytes字符串转换为Byte值
	 *
	 * @param src
	 *            String Byte字符串，每个Byte之间没有分隔符(字符范围:0-9 A-F)
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		/* 对输入值进行规范化整理 */
		src = src.trim().replace(" ", "").toUpperCase(Locale.US);
		// 处理值初始化
		int m = 0, n = 0;
		int iLen = src.length() / 2; // 计算长度
		byte[] ret = new byte[iLen]; // 分配存储空间

		for (int i = 0; i < iLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
		}
		return ret;
	}
	// int[ ] –>16进制字符串
	public static String int2hex(int[] buffer, int iInLen) {
		String h = "";
		for (int i = 0; i < iInLen; i++) {
			String temp = Integer.toHexString(buffer[i] & 0xFF);
			if (temp.length() == 1) {
				temp = "0" + temp;
			}
			h = h + temp;
		}
		return h;
	}
	// 16进制字符串 –>int [ ]
	public static int[] hexStringToInt(String hex) {
		int len = (hex.length() / 2);
		int[] result = new int[len];
		char[] achar = hex.toCharArray();
		int pos = 0;
		int iH = 0, iL = 0;
		for (int i = 0; i < len; i++) {
			pos = i * 2;
			iH = Character.getNumericValue(achar[pos]);
			iL = Character.getNumericValue(achar[pos + 1]);
			result[i] = (int) (iH * 0x10 + iL);
		}
		return result;
	}
	// 16进制字符串 –>byte[ ]
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	/**
	 * ip地址格式校验
	 * */
	public static boolean isIP(String ip){

		String Ip = ip.replaceAll(" ","");

		if(Ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){

			String[] st = Ip.split("\\.");

			if(Integer.parseInt(st[0])<225)

				if(Integer.parseInt(st[1])<225)

					if(Integer.parseInt(st[2])<225)

						if(Integer.parseInt(st[3])<225)

							return true;
		}
		return false;
	}
    public static byte[] strIPto4Bytes(String ip)
	{
		try{
			Log.e("Terminal","util ip:"+ip);
			int[] i_ip=new int[4];
			byte[] byte_ip=new byte[4];
			String[] str_ip=ip.split("\\.");
			Log.e("Terminal","util str_ip size:"+str_ip.length);
			for(int i=0;i<4;i++)
			{
				i_ip[i]= Integer.valueOf(str_ip[i]);
			}
			for(int i=0;i<4;i++)
			{
				byte_ip[i]=(byte)i_ip[i];
			}
			Log.e("Terminal","ip:"+ByteUtil.byte2HexStr(byte_ip));
			if(byte_ip.length==4)
				return byte_ip;
			else
				return null;
		}catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public static byte[] strPortTo2Bytes(String port)
	{
		try{
			int i_port= Integer.valueOf(port);
			Log.i("ip","i_port:"+i_port);
			byte[] bt=intTo2ByteArray2(i_port);
			Log.i("ip","hex:"+ByteUtil.byte2HexStr(bt));
			return bt;
		}catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

    /**
     * 4个字节转int
     * */
    public static int byteArrayToInt(byte a,byte b,byte c,byte d) {
        int x=0;
        int a1=0,a2=0,a3=0,a4=0;
        a1=a&0xff;
        a2=(b&0xff)*0x100;
        a3=(c&0xff)*0x10000;
        a4=(d&0xff)*0x1000000;
        x= a1+a2+a3+a4;
        return x;
    }
	/**
	 * 2个字节转int
	 * */
	public static int byteArrayToInt(byte a,byte b) {
		int x=0;
		int a1=0,a2=0;
		a1=a&0xff;
		a2=(b&0xff)*0x100;
		x= a1+a2;
		return x;
	}

    public static String BinaryToHexString(byte[] bytes) {
		String hexStr = "0123456789ABCDEF";
		String result = "";
		String hex = "";
		for (byte b : bytes) {
			hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
			hex += String.valueOf(hexStr.charAt(b & 0x0F));
			result += hex + " ";
		}
		return result;
	}
	public static String bytes2hex02(byte[] bytes)
    {  
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes)  
        {  
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制  
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位  
            {  
                tmp = "0" + tmp;  
            }  
            sb.append(tmp);  
        }  
  
        return sb.toString();  
  
    }  
	/**
	 * bytesת����ʮ�������ַ���
	 */
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}
	public static String byte2HexStr(byte b) {
		String hs = "";
		String stmp = "";
			stmp = (Integer.toHexString(b & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";

		return hs.toUpperCase();
	}
	///
	public static byte string2byte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return result;
	}

	/**
	 * byteת2����string
	 */
	public static String byte2string(byte b) {

		int z = b;
		z |= 256;
		String str = Integer.toBinaryString(z);
		int len = str.length();
		return str.substring(len - 8, len);
	}
	/**
	 * 字节数组转为普通字符串（ASCII对应的字符）
	 *
	 * @param bytearray
	 *            byte[]
	 * @return String
	 */
	public static String byte2String(byte[] bytearray) {
		String result = "";
		char temp;

		int length = bytearray.length;
		for (int i = 0; i < length; i++) {
			temp = (char) bytearray[i];
			result += temp;
		}
		return result;
	}
	public static String byte2CommonString(byte[] data){
		return new String(data);
	}

	public static byte[] getBooleanArray(byte b) {
		byte[] array = new byte[8];
		for (int i = 7; i >= 0; i--) {
			array[i] = (byte) (b & 1);
			b = (byte) (b >> 1);
		}
		return array;
	}

	/**
	 * intת2�ֽ�byte[],��λ��ǰ����λ�ں󣬵�λ��β����10
	 * 
	 * @param res
	 * @return
	 */
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[2];

		targets[0] = (byte) ((res << 2) + 2 & 0xff);// ���λ
		targets[1] = (byte) ((res >> 6) & 0xff);// �ε�λ

		return targets;
	}

	/**
	 * ����ת��Ϊ������byte����λתΪ����λ��ʮλתΪ����λ
	 * 
	 * @param res
	 * @return
	 */
	public static byte intToDoublebyte(int res) {
		int i = Integer.valueOf(res);
		String devIdString = Integer.toHexString(i);
		byte devBin = (byte) Integer.parseInt(devIdString, 16);
		return devBin;
	}

	/**
	 * int转4字节数组
	 *
	 * @param integer
	 * @return
	 */
	public static byte[] intTo4ByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}

	/**
	 * 2字节byte转int
	 * 
	 * @param a 低字节
	 * @param b 高字节
	 * @return
	 */
	public static int TwobyteToInt(byte a, byte b) {
		int x = a&0xFF;
		int y = (b&0xFF);
		int big_y=y*256;
		return x + big_y;
	}

	/**
	 * ���ֽ�byte����תint
	 * 
	 * @param b
	 *            ����
	 * @param offset
	 *            �����С
	 * @return
	 */
	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * int转2字节byte,高字节在前，低字节在后
	 * 
	 * @param integer
	 * @return
	 */
	public static byte[] intTo2ByteArrayRevert(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[2];

		for (int n = 0; n < byteNum; n++)
			byteArray[1 - n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}
	/**
	 * int转2字节byte,低字节在前，高字节在后
	 *
	 * @param integer
	 * @return
	 */
	public static byte[] intTo2ByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[2];

		for (int n = 0; n < byteNum; n++)
			byteArray[n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}
	public static byte[] intTo2ByteArray2(int res) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		return targets;
	}
	public static byte[] intTo2ByteArray3(int res) {
		byte[] targets = new byte[2];
		targets[1] = (byte) (res & 0xff);
		targets[0] = (byte) ((res >> 8) & 0xff);
		return targets;
	}
	/**
	 * A25格式，double转3字节byte,默认正数
	 * */
	public static byte[] A25DoubletoBytes(double data)
	{
		byte[] frame=new byte[3];
		//123.456
		DecimalFormat df   = new DecimalFormat("000.000");
		String s_data=df.format(data);
		Log.e("A25","data:"+data+" s_data:"+s_data);
		//千分位
		int pQF= Integer.parseInt(""+s_data.charAt(s_data.length()-1));
		//百分位
		int pBF= Integer.parseInt(""+s_data.charAt(s_data.length()-2));
		//十分位
		int pSF= Integer.parseInt(""+s_data.charAt(s_data.length()-3));
		//个位
		int pG= Integer.parseInt(""+s_data.charAt(s_data.length()-5));
		//十位
		int pS= Integer.parseInt(""+s_data.charAt(s_data.length()-6));
		//百位,不能超过7
		int pB= Integer.parseInt(""+s_data.charAt(s_data.length()-7));
		byte oneL=(byte) ((byte)pBF<<4);

		frame[0]=(byte) (oneL+pQF);//101
		byte twoL=(byte) ((byte)pG<<4);
		frame[1]=(byte) (twoL+pSF);
		byte thrdL=(byte) ((byte) ((byte)(int)pB&0x07)<<4);
		frame[2]=(byte) (thrdL+pS);
		Log.e("A25","千分位："+pQF+" 百分："+pBF+" 十分："+pSF+" 个："+pG+" 十："+pS+"  frame:"+ByteUtil.byte2HexStr(frame));
		return frame;
	}
	/**
	 * A5格式，double转2字节byte,默认正数
	 * */
	public static byte[] A5DoubletoBytes(double data)
	{
		byte[] frame=new byte[2];
		//123.4
		DecimalFormat df   = new DecimalFormat("000.0");
		String s_data=df.format(data);

		//十分位
		int pSF= Integer.parseInt(""+s_data.charAt(s_data.length()-1));
		//个位
		int pG= Integer.parseInt(""+s_data.charAt(s_data.length()-3));
		//十位
		int pS= Integer.parseInt(""+s_data.charAt(s_data.length()-4));
		//百位,不能超过7
		int pB= Integer.parseInt(""+s_data.charAt(s_data.length()-5));
		byte oneL=(byte) ((byte)pG<<4);

		frame[0]=(byte) (oneL+pSF);//101

		byte thrdL=(byte) ((byte) ((byte)(int)pB&0x07)<<4);
		frame[1]=(byte) (thrdL+pS);
		if(data<0)
			frame[1]= (byte) (frame[1]+0x80);

		return frame;
	}
	/**
	 * A7格式，double转2字节byte
	 * */
	public static byte[] A7DoubletoBytes(double data)
	{
		byte[] frame=new byte[2];
		//123.4
		DecimalFormat df   = new DecimalFormat("000.0");
		String s_data=df.format(data);
		//十分位
		int pSF= Integer.parseInt(""+s_data.charAt(s_data.length()-1));
		//个位
		int pG= Integer.parseInt(""+s_data.charAt(s_data.length()-3));
		//十位
		int pS= Integer.parseInt(""+s_data.charAt(s_data.length()-4));
		//百位
		int pB= Integer.parseInt(""+s_data.charAt(s_data.length()-5));
		byte oneL=(byte) ((byte)pG<<4);
		frame[0]=(byte) (oneL+pSF);
		byte thrdL=(byte) ((byte)pB<<4);
		frame[1]=(byte) (thrdL+pS);
		return frame;
	}
	/**
	 * 3字节数组转float，A.25
	 * 
	 * @param data
	 */
	public static float A25Byte3toFloat(byte[] data) {

		float pHundred = 0;
		float pThousand = 0;
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		int INDEX=0;
		try {
			if (data.length == 3) {
				pThousand = data[0] & 0x0f;
				pHundred = ((data[0]&0xff) >> 4) & 0x0f;
				pTen = data[1] & 0x0f;
				Count = ((data[1]&0xff) >> 4) & 0x0f;
				Ten = data[2] & 0x0f;
				Hundred = (data[2] >> 4) & 0x07;
				INDEX=(data[2]&0xff)>>7;
				if(INDEX==0)
					fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1 + pHundred * 0.01 + pThousand * 0.001);
				else if(INDEX==1)
					fTotal = -(float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1 + pHundred * 0.01 + pThousand * 0.001);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return fTotal;
	}
	/**
	 * 3字节数组转float，A.25
	 */
	public static float A25Byte3toFloat(byte a,byte b,byte c) {

		float pHundred = 0;
		float pThousand = 0;
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		int INDEX=0;
		try {

				pThousand = a & 0x0f;
				pHundred = ((a&0xff) >> 4) & 0x0f;
				pTen = b & 0x0f;
				Count = ((b&0xff) >> 4) & 0x0f;
				Ten = c & 0x0f;
				Hundred = (c >> 4) & 0x07;
				INDEX=(c&0xff)>>7;
				if(INDEX==0)
					fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1 + pHundred * 0.01 + pThousand * 0.001);
				else if(INDEX==1)
					fTotal = -(float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1 + pHundred * 0.01 + pThousand * 0.001);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return fTotal;
	}
	/**
	 * 日期转5字节byte A.15
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static byte[] A15Stringto5byte(String time) {
		byte[] data = new byte[5];// 分 时 日 月 年
		// 2018-02-09 12:23:23
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// time=df.format(new Date());
		String[] arr = time.split(" ");
		String[] arrBigT = arr[0].split("-");
		String[] arrSmallT = arr[1].split(":");
		int min = Integer.valueOf(arrSmallT[1]);
		int hour = Integer.valueOf(arrSmallT[0]);

		int day = Integer.valueOf(arrBigT[2]);
		int month = Integer.valueOf(arrBigT[1]);
		int year = Integer.valueOf(arrBigT[0]);
		// data[0]=(byte) ((minL<<4)&0xF0+minR&0x0F);
		byte a=(byte) (((byte) (min / 10) << 4) & 0xF0);
		byte b=(byte) ((byte) (min % 10) & 0x0F);
		data[0] = (byte) (a+b);
		a=(byte) (((byte) (hour / 10) << 4) & 0xF0);
		b=(byte) ((byte) (hour % 10) & 0x0F);
		data[1] = (byte) (a+b);
		a=(byte) (((byte) (day / 10) << 4) & 0xF0);
		b=(byte) ((byte) (day % 10) & 0x0F);
		data[2] = (byte)(a+b);
		a=(byte) (((byte) (month / 10) << 4) & 0xF0);
		b=(byte) ((byte) (month % 10) & 0x0F);
		data[3] = (byte) (a+b);
		a=(byte) (((byte) ((year%2000) / 10) << 4) & 0xF0);
		b=(byte) ((byte) ((year%2000) % 10) & 0x0F);
		data[4] = (byte) (a+b);
		return data;
	}

	/**
	 * 2字节byte转float,A.5
	 * 
	 * @param data
	 * @return
	 */
	public static float A5Byte2toFloat(byte[] data) {
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		int INDEX=0;
		try {
			pTen = data[0] & 0x0f;
			Count = ((data[0]&0xff) >> 4) & 0x0f;
			Ten = data[1] & 0x0f;
			Hundred = ((data[1]&0xff) >> 4) & 0x07;
			INDEX=(data[1]&0xff)>>7;
			if(INDEX==0)
				fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);
			else if(INDEX==1)
				fTotal = -(float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}

	/**
	 * A5byte转float,A.5
	 * 
	 * @param data1,data2
	 * @return
	 */
	public static float A5ByteByte2toFloat(byte data1, byte data2) {
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		int INDEX=0;
		try {
			pTen = data1 & 0x0f;
			Count = ((data1&0xff) >> 4) & 0x0f;
			Ten = data2 & 0x0f;
			Hundred = ((data2&0xff) >> 4) & 0x07;
			INDEX=(data2&0xff)>>7;
			if(INDEX==0)
				fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);
			else if(INDEX==1)
				fTotal = -(float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}

	/**
	 * 2字节byte转float,A.7
	 * 
	 * @param data
	 * @return
	 */
	public static float A7Byte2toFloat(byte[] data) {
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		try {
			if (data.length == 2) {
				pTen = data[0] & 0x0f;
				Count = ((data[0]&0xff) >> 4) & 0x0f;
				Ten = data[1] & 0x0f;
				Hundred = ((data[1]&0xff) >> 4) & 0x0f;

				fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);
			Log.i("湿度:","百 "+Hundred+"十 "+Count+"十分位 "+pTen*0.1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}

	public static float A7Humanity(byte[] data)
	{
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		try {
			if (data.length == 2) {
				pTen = data[0] & 0x0f;
				Count = ((data[0]&0xff) >> 4) & 0x0f;
				Ten = data[1] & 0x0f;
				Hundred = ((data[1]&0xff) >> 4) & 0x0f;

				fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen*0.1 );
				Log.i("湿度:","百 "+Hundred+"十 "+Ten+"个位 "+Count+"十分位 "+pTen*0.1);
				Log.i("湿度:","total:"+fTotal);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}
	public static float A7HumadityByteByte2toFloat(byte data1, byte data2) {
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		try {
			pTen = data1 & 0x0f;
			Count = ((data1&0xff) >> 4) & 0x0f;
			Ten = data2 & 0x0f;
			Hundred = ((data2&0xff) >> 4) & 0x0f;

			fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);
			Log.i("湿度:","百 "+Hundred+"十 "+Ten+"个位 "+Count+"十分位 "+pTen*0.1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}
	/**
	 * A7byte转float,A.7
	 * 
	 * @param data1,data2
	 * @return
	 */
	public static float A7ByteByte2toFloat(byte data1, byte data2) {
		float pTen = 0;
		float Count = 0;
		float Ten = 0;
		float Hundred = 0;
		float fTotal = 0;
		try {
			pTen = data1 & 0x0f;
			Count = ((data1&0xff) >> 4) & 0x0f;
			Ten = data2 & 0x0f;
			Hundred = ((data2&0xff) >> 4) & 0x0f;

			fTotal = (float) (Hundred * 100 + Ten * 10 + Count + pTen * 0.1);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}

	/**
	 * 3字节byte转float,A.9
	 * 
	 * @param data
	 * @return
	 */
	public static float A9Byte3toFloat(byte[] data) {
		float pTen = 0;
		float pHundred = 0;
		float pThousand = 0;
		float p100Hundred = 0;

		float Count = 0;
		float Ten = 0;
		float fTotal = 0;
		int INDEX=0;

		try {
			if (data.length == 3) {
				p100Hundred = data[0] & 0x0f;
				pThousand = ((data[0]&0xff) >> 4) & 0x0f;
				pHundred = data[1] & 0x0f;
				pTen = ((data[1]&0xff) >> 4) & 0x0f;
				Count = data[2] & 0x0f;
				Ten = ((data[2]&0xff) >> 4) & 0x70;
				Log.i("ByteUtil","万分位:"+p100Hundred+" 千分位:"+pThousand+" 百分位:"+pHundred+" 十分位:"+pTen+" 个位:"+pTen+" 十位:"+Ten);

				INDEX=(data[2]&0xff)>>7;
				if(INDEX==0)
					fTotal = (float) (Ten * 10 + Count + pTen * 0.1 + pHundred * 0.01 + pThousand * 0.001
							+ p100Hundred * 0.0001);
				else if(INDEX==1)
					fTotal = -(float) (Ten * 10 + Count + pTen * 0.1 + pHundred * 0.01 + pThousand * 0.001
							+ p100Hundred * 0.0001);
				//Log.i("ByteUtil","A9Byte3toFloat:"+fTotal);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fTotal;
	}
	/**
	 * 3字节byte转int,A.10
	 *
	 * @param data
	 * @return
	 */
	public static int A10Byte3toInt(byte[] data) {
		int i_qian = 0;
		int i_bai = 0;
		int i_shi = 0;
		int i_ge = 0;
		int i_wan=0;
		int i_shiwan=0;

		int i_sum = 0;
		try {
			if (data.length == 3) {
				i_ge = data[0] & 0x0f;
				i_shi = ((data[0]&0xff) >> 4) & 0x0f;
				i_bai = data[1] & 0x0f;
				i_qian = ((data[1]&0xff) >> 4) & 0x0f;
				i_wan = data[2] & 0x0f;
				i_shiwan = ((data[2]&0xff) >> 4) & 0x0f;

				i_sum =i_ge+i_shi*10+i_bai*100+i_qian*1000+i_wan*10000+i_shiwan*100000;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i_sum;
	}
	/**
	 * 超时时间和重发次数转两字节byte
	 * */
	public static byte[] AFN04F1_int2byte(int overlay,int count)
	{
		byte byte_count=(byte) count;
		byte[] data=new byte[2];
		data[0]= (byte) ((byte) overlay&0xff);
		if(count>3)
			return null;
		if(overlay>4095)
			return null;
		if(overlay<=255)
		{
			data[1]= (byte) (byte_count<<4);
		}
		else
		{
			byte high_data= (byte) (overlay>>8);
			byte high_count=(byte) (byte_count<<4);
			data[1]= (byte) (high_count+high_data);
		}
		return data;
	}
	/**
	 * 5字节byte转时间 A.15
	 * 
	 * @param data
	 * @return
	 */
	public static String A15BytetoString(byte[] data) {
		String sRet = "";
		if (data != null) {

				sRet = bcd2Str(data[4]) + "年" + bcd2Str(data[3]) + "月" + bcd2Str(data[2]) + "日 "
					+ bcd2Str(data[1]) + "时" + bcd2Str(data[0])+"分";
		}
			return sRet;
	}
	/**
	 * 5字节byte转时间 A.15
	 *
	 * @param data
	 * @return
	 */
	public static String A15BytetoString2(byte[] data) {
		String sRet = "";
		if (data != null) {

			sRet = bcd2Str(data[4]) + "-" + bcd2Str(data[3]) + "-" + bcd2Str(data[2]) + " "
					+ bcd2Str(data[1]) + ":" + bcd2Str(data[0]);
		}
		return sRet;
	}
	/**
	 * 1字节高四位转10位，低四位转个位
	 * 
	 * @param data
	 * @return 整形
	 */
	private static int oneByte2TenInt(byte data) {
		int y = data & 0x0f;
		int x = ((data&0xff) >> 4) & 0x0f;

		return x * 10 + y;
	}
	public static String A1BytetoString(byte[] data)
	{
		//Log.e("ByteUtil","data:"+ByteUtil.byte2HexStr(data));
		int sec=0,min=0,hour=0,day=0,weekday=0,month=0,year=0;
		int ten=((data[0]&0xff)>>4)*10;
		int count=data[0]&0x0f;
		sec=ten+count;
		ten=((data[1]&0xff)>>4)*10;
		count=data[1]&0x0f;
		min=ten+count;
		ten=((data[2]&0xff)>>4)*10;
		count=data[2]&0x0f;
		hour=ten+count;
		ten=((data[3]&0xff)>>4)*10;
		count=data[3]&0x0f;
		day=ten+count;
		weekday=(data[4]&0xff)>>5;
		ten=(((data[4]&0xff)>>4)&0x01)*10;
		count=data[4]&0x0f;
		month=ten+count;
		ten=((data[5]&0xff)>>4)*10;
		//Log.e("ByteUtil","ten:"+ten);
		count=data[5]&0x0f;
		//Log.e("ByteUtil","ten:"+ten);
		year=ten+count;


		return String.valueOf(year+"年"+month+"月"+day+"日")+ String.valueOf(" 星期"+weekday+" "+hour+"时"+min+"分"+sec+"秒");
	}
	public static String A1BytetoString2(byte[] data)
	{
		//Log.e("ByteUtil","data:"+ByteUtil.byte2HexStr(data));
		int sec=0,min=0,hour=0,day=0,weekday=0,month=0,year=0;
		int ten=((data[0]&0xff)>>4)*10;
		int count=data[0]&0x0f;
		sec=ten+count;
		ten=((data[1]&0xff)>>4)*10;
		count=data[1]&0x0f;
		min=ten+count;
		ten=((data[2]&0xff)>>4)*10;
		count=data[2]&0x0f;
		hour=ten+count;
		ten=((data[3]&0xff)>>4)*10;
		count=data[3]&0x0f;
		day=ten+count;
		weekday=(data[4]&0xff)>>5;
		ten=(((data[4]&0xff)>>4)&0x01)*10;
		count=data[4]&0x0f;
		month=ten+count;
		ten=((data[5]&0xff)>>4)*10;
		//Log.e("ByteUtil","ten:"+ten);
		count=data[5]&0x0f;
		//Log.e("ByteUtil","ten:"+ten);
		year=ten+count;
		if(year==0&&month==0&&day==0&&hour==0&&min==0&&sec==0)
		return "-1";
		else
		{
			String str_month= String.valueOf(month);
			String str_day= String.valueOf(day);
			String str_hour= String.valueOf(hour);
			String str_min= String.valueOf(min);
			String str_sec= String.valueOf(sec);
			if(month<10)
				str_month="0"+month;
			if(day<10)
				str_day="0"+day;
			if(hour<10)
				str_hour="0"+hour;
			if(min<10)
				str_min="0"+str_min;
			if(sec<10)
				str_sec="0"+str_sec;
			//return String.valueOf(year+"-"+month+"-"+day)+String.valueOf(" "+hour+":"+min+":"+sec);
			return year+"-"+str_month+"-"+str_day+" "+str_hour+":"+str_min+":"+str_sec;

		}

	}
	public static String A1BytetoString3(byte[] data)
	{
		//Log.e("ByteUtil","data:"+ByteUtil.byte2HexStr(data));
		int sec=0,min=0,hour=0,day=0,weekday=0,month=0,year=0;
		int ten=((data[0]&0xff)>>4)*10;
		int count=data[0]&0x0f;
		sec=ten+count;
		ten=((data[1]&0xff)>>4)*10;
		count=data[1]&0x0f;
		min=ten+count;
		ten=((data[2]&0xff)>>4)*10;
		count=data[2]&0x0f;
		hour=ten+count;
		ten=((data[3]&0xff)>>4)*10;
		count=data[3]&0x0f;
		day=ten+count;
		weekday=(data[4]&0xff)>>5;
		ten=(((data[4]&0xff)>>4)&0x01)*10;
		count=data[4]&0x0f;
		month=ten+count;
		ten=((data[5]&0xff)>>4)*10;
		//Log.e("ByteUtil","ten:"+ten);
		count=data[5]&0x0f;
		//Log.e("ByteUtil","ten:"+ten);
		year=ten+count;

		return String.valueOf(year+"年"+month+"月"+day+"日")+ String.valueOf(" "+hour+"时"+min+"分"+sec+"秒");
	}

	/**
     * 3字节转日期，A.20格式
     * */
	public static String A20Byte2String(byte[] data)
    {
        String str_date;
        String day,month,year;
		day=bcd2Str(data[0]);
		month=bcd2Str(data[1]);
		year=bcd2Str(data[2]);
        //day=data[0]&0x0f+((data[0]&0xff)>>4);
        //month=data[1]&0x0f+((data[1]&0xff)>>4);
        //year=data[2]&0x0f+((data[2]&0xff)>>4);
        str_date=year+"年"+month+"月"+day+"日";
        //Log.i("ByteUtil","A.20日期:"+str_date);
        return str_date;
    }

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String bcd2Str(byte bytes) {
		StringBuffer temp = new StringBuffer(2);
			temp.append((byte) ((bytes & 0xf0) >>> 4));
			temp.append((byte) (bytes & 0x0f));
		return temp.toString();
		//return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
		//		.toString().substring(1) : temp.toString();
	}
	/**
	 * 字节转二进制字符串
	 * */
	public static String byte2bits(byte b) {

		int z = b;
		z |= 256;
		String str = Integer.toBinaryString(z);
		int len = str.length();
		return str.substring(len - 8, len);
	}
	/*
	* 将二进制字符串转换回字节
	* */
	public static byte bit2byte(String bString){
		byte result=0;
		for(int i=bString.length()-1,j=0;i>=0;i--,j++){
			result+=(Byte.parseByte(bString.charAt(i)+"")* Math.pow(2, j));
		}
		return result;
	}
	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
}
