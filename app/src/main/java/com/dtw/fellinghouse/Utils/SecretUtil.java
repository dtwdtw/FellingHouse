package com.dtw.fellinghouse.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public class SecretUtil {
    public static String getMD5(String value){
        MessageDigest messageDigest=null;
        try {
            messageDigest=MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] out=messageDigest.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<out.length; i++) {
            hexString.append(Integer.toHexString(0xFF & out[i]));
        }
        return hexString.toString();
    }
}
