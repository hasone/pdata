package com.cmcc.vrp.util;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Encrypter {

    private static final Log LOGGER = LogFactory
        .getLog(Encrypter.class);

    private static final String key = "this is secret!";
    
    public static byte[] encrypt(byte[] data, String username, String mobile,
                                 String key) {
        if (data == null || StringUtils.isBlank(key)
            || StringUtils.isBlank(username) || StringUtils.isBlank(mobile)) {
            return null;
        }

        String aes_key = username + mobile;

        LOGGER.info("key=" + key + ", username=" + username + ", mobile=" + mobile + ", aes_key=" + aes_key);
        try {
            byte[] aesEncoded = AES.encrypt(data, aes_key.getBytes("UTF-8"));
            return RSA.encryptByPrivateKey(aesEncoded, key);
        } catch (Exception e) {
            // 加密失败
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] data, String username, String mobile,
                                 String key) {
        if (data == null || StringUtils.isBlank(key)
            || StringUtils.isBlank(username) || StringUtils.isBlank(mobile)) {
            return null;
        }
        try {
            byte[] rsaDecrypted = RSA.decryptByPublicKey(data, key);
            return AES.decrypt(rsaDecrypted, (username + mobile).getBytes());
        } catch (Exception e) {
            // 解密失败
        }
        return null;
    }

    public static void encryptFile(File input, File output, String username,
                                   String mobile, String key) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(input);
        byte[] encoded = encrypt(data, username, mobile, key);

        if (encoded == null) {
            throw new IOException("加密失败");
        }
        FileUtils.writeByteArrayToFile(output, encoded);
    }

    public static void decryptFile(File input, File output, String username,
                                   String mobile, String key) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(input);
        byte[] decoded = decrypt(data, username, mobile, key);

        if (decoded == null) {
            throw new IOException("解密失败");
        }
        FileUtils.writeByteArrayToFile(output, decoded);
    }

    public static void encryptFile(File input, File output, String username,
                                   String mobile, File keyfile) throws IOException {
        String key = FileUtils.readFileToString(keyfile);
        encryptFile(input, output, username, mobile, key);
    }

    public static void decryptFile(File input, File output, String username,
                                   String mobile, File keyfile) throws IOException {
        String key = FileUtils.readFileToString(keyfile);
        decryptFile(input, output, username, mobile, key);
    }

	/*public static void main(String[] args) throws Exception {
        String username = "乔浩";
		String mobile = "18867102967";
		String publicKeyFile = "C:\\works\\svn\\opp\\4ggogo\\trunk\\VRP\\ChongQing\\Decrypter\\sample\\public.key";
		String privateKeyFile = "C:\\works\\svn\\opp\\4ggogo\\trunk\\VRP\\ChongQing\\Decrypter\\sample\\private.key";
		String inputFile = "C:\\works\\svn\\opp\\4ggogo\\trunk\\VRP\\ChongQing\\Decrypter\\sample\\卡数据.xlsx";
		String outputFile = "C:\\Users\\cmcc\\Desktop\\已加密数据.data";

		long s = System.nanoTime();
		encryptFile(new File(inputFile), new File(outputFile), username,
		mobile, new File(privateKeyFile));
		long e = System.nanoTime();

		System.out.println("time: " + (e - s));

		inputFile = outputFile;
		outputFile = "C:\\Users\\cmcc\\Desktop\\已解密数据.xlsx";
		s = System.nanoTime();
		decryptFile(new File(inputFile), new File(outputFile), username,
				mobile, new File(publicKeyFile));
		e = System.nanoTime();

		System.out.println("time: " + (e - s));
	}*/
    
    /** 
     * 解密
     * @Title: decrypt 
     */
    public static String decrypt(String encryptString) {
        byte[] encryptArray = DatatypeConverter.parseHexBinary(encryptString);
        byte[] decryptResult = null;
        try {
            decryptResult = AES.decrypt(encryptArray, key.getBytes());
            if (decryptResult == null) {
                LOGGER.info("解密结果为空！");
            } else {
                String result = new String(decryptResult);
                LOGGER.info("解密结果=" + result);
                return result;
            }
        } catch (Exception e) {
            LOGGER.error("解密失败，失败原因：" + e.getMessage());
        }
        return null;

    }

    
    /** 
     * 加密
     * @Title: encrypt 
     */
    public static String encrypt(String str) {
        byte[] encryptResult = AES.encrypt(str.getBytes(),
                key.getBytes());
        return DatatypeConverter.printHexBinary(encryptResult);
    }
}
