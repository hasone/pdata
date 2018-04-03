package com.cmcc.vrp.boss;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by sunyiwei on 2016/7/7.
 */
@Service
public class EmptyBossServiceImpl implements BossService {
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        return new TestBossOperationResult();
    }

    @Override
    public String getFingerPrint() {
        return "test";
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return new TestBossOperationResult();
    }

    private class TestBossOperationResult extends AbstractBossOperationResultImpl {
        Random random = new Random();

        @Override
        public String getResultCode() {
            return "Success";
        }

        @Override
        public boolean isSuccess() {
            return random.nextBoolean(); //随机返回成功或者失败
        }

        @Override
        public String getResultDesc() {
            return "This result is just for TEST. Don't never ever use it in production environment";
        }

        @Override
        public Object getOperationResult() {
            return null;
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public boolean isNeedQuery() {
            return false;
        }
    }
}
