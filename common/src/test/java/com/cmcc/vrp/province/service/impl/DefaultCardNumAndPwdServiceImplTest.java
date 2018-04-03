package com.cmcc.vrp.province.service.impl;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MdrcCardInfoMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.service.GlobalConfigService;


/**
 * 默认的卡号卡密规则UT
 * <p>
 * Created by sunyiwei on 2016/12/1.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultCardNumAndPwdServiceImplTest extends AbstractCardNumAndServiceImplTest {
    @InjectMocks
    private DefaultCardNumAndPwdServiceImpl defaultCardNumAndPwdService =
        new DefaultCardNumAndPwdServiceImpl();
    
    @Mock
    private GlobalConfigService globalConfigService;
    
    @Mock
    MdrcCardmakerService mdrcCardmakerService;
    
    @Mock
    MdrcCardInfoMapper mdrcCardInfoMapper;
    
   
    /**
     * 校验卡号
     *
     * @throws Exception
     */
    @Test
    public void testCheckCardNum() throws Exception {
        assertFalse(defaultCardNumAndPwdService.checkCardNum(null));
        assertFalse(defaultCardNumAndPwdService.checkCardNum(""));
        assertFalse(defaultCardNumAndPwdService.checkCardNum(StringUtils.repeat("0", 26)));
        assertFalse(defaultCardNumAndPwdService.checkCardNum(StringUtils.repeat("0", 26) + "r"));
        assertTrue(defaultCardNumAndPwdService.checkCardNum(StringUtils.repeat("0", 27)));
    }

    /**
     * 校验卡密
     *
     * @throws Exception
     */
    @Test
    public void testCheckPassword() throws Exception {
        assertFalse(defaultCardNumAndPwdService.checkPassword(null));
        assertFalse(defaultCardNumAndPwdService.checkPassword(""));
        assertFalse(defaultCardNumAndPwdService.checkPassword(StringUtils.repeat("0", 13)));
        assertFalse(defaultCardNumAndPwdService.checkPassword(StringUtils.repeat("0", 13) + "r"));
        assertTrue(defaultCardNumAndPwdService.checkPassword(StringUtils.repeat("0", 14)));
    }

    /**
     * 校验卡号长度
     *
     * @throws Exception
     */
    @Test
    public void testGetCardNumLength() throws Exception {
        assertTrue(27 == defaultCardNumAndPwdService.getCardNumLength());
    }

    /**
     * 校验卡密长度
     *
     * @throws Exception
     */
    @Test
    public void testGetPasswordLength() throws Exception {
        assertTrue(14 == defaultCardNumAndPwdService.getPasswordLength());
    }

    /**
     * 获取年份
     *
     * @throws Exception
     */
    @Test
    public void testGetIntYear() throws Exception {
        DefaultCardNumAndPwdServiceImpl spy
            = Mockito.spy(defaultCardNumAndPwdService);
        doReturn(false).doReturn(true).when(spy).checkCardNum(anyString());

        //checkCardNum返回false
        assertTrue(0 == spy.getIntYear("434sdaf"));

        ////checkCardNum返回true
        String cardNum = randCardNum();
        assertTrue(spy.getIntYear(cardNum) == NumberUtils.toInt(cardNum.substring(5, 7)));

        verify(spy, times(2)).getIntYear(anyString());
    }

    /**
     * 计算卡号UT
     *
     * @throws Exception
     */
    @Test
    public void testCal() throws Exception {
        assertTrue(StringUtils.isBlank(defaultCardNumAndPwdService.cal("fdfsa", 50)));

        String cardNum = randCardNum();
        final int delta = new Random().nextInt(10000);

        String endCardNum = defaultCardNumAndPwdService.cal(cardNum, delta);

        long beginNum = NumberUtils.toLong(cardNum.substring(18));
        long endNum = NumberUtils.toLong(endCardNum.substring(18));

        assertTrue(beginNum + delta == endNum);
        assertTrue(cardNum.substring(0, 18).equals(endCardNum.substring(0, 18)));
    }
    
    @Test
    public void testGeneratePasswords(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("1");
        Mockito.when(globalConfigService.updateValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        defaultCardNumAndPwdService.generatePasswords(new ArrayList());

        List<MdrcCardInfo> list = new ArrayList<MdrcCardInfo>();
        MdrcCardInfo info = new MdrcCardInfo();
        info.setClearPsw("123456");
        list.add(info);
        defaultCardNumAndPwdService.generatePasswords(list);
        
        
        Mockito.when(globalConfigService.updateValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        defaultCardNumAndPwdService.generatePasswords(list);
    }
    
    @Test
    public void testGeneratCardNums(){
        assertNull(defaultCardNumAndPwdService.generatCardNums(null));
        
        Mockito.when(mdrcCardmakerService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        assertNull(defaultCardNumAndPwdService.generatCardNums(new MdrcBatchConfig()));
        
        MdrcBatchConfig config = new MdrcBatchConfig();
        config.setSerialNumber(1);
        config.setAmount(1L);
        
        MdrcCardmaker cardMarker = new MdrcCardmaker();
        cardMarker.setSerialNumber("11");
        Mockito.when(mdrcCardmakerService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(cardMarker);
        defaultCardNumAndPwdService.generatCardNums(config);
        
        config.setThisYear("2016");
        config.setProvinceCode("00");
        defaultCardNumAndPwdService.generatCardNums(config);
        
        config.setThisYear("1");
        config.setProvinceCode("0");
        cardMarker.setSerialNumber("1");
        Mockito.when(mdrcCardmakerService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(cardMarker);
        defaultCardNumAndPwdService.generatCardNums(config);
        
    }
    /**
     * 
     */
    @Test
    public void testGetBycreateTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getBycreateTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getBycreateTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getBycreateTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getBycreateTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getBycreateTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByStoredTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByStoredTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByStoredTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByStoredTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByStoredTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByStoredTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByBoundTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByBoundTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByBoundTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByBoundTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByBoundTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByBoundTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByActivatedTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByActivatedTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByActivatedTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByActivatedTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByActivatedTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByActivatedTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByUsedTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByUsedTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByUsedTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByUsedTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByUsedTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByUsedTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByLockedTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByLockedTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByLockedTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByLockedTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByLockedTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByLockedTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByDeactivateTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByDeactivateTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByDeactivateTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByDeactivateTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByDeactivateTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByDeactivateTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetByUnlockTime(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getByUnlockTime(begin, null));
        assertNull(defaultCardNumAndPwdService.getByUnlockTime(null, begin));
        assertNull(defaultCardNumAndPwdService.getByUnlockTime(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByUnlockTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getByUnlockTime(begin, end));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetBySerialNum(){
        assertNull(defaultCardNumAndPwdService.getBySerialNum(1l, null));
        assertNull(defaultCardNumAndPwdService.getBySerialNum(null, "111"));
        Mockito.when(mdrcCardInfoMapper.getBySerialNum(any(Long.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getBySerialNum(1l, "2016"));
        
    }
    /**
     * 
     */
    @Test
    public void testGetBill(){
        DateTime begin = new DateTime("2017-01-16T10:24:02.769+08:00");
        DateTime end = new DateTime("2017-01-16T10:24:03.769+08:00");
        assertNull(defaultCardNumAndPwdService.getBill(begin, null));
        assertNull(defaultCardNumAndPwdService.getBill(null, begin));
        assertNull(defaultCardNumAndPwdService.getBill(end, begin));
        Mockito.when(mdrcCardInfoMapper.getByUsedTime(any(Date.class), any(Date.class), any(Integer.class)))
            .thenReturn(null);
        assertNull(defaultCardNumAndPwdService.getBill(begin, end));
        
    }

    @Override
    protected JdbcBasedCardNumAndPwdServiceImpl getCardNumAndPwdService() {
        return defaultCardNumAndPwdService;
    }
}