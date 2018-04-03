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

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 鍔犺В瀵嗗疄鐜扮被
 * 
 * @author wuyuhou
 * 
 */
public class Cipher extends AbstractCryptor {

    public final static String AES_ALGORITHM = "AES";

    public final static String BLOWFISH_ALGORITHM = "Blowfish";

    public final static String DES_ALGORITHM = "DES";

    public final static String DESEDE_ALGORITHM = "DESede";

    private final static String DEFAULT_ALGORITHM = DES_ALGORITHM;

    private static ConcurrentHashMap<String, SecretKey> secretKeyMap = new ConcurrentHashMap<String, SecretKey>();

    // 瀵嗛挜
    private SecretKey secretKey = null;

    private boolean isNeedBase64Coder = true;

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isNeedBase64Coder() {
        return isNeedBase64Coder;
    }

    public void setNeedBase64Coder(boolean isBase64Coder) {
        this.isNeedBase64Coder = isBase64Coder;
    }

    // 鍒濆鍖�
    private void init() throws Exception {
        if (getAlgorithm() == null || getAlgorithm().trim().length() == 0) {
            setAlgorithm(DEFAULT_ALGORITHM);
        }
        if (getSecretKey() == null) {
            if (secretKeyMap.get(getAlgorithm()) == null) {
                synchronized (Cipher.class) {
                    if (secretKeyMap.get(getAlgorithm()) == null) {
                        KeyGenerator keygen = KeyGenerator.getInstance(getAlgorithm());
                        keygen.init(new SecureRandom());
                        secretKeyMap.put(getAlgorithm(), keygen.generateKey());
                    }
                }
            }
            setSecretKey(secretKeyMap.get(getAlgorithm()));
        }
    }

    /**
     * 鍔犲瘑
     * 
     * @param dataBytes
     *            闇�瑕佸姞瀵嗙殑鍘熷鏁版嵁
     * @return 鍔犲瘑鍚庣殑鏁版嵁
     */
    public byte[] encrypt(byte[] dataBytes) {
        if (dataBytes == null || dataBytes.length == 0) {
            throw new IllegalArgumentException("dataBytes is null!");
        }
        try {
            init();
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(getAlgorithm());
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] result = cipher.doFinal(dataBytes);
            if (isNeedBase64Coder) {
                result = Base64Util.encode(result);
            }
            return result;
        } catch (Throwable t) {
            throw new RuntimeException("encrypt error!", t);
        }
    }

    /**
     * 瑙ｅ瘑
     * 
     * @param dataBytes
     *            闇�瑕佽В瀵嗙殑鏁版嵁
     * @return 鍘熷鏁版嵁
     * 
     */
    public byte[] decrypt(byte[] dataBytes) {
        if (dataBytes == null || dataBytes.length == 0) {
            throw new IllegalArgumentException("dataBytes is null!");
        }
        try {
            init();
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(getAlgorithm());
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getSecretKey());
            if (isNeedBase64Coder) {
                dataBytes = Base64Util.decode(dataBytes);
            }
            return cipher.doFinal(dataBytes);
        } catch (Throwable t) {
            throw new RuntimeException("decrypt error!", t);
        }
    }
}