package com.cmcc.vrp.boss.hunan;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sunyiwei on 2016/9/21.
 */
@Ignore
public class HNSpecialBossServiceImplTest {
    @Autowired
    HNSpecialBossServiceImpl hnSpecialBossServiceImpl;

    @Ignore
    @Test
    public void testCharge() throws Exception {
        final Long entId = 34L;
        final Long splId = 20L;
        final String[] mobiles = new String[]{
            "13548754201",
            "15180904310",
            "15874444711",
            "15200620988",
            "15274714981"
        };

        for (String mobile : mobiles) {
            final String serialNum = SerialNumGenerator.buildBossReqSerialNum(16);

            BossOperationResult bor = hnSpecialBossServiceImpl.charge(entId, splId, mobile, serialNum, null);
//            assertTrue(bor.isSuccess());
        }
    }
}