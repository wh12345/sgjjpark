package com.qy.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 

/**
 * MD5加密算法 用于用户密码的加密
 */

public class Md5 {
	private static final String DEFAULT_ENCODING = "utf-8";
	
	/*
	 * 
	 * md5加盐
	 */
	public static String md5(String strSrc, String key) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] b = strSrc.getBytes(DEFAULT_ENCODING);
			md5.update(b);
			String result = "";
			byte[] temp = md5.digest(key.getBytes(DEFAULT_ENCODING));
			String s = "";
			for (byte bb : temp) {
				s += (bb + " ");
			}
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			return result;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	 
	public static String md5(String plainText) {
	  String result = null;
	  try {
	   MessageDigest md = MessageDigest.getInstance("MD5");
	   md.update(plainText.getBytes(DEFAULT_ENCODING));
	   byte b[] = md.digest();
	   int i;
	   StringBuffer buf = new StringBuffer("");
	   for (int offset = 0; offset < b.length; offset++) {
	    i = b[offset];
	    if (i < 0)
	     i += 256;
	    if (i < 16)
	     buf.append("0");
	    buf.append(Integer.toHexString(i));
	
	   }
	    result = buf.toString();  //md5 32bit
	   // result = buf.toString().substring(8, 24))); //md5 16bit
	   //result = buf.toString().substring(8, 24);
	   //System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
	   //System.out.println("md5 32bit: " + buf.toString() );
	  } catch (NoSuchAlgorithmException e) {
	   e.printStackTrace();
	  } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	  return result;
	}
	public static void main(String[] args) {
			System.out.println(md5("0123456"));		
	}
 
}
