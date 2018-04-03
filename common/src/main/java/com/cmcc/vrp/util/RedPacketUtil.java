package com.cmcc.vrp.util;

import org.apache.log4j.Logger;
import org.springframework.util.NumberUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;


/**
 * @ClassName: RedPacketUtil
 * @Description: 用于生成红包的校验码
 * @author: sunyiwei
 * @date:2015年2月3日 下午4:28:44
 */
public class RedPacketUtil {
    private static final Logger logger = Logger.getLogger(RedPacketUtil.class);

    /**
     * @param redpacketId
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws
     * @Title:getRedpacketUrl
     * @Description: 生成红包的加密地址，加密方式如下：
     * redPacketId=des(id)
     * @author: sunyiwei
     */
    public static String getEncodeId(Long redpacketId) {
        String encodeId;
        try {
            encodeId = Des.encrypt(redpacketId.toString());
        } catch (Exception e) {
            logger.error(e.toString());
            encodeId = null;
        }

        return encodeId;
    }

    /**
     * @param encodeRedpacketId
     * @return
     * @throws
     * @Title:getRedpacketId
     * @Description: 获得解密后的ID
     * @author: sunyiwei
     */
    public static Long getDecodedId(String encodeRedpacketId) {
        String szRedpacketId = null;
        try {
            szRedpacketId = Des.decrypt(encodeRedpacketId);
        } catch (Exception e) {
            return null;
        }

        return NumberUtils.parseNumber(szRedpacketId, Long.class);
    }
}
