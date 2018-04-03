package com.cmcc.vrp.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

public class RSAUtil {


    public static String getRequestKey() throws NoSuchAlgorithmException {

        String keySeed = UUID.randomUUID().toString();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(keySeed.getBytes());
        byte[] buffer = md5.digest();
        String keyString = bcd2Str(buffer).substring(16);

        return keyString;
    }

    public static byte[] AESEncrypt(byte[] src, byte[] key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] crypted = null;

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        crypted = cipher.doFinal(src);

        return crypted;
    }

    public static byte[] AESDecrypt(byte[] src, byte[] key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = null;

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        data = cipher.doFinal(src);
        return data;
    }

    /**
     * 用于服务端获取私钥，对客户端上传的密文进行解密
     *
     * @param priKey 私钥证书（服务端保存）
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPriKeyForDecode(String priKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey rsaKey = null;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
            Base64.decodeBase64(priKey));
        rsaKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        return rsaKey;
    }

    /**
     * 用于客户端获取公钥，用于解密服务端下发的密文
     *
     * @param pubKey 公钥证书（客户端、服务端保存）
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPubKeyForDecode(String pubKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey rsaKey = null;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
            Base64.decodeBase64(pubKey));
        rsaKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return rsaKey;
    }

    /**
     * 用于服务端获取私钥，加密待下发的数据
     *
     * @param priKey 私钥证书（服务端保存）
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPriKeyForEncode(String priKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey rsaKey = null;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
            Base64.decodeBase64(priKey));
        rsaKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        return rsaKey;
    }

    /**
     * 用于客户端获取公钥，加密待上发的数据
     *
     * @param pubKey 公钥证书（服务端、客户端保存）
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPubKeyForEncode(String pubKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey rsaKey = null;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
            Base64.decodeBase64(pubKey));
        rsaKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return rsaKey;
    }

    /**
     * 用于客户端加密
     *
     * @param data
     * @param pubKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String RSAEncryptForClient(String data, RSAPublicKey pubKey)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String crypted = null;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        int keyLength = pubKey.getModulus().bitLength() / 8;
        String[] dataPieces = splitString(data, keyLength - 11);

        String tmp = "";
        for (String piece : dataPieces) {
            tmp += bcd2Str(cipher.doFinal(piece.getBytes()));
        }
        crypted = tmp;

        return crypted;
    }

    /**
     * 用于服务端RSA加密
     *
     * @param data
     * @param priKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String RSAEncryptForServer(String data, RSAPrivateKey priKey)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String crypted = null;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        int keyLength = priKey.getModulus().bitLength() / 8;
        String[] dataPieces = splitString(data, keyLength - 11);

        String tmp = "";
        for (String piece : dataPieces) {
            tmp += bcd2Str(cipher.doFinal(piece.getBytes()));
        }
        crypted = tmp;

        return crypted;
    }

    /**
     * 用于服务端RSA解密
     *
     * @param src
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String RSADecryptForServer(String src, RSAPrivateKey key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String data = null;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        int keyLength = key.getModulus().bitLength() / 8;
        byte[] bytes = src.getBytes();
        byte[] bcd = ASCII2BCD(bytes, bytes.length);
        byte[][] cryptedPieces = splitArray(bcd, keyLength);

        String tmp = "";
        for (byte[] piece : cryptedPieces) {
            tmp += new String(cipher.doFinal(piece));
        }
        data = tmp;
        return data;
    }

    /**
     * 用于客户端RSA解密
     *
     * @param src
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String RSADecryptForClient(String src, RSAPublicKey key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String data = null;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        int keyLength = key.getModulus().bitLength() / 8;
        byte[] bytes = src.getBytes();
        byte[] bcd = ASCII2BCD(bytes, bytes.length);
        byte[][] cryptedPieces = splitArray(bcd, keyLength);

        String tmp = "";
        for (byte[] piece : cryptedPieces) {
            tmp += new String(cipher.doFinal(piece));
        }
        data = tmp;
        return data;
    }

    public static byte[] HmacSHA1Signature(byte[] data, byte[] key)
        throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] sign = null;

        SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        sign = mac.doFinal(data);

        return sign;
    }

    private static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    private static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    private static String bcd2Str(byte[] bytes) {
        char[] temp = new char[bytes.length * 2];
        char val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    private static byte[] ASCII2BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc2bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc2bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private static byte asc2bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // // 客户端生成随机密钥
        // String secretKey = "62DD76320A47C78E";
        // // 使用服务端下发的公钥对密钥进行加密
        // // RSAPublicKey pubKey = RSAUtil.getRsaPubKey();
        // String secretKeyEncrypted = RSAUtil.RSAEncrypt(secretKey, pubKey);
        // System.out.println("req_key=" + secretKeyEncrypted);
        // // 使用随机密钥对数据进行加密
        // String json =
        // "{\"phone_id\":\"4334ds\",\"message\":\"短信测试内容！\",\"flag\":\"SEND\"}";
        // byte[] jsonEncrypted = RSAUtil.AESEncrypt(json.getBytes("UTF-8"),
        // secretKey.getBytes("UTF-8"));
        // System.out.println("req_msg="
        // + Base64.encodeBase64String(jsonEncrypted));
        //
        // // 服务端使用与公钥配对的私钥进行解密
        // // RSAPrivateKey priKey = RSAUtil.getRsaPriKey();
        // // String _secretKey = RSAUtil.RSADecrypt(secretKeyEncrypted,
        // priKey);
        // System.out.println("req_key=" + _secretKey);
        // // 使用解密后的私钥进行解密数据
        // byte[] req_msg = RSAUtil.AESDecrypt(jsonEncrypted,
        // _secretKey.getBytes("UTF-8"));
        // System.out.println("req_msg=" + new String(req_msg));
    }
}
