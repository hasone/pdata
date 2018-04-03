package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.util.PasswordEncoder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

/**
 * 公用的卡号卡密测试类
 * <p>
 * Created by sunyiwei on 2016/12/1.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractCardNumAndServiceImplTest {
    @Mock
    PasswordEncoder passwordEncoder;

    protected MdrcBatchConfig buildConfig() {
        return buildConfig(new Random().nextInt(100) + 10);
    }

    protected MdrcBatchConfig buildConfig(int count) {
        Random r = new Random();
        MdrcBatchConfig config = new MdrcBatchConfig();
        config.setAmount((long) count);
        config.setSerialNumber(r.nextInt(100));

        return config;
    }

    protected String randCardNum() {
        final int length = getCardNumAndPwdService().getCardNumLength();
        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (r.nextInt(10) + '0'));
        }

        return stringBuilder.toString();
    }

    protected MdrcBatchConfig nullSerialNum() {
        MdrcBatchConfig config = buildConfig();
        config.setSerialNumber(null);

        return config;
    }

    protected MdrcBatchConfig invalidAmount() {
        MdrcBatchConfig config = buildConfig();
        config.setAmount(0L);

        return config;
    }


    protected abstract JdbcBasedCardNumAndPwdServiceImpl getCardNumAndPwdService();
}
