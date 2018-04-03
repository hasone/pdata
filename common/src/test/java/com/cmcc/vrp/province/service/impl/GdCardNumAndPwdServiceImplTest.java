package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcCardInfoMapper;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 广东营销卡卡号卡密服务
 * <p>
 * Created by sunyiwei on 2016/11/30.
 */
public class GdCardNumAndPwdServiceImplTest extends AbstractCardNumAndServiceImplTest {
    @InjectMocks
    GdCardNumAndPwdServiceImpl gdCardNumAndPwdService = new GdCardNumAndPwdServiceImpl();

    @Mock
    MdrcCardInfoMapper mdrcCardInfoMapper;

    /**
     * 测试卡号规则
     *
     * @throws Exception
     */
    @Test
    public void testCheckCardNum() throws Exception {
        final String emptyStr = "";
        final String nullStr = null;
        final String errorLengthStr = "123456789012345"; //expected 16, actual 15
        final String errorPattern = "123456789012345z"; //expected digital,actual digital+char
        final String correctPattern = "1234567890123456"; //correct

        assertFalse(gdCardNumAndPwdService.checkCardNum(emptyStr));
        assertFalse(gdCardNumAndPwdService.checkCardNum(nullStr));
        assertFalse(gdCardNumAndPwdService.checkCardNum(errorLengthStr));
        assertFalse(gdCardNumAndPwdService.checkCardNum(errorPattern));
        assertTrue(gdCardNumAndPwdService.checkCardNum(correctPattern));
    }

    @Override
    protected JdbcBasedCardNumAndPwdServiceImpl getCardNumAndPwdService() {
        return gdCardNumAndPwdService;
    }

    /**
     * 测试卡密规则
     *
     * @throws Exception
     */
    @Test
    public void testCheckPassword() throws Exception {
        final String emptyStr = "";
        final String nullStr = null;
        final String errorLengthStr = "1234567"; //expected 8, actual 7
        final String errorPattern = "1234567z"; //expected digital,actual digital+char
        final String correctPattern = "12345678"; //correct

        assertFalse(gdCardNumAndPwdService.checkPassword(emptyStr));
        assertFalse(gdCardNumAndPwdService.checkPassword(nullStr));
        assertFalse(gdCardNumAndPwdService.checkPassword(errorLengthStr));
        assertFalse(gdCardNumAndPwdService.checkPassword(errorPattern));
        assertTrue(gdCardNumAndPwdService.checkPassword(correctPattern));
    }

    /**
     * 校验卡号长度
     *
     * @throws Exception
     */
    @Test
    public void testGetCardNumLength() throws Exception {
        assertTrue(16 == gdCardNumAndPwdService.getCardNumLength());
    }

    /**
     * 校验密码长度
     *
     * @throws Exception
     */
    @Test
    public void testGetPasswordLength() throws Exception {
        assertTrue(8 == gdCardNumAndPwdService.getPasswordLength());
    }

    /**
     * 校验年份
     *
     * @throws Exception
     */
    @Test
    public void testGetIntYear() throws Exception {
        GdCardNumAndPwdServiceImpl spy = Mockito.spy(gdCardNumAndPwdService);
        doReturn(false).doReturn(true).when(spy).checkCardNum(anyString());

        //checkCardNum返回false
        assertTrue(0 == spy.getIntYear("434sdaf"));

        ////checkCardNum返回true
        String cardNum = randCardNum();
        assertTrue(spy.getIntYear(cardNum) == NumberUtils.toInt(cardNum.substring(6, 8)));

        verify(spy, times(2)).getIntYear(anyString());
    }

    /**
     * 测试卡号校验规则
     *
     * @throws Exception
     */
    @Test
    public void testValidate() throws Exception {

    }

    /**
     * 根据卡号获取相应的营销卡信息
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        assertNull(gdCardNumAndPwdService.get(null));
        assertNull(gdCardNumAndPwdService.get(""));

        GdCardNumAndPwdServiceImpl spy = spy(gdCardNumAndPwdService);
        doReturn(0).doReturn(16).when(spy).getIntYear(anyString());
        when(mdrcCardInfoMapper.selectByCardNumber(anyInt(), anyString()))
            .thenReturn(null).thenReturn(new MdrcCardInfo());

        assertNull(spy.get("fdsfs"));
        assertNull(spy.get("fdsfs"));
        assertNotNull(spy.get("fdsfs"));

        verify(spy, times(3)).getIntYear(anyString());
        verify(mdrcCardInfoMapper, times(2)).selectByCardNumber(anyInt(), anyString());
    }


    /**
     * 测试计算卡号UT
     *
     * @throws Exception
     */
    @Test
    public void testCal() throws Exception {
        assertTrue(StringUtils.isBlank(gdCardNumAndPwdService.cal("fdfsa", 50)));

        String cardNum = randCardNum();
        final int delta = new Random().nextInt(10000);

        String endCardNum = gdCardNumAndPwdService.cal(cardNum, delta);

        long beginNum = NumberUtils.toLong(cardNum.substring(10));
        long endNum = NumberUtils.toLong(endCardNum.substring(10));

        assertTrue(beginNum + delta == endNum);
        assertTrue(cardNum.substring(0, 10).equals(endCardNum.substring(0, 10)));
    }

    /**
     * 测试生成卡号
     *
     * @throws Exception
     */
    @Test
    public void testGeneratCardNums() throws Exception {
        assertNull(gdCardNumAndPwdService.generatCardNums(nullSerialNum()));
        assertNull(gdCardNumAndPwdService.generatCardNums(invalidAmount()));

        final int COUNT = new Random().nextInt(100) + 100;
        List<String> cardNums = gdCardNumAndPwdService.generatCardNums(buildConfig(COUNT));
        assertNotNull(cardNums);
        assertTrue(COUNT == cardNums.size());

        for (int i = 0; i < COUNT; i++) {
            assertTrue(gdCardNumAndPwdService.checkCardNum(cardNums.get(i)));
        }
    }


}