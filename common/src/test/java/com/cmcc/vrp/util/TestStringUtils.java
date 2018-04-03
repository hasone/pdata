/**
 * @Title: TestStringUtils.java
 * @Package com.cmcc.vrp.util
 * @author: sunyiwei
 * @date: 2015年3月11日 下午4:35:03
 * @version V1.0
 */
package com.cmcc.vrp.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @ClassName: TestStringUtils
 * @Description: 工具类的单元测试类
 * @author: sunyiwei
 * @date: 2015年3月11日 下午4:35:03
 *
 */
public class TestStringUtils {

    /**
     * Test method for {@link com.cmcc.vrp.util.StringUtils#isValidPassword(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testIsValidPassword() {
        assertTrue(StringUtils.isValidPassword("sunyiwei"));
        assertTrue(StringUtils.isValidPassword("sunyiwei_"));
        assertTrue(StringUtils.isValidPassword("sunyiwei_24332"));
        assertTrue(StringUtils.isValidPassword("fda_24332_ADFAS"));

        assertFalse(StringUtils.isValidPassword("fda_24332_ADFASjfdlkasjlfdkjal"));
        assertFalse(StringUtils.isValidPassword("fda_243$"));
        assertFalse(StringUtils.isValidPassword("fda_243 "));
    }

}
