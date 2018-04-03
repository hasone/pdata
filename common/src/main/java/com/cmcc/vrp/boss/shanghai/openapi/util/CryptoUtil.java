/*
 * Copyright 2013 Primeton.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmcc.vrp.boss.shanghai.openapi.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;

/**
 * 鍔犺В瀵嗗伐鍏风被
 * 
 * @author wuyuhou
 * 
 */
public class CryptoUtil {

    private static final String STR_ENCODE = "UTF-8";

    /**
     * 鍔犲瘑
     * 
     * @param dataString
     *            闇�瑕佸姞瀵嗙殑鍘熷鏁版嵁
     * @param algorithm
     *            鍔犲瘑绠楁硶锛屽彲浠ヤ负绌猴紝榛樿涓篋ES绠楁硶
     * @param secretKey
     *            鍔犲瘑瀵嗛挜锛屽彲浠ヤ负绌�
     * @return 鍔犲瘑鍚庣殑鏁版嵁
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String dataString, String algorithm, SecretKey secretKey) {
        if (dataString == null) {
            throw new IllegalArgumentException("Encrypt dataString is null!");
        }
        Cipher cipher = new Cipher();
        cipher.setAlgorithm(algorithm);
        cipher.setSecretKey(secretKey);
        String result = null;
        try {
            result = new String(cipher.encrypt(dataString.getBytes()), STR_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 瑙ｅ瘑
     * 
     * @param dataString
     *            闇�瑕佽В瀵嗙殑鏁版嵁
     * @param algorithm
     *            鍔犲瘑绠楁硶锛屽彲浠ヤ负绌猴紝榛樿涓篋ES绠楁硶
     * @param secretKey
     *            鍔犲瘑瀵嗛挜锛屽彲浠ヤ负绌�
     * @return 瑙ｅ瘑鍚庣殑鏁版嵁
     */
    public static String decrypt(String dataString, String algorithm, SecretKey secretKey) {
        if (dataString == null) {
            throw new IllegalArgumentException("Decrypt dataString is null!");
        }
        Cipher cipher = new Cipher();
        cipher.setAlgorithm(algorithm);
        cipher.setSecretKey(secretKey);
        String result = null;
        try {
            result = new String(cipher.decrypt(dataString.getBytes()), STR_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 鐢熸垚鎽樿
     * 
     * @param dataString
     *            闇�瑕佺敓鎴愭憳瑕佺殑鍘熷鏁版嵁
     * @param algorithm
     *            鎽樿绠楁硶锛屽彲浠ヤ负绌猴紝榛樿涓篗D5绠楁硶
     * @return 鎽樿鏁版嵁
     */
    public static String digest(String dataString, String algorithm) {
        if (dataString == null) {
            throw new IllegalArgumentException("Digest dataString is null!");
        }
        Digestor digestor = new Digestor();
        digestor.setAlgorithm(algorithm);
        String result = null;
        try {
            result = new String(digestor.digest(dataString.getBytes()), STR_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 楠岃瘉鎽樿
     * 
     * @param dataString
     *            鐢熸垚鎽樿鐨勫師濮嬫暟鎹�
     * @param digestedString
     *            鐢熸垚鐨勬憳瑕佹暟鎹�
     * @param algorithm
     *            鎽樿绠楁硶锛屽彲浠ヤ负绌猴紝榛樿涓篗D5绠楁硶
     * @return true锛氶獙璇侀�氳繃
     */
    public static boolean verifyDigest(String dataString, String digestedString, String algorithm) {
        if (dataString == null) {
            throw new IllegalArgumentException("Very Digest dataString is null!");
        }
        if (digestedString == null) {
            throw new IllegalArgumentException("Very Digest digestedString is null!");
        }
        Digestor digestor = new Digestor();
        digestor.setAlgorithm(algorithm);
        return digestor.verify(dataString.getBytes(), digestedString.getBytes());
    }

    /**
     * 鐢熸垚鏁板瓧绛惧悕
     * 
     * @param dataString
     *            闇�瑕佺敓鎴愭暟瀛楃鍚嶇殑鍘熷鏁版嵁
     * @param algorithm
     *            鏁板瓧绛惧悕绠楁硶锛屽彲浠ヤ负绌猴紝榛樿涓篋SA绠楁硶
     * @return 鏁板瓧绛惧悕
     */
    public static String sign(String dataString, String algorithm) {
        if (dataString == null) {
            throw new IllegalArgumentException("Signaturer dataString is null!");
        }
        Signaturer signaturer = new Signaturer();
        signaturer.setAlgorithm(algorithm);
        String result = null;
        try {
            result = new String(signaturer.sign(dataString.getBytes()), STR_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 楠岃瘉鏁板瓧绛惧悕
     * 
     * @param dataString
     *            鐢熸垚鏁板瓧绛惧悕鐨勫師濮嬫暟鎹�
     * @param signedString
     *            鐢熸垚鐨勬暟瀛楃鍚嶆暟鎹�
     * @param algorithm
     *            鏁板瓧绛惧悕绠楁硶锛屽彲浠ヤ负绌猴紝榛樿涓篋SA绠楁硶
     * @return true锛氶獙璇侀�氳繃
     */
    public static boolean verifySign(String dataString, String signedString, String algorithm) {
        if (dataString == null) {
            throw new IllegalArgumentException("Signaturer dataString is null!");
        }
        if (signedString == null) {
            throw new IllegalArgumentException("Signaturer signedString is null!");
        }
        Signaturer signaturer = new Signaturer();
        signaturer.setAlgorithm(algorithm);
        return signaturer.verify(dataString.getBytes(), signedString.getBytes());
    }
}