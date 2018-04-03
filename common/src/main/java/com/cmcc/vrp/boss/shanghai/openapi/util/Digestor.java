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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 娑堟伅鎽樿瀹炵幇绫�
 * 
 * @author wuyuhou
 * 
 */
public class Digestor extends AbstractCryptor {

    public final static String MD5_ALGORITHM = "MD5";

    public final static String SHA_ALGORITHM = "SHA";

    public final static String SHA1_ALGORITHM = "SHA-1";

    private final static String DEFAULT_ALGORITHM = MD5_ALGORITHM;

    private boolean isNeedBase64Coder = true;

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
    }

    /**
     * 鐢熸垚娑堟伅鎽樿
     * 
     * @param dataBytes
     *            闇�瑕佺敓鎴愭憳瑕佺殑鍘熷鏁版嵁
     * @return 娑堟伅鎽樿鏁版嵁
     */
    public byte[] digest(byte[] dataBytes) {
        if (dataBytes == null || dataBytes.length == 0) {
            throw new IllegalArgumentException("dataBytes is null!");
        }
        try {
            byte[] result = doDigest(dataBytes);
            if (isNeedBase64Coder) {
                result = Base64Util.encode(result);
            }
            return result;
        } catch (Throwable t) {
            throw new RuntimeException("digest error!", t);
        }
    }

    private byte[] doDigest(byte[] dataBytes) throws Throwable {
        init();
        MessageDigest md = MessageDigest.getInstance(getAlgorithm());
        md.update(dataBytes);
        return md.digest();
    }

    /**
     * 楠岃瘉娑堟伅鎽樿鍊�
     * 
     * @param dataBytes
     *            闇�瑕佺敓鎴愭憳瑕佺殑鍘熷鏁版嵁
     * @param digestedBytes
     *            闇�瑕侀獙璇佺殑鎽樿鏁版嵁
     * @return true锛氭槸鐩稿悓鐨勮缁嗘憳瑕�
     */
    public boolean verify(byte[] dataBytes, byte[] digestedBytes) {
        if (dataBytes == null || dataBytes.length == 0) {
            throw new IllegalArgumentException("dataBytes is null!");
        }
        if (digestedBytes == null || digestedBytes.length == 0) {
            throw new IllegalArgumentException("digestedBytes is null!");
        }
        try {
            if (isNeedBase64Coder) {
                digestedBytes = Base64Util.decode(digestedBytes);
            }
            byte[] newDegest = doDigest(dataBytes);
            return MessageDigest.isEqual(newDegest, digestedBytes);
        } catch (Throwable t) {
            throw new RuntimeException("verify digest error!", t);
        }
    }
}