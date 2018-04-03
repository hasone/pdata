package com.cmcc.vrp.boss.zhuowang;

import com.cmcc.vrp.boss.BossOperationResult;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by sunyiwei on 2016/9/19.
 */
@Ignore
public class ZWBossServiceImplTest {
    @Autowired
    ZWBossServiceImpl zwBossService;

    private static String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }

    /**
     * 测试字符串长度
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testLength() throws Exception {
        System.out.println("ZJHYovpgrsct2016101711020200000".length());
    }

    /**
     * 测试卓望BOSS充值
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testCharge() throws Exception {
        final String mobile = "18867102100";
        final String serialNum = "ZJHY" + randStr(26);
        BossOperationResult bor = zwBossService.charge(10L, 24L, mobile, serialNum, null);
        assertTrue(bor.isSuccess());
    }
}