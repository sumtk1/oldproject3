package com.zyd.wlwsdk.utlis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hygo00 on 2016/7/15.
 * 采用MD5加密解密 
 */  
public class MD5Utlis {
  
    /*** 
     * MD5加码 生成32位md5码 
     */  
	public static String Md5(String plainText ) { 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plainText.getBytes()); 
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer(""); 
			
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
				buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			System.out.println("result: " + buf.toString());//32位的加密 
			System.out.println("result: " + buf.toString().substring(8,24));//16位的加密 
			return buf.toString();
		} catch (NoSuchAlgorithmException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}
		return null; 
    		
    }
}  