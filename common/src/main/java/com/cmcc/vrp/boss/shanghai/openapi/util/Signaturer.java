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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 鏁板瓧绛惧悕瀹炵幇绫�
 * 
 * @author wuyuhou
 * 
 */
public class Signaturer extends AbstractCryptor {

    public final static String DSA_ALGORITHM = "DSA";

    public final static String SHA1_DSA_ALGORITHM = "SHA1WithDSA";

    private final static String DEFAULT_ALGORITHM = SHA1_DSA_ALGORITHM;

    // 绉侀挜锛岀敤浜庣敓鎴愮鍚�
    private PrivateKey privateKey = null;

    // 鍏挜锛岀敤浜庨獙璇佺鍚�
    private PublicKey publicKey = null;

    private static ConcurrentHashMap<String, PrivateKey> privateKeyMap = new ConcurrentHashMap<String, PrivateKey>();

    private static ConcurrentHashMap<String, PublicKey> publicKeyMap = new ConcurrentHashMap<String, PublicKey>();

    private boolean isNeedBase64Coder = true;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    // 璁剧疆绉侀挜
    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    // 璁剧疆鍏挜
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public boolean isNeedBase64Coder() {
        return isNeedBase64Coder;
    }

    public void setNeedBase64Coder(boolean isBase64Coder) {
        this.isNeedBase64Coder = isBase64Coder;
    }

    // 鍒濆鍖�
    private void init() throws NoSuchAlgorithmException {
        if (getAlgorithm() == null || getAlgorithm().trim().length() == 0) {
            setAlgorithm(DEFAULT_ALGORITHM);
        }
        if (getPrivateKey() == null || getPublicKey() == null) {
            if (privateKeyMap.get(getAlgorithm()) == null || publicKeyMap.get(getAlgorithm()) == null) {
                synchronized (Signaturer.class) {
                    if (privateKeyMap.get(getAlgorithm()) == null || publicKeyMap.get(getAlgorithm()) == null) {
                        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(DSA_ALGORITHM);
                        KeyPair keyPair = keyGenerator.generateKeyPair();
                        // 蹇呴』鎴愬鐢熸垚锛屽惁鍒欓獙璇佸嚭閿�
                        privateKeyMap.put(getAlgorithm(), keyPair.getPrivate());
                        publicKeyMap.put(getAlgorithm(), keyPair.getPublic());
                    }
                }
            }
            setPrivateKey(privateKeyMap.get(getAlgorithm()));
            setPublicKey(publicKeyMap.get(getAlgorithm()));
        }
    }

    /**
     * 鐢熸垚鏁板瓧绛惧悕
     * 
     * @param dataBytes
     *            闇�瑕佺敓鎴愭暟瀛楃鍚嶇殑鍘熷鏁版嵁
     * @return 鏁板瓧绛惧悕鏁版嵁
     */
    public byte[] sign(byte[] dataBytes) {
        if (dataBytes == null || dataBytes.length == 0) {
            throw new IllegalArgumentException("dataBytes is null!");
        }
        try {
            init();
            Signature signature = Signature.getInstance(getAlgorithm());
            signature.initSign(getPrivateKey());
            signature.update(dataBytes);
            byte[] result = signature.sign();
            if (isNeedBase64Coder) {
                result = Base64Util.encode(result);
            }
            return result;
        } catch (Throwable t) {
            throw new RuntimeException("sign error!", t);
        }
    }

    /**
     * 楠岃瘉鏁板瓧绛惧悕
     * 
     * @param dataBytes
     *            闇�瑕佺敓鎴愭暟瀛楃鍚嶇殑鍘熷鏁版嵁
     * @param signedBytes
     *            闇�瑕侀獙璇佺殑鏁板瓧绛惧悕鏁版嵁
     * @return true锛氭槸鐩稿悓鐨勬暟瀛楃鍚�
     */
    public boolean verify(byte[] dataBytes, byte[] signedBytes) {
        if (dataBytes == null || dataBytes.length == 0) {
            throw new IllegalArgumentException("dataBytes is null!");
        }
        if (signedBytes == null || signedBytes.length == 0) {
            throw new IllegalArgumentException("signedBytes is null!");
        }
        try {
            init();
            if (isNeedBase64Coder) {
                signedBytes = Base64Util.decode(signedBytes);
            }
            Signature signature = Signature.getInstance(getAlgorithm());
            signature.initVerify(getPublicKey());
            signature.update(dataBytes);
            return signature.verify(signedBytes);
        } catch (Throwable t) {
            throw new RuntimeException("verify signature error!", t);
        }
    }
}