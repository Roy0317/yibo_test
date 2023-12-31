package com.yibo.yiboapp.utils;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static String iv = "0>2^4*6(~9!B#D$F";
    private static String Algorithm = "AES";
    private static String AlgorithmProvider = "AES/CBC/PKCS5Padding";

    public AESUtils() {
    }

    public static byte[] generatorKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(Algorithm);
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static IvParameterSpec getIv() throws UnsupportedEncodingException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("utf-8"));
        return ivParameterSpec;
    }

    public static byte[] encrypt(String src, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        SecretKey secretKey = new SecretKeySpec(key, Algorithm);
        IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(1, secretKey, ivParameterSpec);
        byte[] cipherBytes = cipher.doFinal(src.getBytes(Charset.forName("utf-8")));
        return cipherBytes;
    }

    public static String encrypt(String source, String key, String iv) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), Algorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("utf-8"));
            Cipher cipher = Cipher.getInstance(AlgorithmProvider);
            cipher.init(1, secretKey, ivParameterSpec);
            byte[] cipherBytes = cipher.doFinal(source.getBytes(Charset.forName("utf-8")));
            return byteToHexString(cipherBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] decrypt(String src, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, Algorithm);
        IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(2, secretKey, ivParameterSpec);
        byte[] hexBytes = hexStringToBytes(src);
        byte[] plainBytes = cipher.doFinal(hexBytes);
        return plainBytes;
    }

    public static String decrypt(String target, String key, String iv) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), Algorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("utf-8"));
            Cipher cipher = Cipher.getInstance(AlgorithmProvider);
            cipher.init(2, secretKey, ivParameterSpec);
            byte[] hexBytes = hexStringToBytes(target);
            byte[] plainBytes = cipher.doFinal(hexBytes);
            return new String(plainBytes, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < src.length; ++i) {
            int v = src[i] & 255;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }

            sb.append(hv);
        }

        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];

        for (int i = 0; i < length; ++i) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }

        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String enc(String key, String data) {
        try {
            byte[] k = key.getBytes("utf-8");
            String res = byteToHexString(encrypt(data, k));
            return res;
        } catch (Exception var4) {
            var4.printStackTrace();
            return "";
        }
    }

    public static String dec(String key, String data) {
        try {
            byte[] k = key.getBytes("utf-8");
            return new String(decrypt(data, k), "utf-8");
        } catch (Exception var3) {
            var3.printStackTrace();
            return "";
        }
    }
}
