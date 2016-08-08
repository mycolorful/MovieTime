package per.yrj.movietime.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String toMD5(String psd) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			byte[] digest = msgDigest.digest(psd.getBytes());
			
			for(byte b : digest){
				int i = b&0xff;
				String hexString = Integer.toHexString(i);
				if(hexString.length() < 2){
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}
