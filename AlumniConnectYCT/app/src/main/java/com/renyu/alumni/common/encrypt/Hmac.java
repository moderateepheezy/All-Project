package com.renyu.alumni.common.encrypt;

import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Hmac<br/>
 * algorithm HmacMD5/HmacSHA/HmacSHA256/HmacSHA384/HmacSHA512
 * @author Aub
 */
public class Hmac {
	
	/**
	 * ���ݸ�����Կ�����㷨������Կ
	 * 
	 * @param algorithm
	 *            ��Կ�㷨
	 * @return ��Կ
	 * @throws RuntimeException
	 *             �� {@link java.security.NoSuchAlgorithmException} ����ʱ
	 */
	private static byte[] getHmacKey(String algorithm){
		//��ʼ��KeyGenerator
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
		//������Կ
		SecretKey secretKey = keyGenerator.generateKey();
		//�����Կ
		return secretKey.getEncoded();
	}
	
	/**
	 * ��ȡ HmaMD5����Կ
	 * 
	 * @return  HmaMD5����Կ
	 * @throws RuntimeException
	 *             �� {@link java.security.NoSuchAlgorithmException} ����ʱ
	 */
	public static byte[] getHmaMD5key(){
		return getHmacKey("HmacMD5");
	}
	
	/**
	 * ��ȡ HmaSHA����Կ
	 * 
	 * @return  HmaSHA����Կ
	 * @throws RuntimeException
	 *             �� {@link java.security.NoSuchAlgorithmException} ����ʱ
	 */
	public static byte[] getHmaSHAkey(){
		return getHmacKey("HmacSHA1");
	}
	
	/**
	 * ��ȡ HmaSHA256����Կ
	 * 
	 * @return  HmaSHA256����Կ
	 * @throws RuntimeException
	 *             �� {@link java.security.NoSuchAlgorithmException} ����ʱ
	 */
	public static byte[] getHmaSHA256key(){
		return getHmacKey("HmacSHA256");
	}
	
	/**
	 * ��ȡ HmaSHA384����Կ
	 * 
	 * @return  HmaSHA384����Կ
	 * @throws RuntimeException
	 *             �� {@link java.security.NoSuchAlgorithmException} ����ʱ
	 */
	public static byte[] getHmaSHA384key(){
		return getHmacKey("HmacSHA384");
	}
	
	/**
	 * ��ȡ HmaSHA512����Կ
	 * 
	 * @return  HmaSHA384����Կ
	 * @throws RuntimeException
	 *             �� {@link java.security.NoSuchAlgorithmException} ����ʱ
	 */
	public static byte[] getHmaSHA512key(){
		return getHmacKey("HmacSHA512");
	}
	
	/**
	 * ת����Կ
	 * 
	 * @param key	��������Կ
	 * @param algorithm	��Կ�㷨
	 * @return ��Կ
	 */
	private static Key toKey(byte[] key,String algorithm){
		//������Կ
		return new SecretKeySpec(key, algorithm);
	}
	
	/**
	 * ʹ��HmacMD5��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacMD5(byte[] data, Key key){
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacMD5");
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}catch (InvalidKeyException e) {
			e.printStackTrace();
			return new byte[0];
		}
		return mac.doFinal(data);
	}
	
	/**
	 * ʹ��HmacMD5��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacMD5(byte[] data, byte[] key){
		Key k = toKey(key, "HmacMD5");
		return encodeHmacMD5(data, k);
	}
	
	/**
	 * ʹ��HmacSHA��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA(byte[] data, Key key){
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}catch (InvalidKeyException e) {
			e.printStackTrace();
			return new byte[0];
		}
		return mac.doFinal(data);
	}
	
	/**
	 * ʹ��HmacSHA��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA(byte[] data, byte[] key){
		Key k = toKey(key, "HmacSHA1");
		return encodeHmacSHA(data, k);
	}
	
	/**
	 * ʹ��HmacSHA256��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA256(byte[] data, Key key){
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}catch (InvalidKeyException e) {
			e.printStackTrace();
			return new byte[0];
		}
		return mac.doFinal(data);
	}
	
	/**
	 * ʹ��HmacSHA256��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA256(byte[] data, byte[] key){
		Key k = toKey(key, "HmacSHA256");
		return encodeHmacSHA256(data, k);
	}
	
	
	/**
	 * ʹ��HmacSHA384��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA384(byte[] data, Key key){
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA384");
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}catch (InvalidKeyException e) {
			e.printStackTrace();
			return new byte[0];
		}
		return mac.doFinal(data);
	}
	
	/**
	 * ʹ��HmacSHA384��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA384(byte[] data, byte[] key){
		Key k = toKey(key, "HmacSHA384");
		return encodeHmacSHA384(data, k);
	}
	
	
	
	/**
	 * ʹ��HmacSHA512��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA512(byte[] data, Key key){
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA512");
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}catch (InvalidKeyException e) {
			e.printStackTrace();
			return new byte[0];
		}
		return mac.doFinal(data);
	}
	
	/**
	 * ʹ��HmacSHA512��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data ����ϢժҪ������
	 * @param key ��Կ
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeHmacSHA512(byte[] data, byte[] key){
		Key k = toKey(key, "HmacSHA512");
		return encodeHmacSHA512(data, k);
	}
	
	
	private static String  showByteArray(byte[] data){
		if(null == data){
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for(byte b:data){
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		byte[] key = getHmaMD5key();
//		byte[] key = getHmaSHAkey();
//		byte[] key = getHmaSHA256key();
//		byte[] key = getHmaSHA384key();
		byte[] key = getHmaSHA512key();
		
		
		System.out.println("������Կ: byte[]:"+showByteArray(key).length());
		
		String data = "Macadsfads���������ص�������ˮ����ˮ";
		System.out.println("����ǰ����: string:"+data);
		System.out.println("����ǰ����: byte[]:"+showByteArray(data.getBytes()));
		System.out.println();
//		byte[] encodeData = encodeHmacMD5(data.getBytes(), key);
//		byte[] encodeData = encodeHmacSHA(data.getBytes(), key);
//		byte[] encodeData = encodeHmacSHA256(data.getBytes(), key);
//		byte[] encodeData = encodeHmacSHA384(data.getBytes(), key);
		byte[] encodeData = encodeHmacSHA512(data.getBytes(), key);
		System.out.println("���ܺ�����: byte[]:"+showByteArray(encodeData).length());
		System.out.println("���ܺ�����: byte[]:"+encodeData.length);
		System.out.println("���ܺ�����: hexStr:"+Hex.encodeHexString(encodeData));
		System.out.println();
	}
}
