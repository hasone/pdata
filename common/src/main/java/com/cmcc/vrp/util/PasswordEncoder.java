package com.cmcc.vrp.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * 哈希盐密码处理器
 * <p>
 * Created by sunyiwei on 2016/8/16.
 */
@Component
public class PasswordEncoder {
    /**
     * 加密密码
     *
     * @param rawPassword 明文密码
     * @param salt        哈希盐
     * @return 加密后的密文
     */
    public String encode(String rawPassword, String salt) {
        return DigestUtils.sha256Hex(rawPassword + salt);
    }

    /**
     * 校验密码
     *
     * @param rawPassword 明文密码
     * @param encodedPwd  加密密文
     * @param salt        哈希盐
     * @return 校验通过返回true， 否则false
     */
    public boolean matches(String rawPassword, String encodedPwd, String salt) {
        return DigestUtils.sha256Hex(rawPassword + salt).equals(encodedPwd);
    }
}
