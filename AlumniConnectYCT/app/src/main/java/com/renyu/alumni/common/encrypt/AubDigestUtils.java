package com.renyu.alumni.common.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * reference apache commons <a
 * href="http://commons.apache.org/codec/">http://commons.apache.org/codec/</a>
 * 
 * support MD2/MD5/SHA/SHA256/SHA384/SHA512
 * @author Aub
 * http://aub.iteye.com/blog/1131494
 * 
 */
public class AubDigestUtils {


	static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	private static MessageDigest getMd5Digest() {
		return getDigest("MD5");
	}


	private static MessageDigest getShaDigest() {
		return getDigest("SHA");
	}


	private static MessageDigest getSha256Digest() {
		return getDigest("SHA-256");
	}

	private static MessageDigest getSha384Digest() {
		return getDigest("SHA-384");
	}


	private static MessageDigest getSha512Digest() {
		return getDigest("SHA-512");
	}

	/**
	 * ʹ��MD5��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return ��ϢժҪ������Ϊ16���ֽ����飩
	 */
	public static byte[] encodeMD5(byte[] data) {
		return getMd5Digest().digest(data);
	}

	/**
	 * ʹ��MD5��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return ��ϢժҪ������Ϊ32��ʮ�������ַ�����
	 */
	public static String encodeMD5Hex(byte[] data) {
		return AubHex.encodeHexStr(encodeMD5(data));
	}

	/**
	 * ʹ��SHA-1��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-1��ϢժҪ������Ϊ20���ֽ����飩
	 */
	public static byte[] encodeSHA(byte[] data) {
		return getShaDigest().digest(data);
	}

	/**
	 * ʹ��SHA-1��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-1��ϢժҪ������Ϊ40��ʮ�������ַ�����
	 */
	public static String encodeSHAHex(byte[] data) {
		return AubHex.encodeHexStr(getShaDigest().digest(data));
	}

	/**
	 * ʹ��SHA-256��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-256��ϢժҪ������Ϊ32���ֽ����飩
	 */
	public static byte[] encodeSHA256(byte[] data) {
		return getSha256Digest().digest(data);
	}

	/**
	 * ʹ��SHA-256��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-256��ϢժҪ������Ϊ64��ʮ�������ַ�����
	 */
	public static String encodeSHA256Hex(byte[] data) {
		return AubHex.encodeHexStr(encodeSHA256(data));
	}
	
	/**
	 * ʹ��SHA-256��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-256��ϢժҪ������Ϊ64��ʮ�������ַ�����
	 */
	public static String encodeSHA256Hex(String data) {
		try {
			return AubHex.encodeHexStr(encodeSHA256(data.getBytes(Base64.CHARSET_UTF8)));
		} catch (UnsupportedEncodingException e) {
			return data;
		}
	}


	/**
	 * ʹ��SHA-384��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-384��ϢժҪ������Ϊ43���ֽ����飩
	 */
	public static byte[] encodeSHA384(byte[] data) {
		return getSha384Digest().digest(data);
	}

	/**
	 * ʹ��SHA-384��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-384��ϢժҪ������Ϊ86��ʮ�������ַ�����
	 */
	public static String encodeSHA384Hex(byte[] data) {
		return AubHex.encodeHexStr(encodeSHA384(data));
	}

	/**
	 * ʹ��SHA-512��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-512��ϢժҪ������Ϊ64���ֽ����飩
	 */
	public static byte[] encodeSHA512(byte[] data) {
		return getSha512Digest().digest(data);
	}

	/**
	 * ʹ��SHA-512��ϢժҪ�㷨������ϢժҪ
	 * 
	 * @param data
	 *            ����ϢժҪ������
	 * @return SHA-512��ϢժҪ������Ϊ128��ʮ�������ַ�����
	 */
	public static String encodeSHA512Hex(byte[] data) {
		return AubHex.encodeHexStr(encodeSHA512(data));
	}

}