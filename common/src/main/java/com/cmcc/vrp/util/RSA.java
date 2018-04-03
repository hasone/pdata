package com.cmcc.vrp.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

    public static final int KEY_SIZE = 1024;
    public static final String RSA_ALGORTHM = "RSA";
    private static final Log LOGGER = LogFactory
        .getLog(RSA.class);
    /**
     * RSA最大加密明文长度
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文长度
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static byte[] encrypt(Cipher cipher, final byte[] data)
        throws Exception {
        return doCrypt(cipher, MAX_ENCRYPT_BLOCK, data);
    }

    private static byte[] decrypt(Cipher cipher, final byte[] data)
        throws Exception {
        return doCrypt(cipher, MAX_DECRYPT_BLOCK, data);
    }

    private static byte[] doCrypt(Cipher cipher, int maxBlockSize,
                                  final byte[] data) throws Exception {

        byte[] encryptedData = null;
        int inputLen = data.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段操作
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxBlockSize) {
                    cache = cipher.doFinal(data, offSet, maxBlockSize);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                outputStream.write(cache, 0, cache.length);
                i++;
                offSet = i * maxBlockSize;
            }
            encryptedData = outputStream.toByteArray();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        return encryptedData;

    }

    /**
     * 使用私钥加密
     * <p>
     *
     * @param data
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(final byte[] data,
                                             final byte[] keyBytes) throws Exception {
        // 取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
            keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return encrypt(cipher, data);
    }

    public static byte[] encryptByPrivateKey(final byte[] data, String base64Key)
        throws Exception {
        return encryptByPrivateKey(data, base64Decode(base64Key));
    }

    /**
     * 使用私钥解密
     * <p>
     *
     * @param data
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(final byte[] data, byte[] keyBytes)
        throws Exception {

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
            keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return decrypt(cipher, data);
    }

    public static byte[] decryptByPrivateKey(final byte[] data, String base64Key)
        throws Exception {
        return decryptByPrivateKey(data, base64Decode(base64Key));
    }

    /**
     * 使用公钥加密
     * <p>
     *
     * @param data
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(final byte[] data, byte[] keyBytes)
        throws Exception {
        // 取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return encrypt(cipher, data);
    }

    public static byte[] encryptByPublicKey(final byte[] data, String base64Key)
        throws Exception {
        return encryptByPublicKey(data, base64Decode(base64Key));
    }

    /**
     * 使用公钥解密
     * <p>
     *
     * @param data
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(final byte[] data, byte[] keyBytes)
        throws Exception {
        // 取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return decrypt(cipher, data);
    }

    public static byte[] decryptByPublicKey(final byte[] data, String base64Key)
        throws Exception {
        return decryptByPublicKey(data, base64Decode(base64Key));
    }

    /**
     * 生成密钥对
     * <p>
     *
     * @return
     */
    public static KeyPair initKey() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(RSA_ALGORTHM);
            keyPairGen.initialize(KEY_SIZE);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * base64编码
     * <p>
     *
     * @param data
     * @return
     */
    public static String base64Encode(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    /**
     * base64解码
     * <p>
     *
     * @param data
     * @return
     */
    public static byte[] base64Decode(String data) {
        return Base64.decodeBase64(data);
    }

	/*public static void main1(String[] args) throws Exception {
        KeyPair keyPair = initKey();

		String publicKeyString = base64Encode(keyPair.getPublic().getEncoded());
		String privateKeyString = base64Encode(keyPair.getPrivate()
				.getEncoded());

		FileUtils.write(new File("C:\\Users\\cmcc\\Desktop\\public.key"),
				publicKeyString);
		FileUtils.write(new File("C:\\Users\\cmcc\\Desktop\\private.key"),
				privateKeyString);
	}
*/
	/*public static void main(String[] args) throws Exception {

		KeyPair keyPair = initKey();

		String publicKeyString = base64Encode(keyPair.getPublic().getEncoded());
		String privateKeyString = base64Encode(keyPair.getPrivate()
				.getEncoded());

		System.out.println("publicKey: " + publicKeyString);
		System.out.println("privateKey: " + privateKeyString);

		// String text = "hello world!";

		String text = FileUtils.readFileToString(new File(
				"C:\\Users\\cmcc\\Desktop\\卡数据.xlsx"));

		System.out.println("原文：" + text);

		System.out.println("公钥加密-私钥解密");
		byte[] encrypted_1 = encryptByPublicKey(text.getBytes(),
				base64Decode(publicKeyString));
		System.out.println("密文：" + new String(encrypted_1));

		byte[] decrypted_1 = decryptByPrivateKey(encrypted_1,
				base64Decode(privateKeyString));
		System.out.println("解密：" + new String(decrypted_1));

		System.out.println("私钥加密-公钥解密");
		byte[] encrypted_2 = encryptByPrivateKey(text.getBytes(),
				base64Decode(privateKeyString));
		System.out.println("密文：" + new String(encrypted_2));

		byte[] decrypted_2 = decryptByPublicKey(encrypted_2,
				base64Decode(publicKeyString));
		System.out.println("解密：" + new String(decrypted_2));
	}*/
}
