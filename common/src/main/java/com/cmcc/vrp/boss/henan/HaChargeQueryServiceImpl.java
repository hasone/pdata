package com.cmcc.vrp.boss.henan;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.henan.Util.HaAES256;
import com.cmcc.vrp.boss.henan.model.HaConstats;
import com.cmcc.vrp.boss.henan.model.HaQueryStatusResp;
import com.cmcc.vrp.boss.henan.model.HaStatusResult;
import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by lilin on 2016/9/26.
 */
public abstract class HaChargeQueryServiceImpl implements BaseBossQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(HaChargeQueryServiceImpl.class);

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    @Qualifier("HaQueryServiceImpl")
    private HaQueryBossService haQueryBossService;

    @Autowired
    private SerialNumService serialNumService;

    @Autowired
    private Gson gson;

    protected abstract String getAppkey();


    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("查询河南BOSS侧充值状态开始");
        ChargeRecord record;
        String phone = null;
        String bossReqNum = null;
        SerialNum serialNum;
        if (StringUtils.isBlank(systemNum)
                || (record = recordService.getRecordBySN(systemNum)) == null
                || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
                || StringUtils.isBlank(bossReqNum = serialNum.getBossReqSerialNum())
                || StringUtils.isBlank(phone = record.getPhone())) {
            LOGGER.info("查询河南BOSS侧充值状态失败,参数缺失");
            return BossQueryResult.FAILD;
        }
        HaQueryStatusResp resp = haQueryBossService.queryMemberStatus(phone, bossReqNum);
        String msg;
        String result;
        HaStatusResult statusResult = null;
        if (resp != null
                && resp.getRespCode().equals(HaConstats.QueryStatusResp.success.getCode())
                && StringUtils.isNotBlank(result = resp.getResult())
                && StringUtils.isNotBlank(result = HaAES256.decryption(result, getAppkey()))
                && (statusResult = gson.fromJson(result, HaStatusResult.class)) != null
                && StringUtils.isNotBlank(msg = statusResult.getDEAL_RESULT())) {
            if (msg.equals(HaConstats.QueryStatus.INIT.getMsg())) {
                LOGGER.info("查询河南BOSS侧充值为初始化状态");
                return BossQueryResult.PROCESSING;
            } else if (msg.equals(HaConstats.QueryStatus.SUCCESS.getMsg())) {
                LOGGER.info("查询河南BOSS侧充值为成功状态");
                return BossQueryResult.SUCCESS;
            }
        }
        if (statusResult != null) {
            LOGGER.info("查询河南BOSS侧充值为失败状态,失败信息:{}", statusResult.getDEAL_RESULT());
        }
        return BossQueryResult.FAILD;
    }
    
    @Override
    public BossOperationResult queryStatusAndMsg(final String systemNum) {
        final BossQueryResult queryResult = queryStatus(systemNum);
               
        return new BossOperationResult(){

            @Override
            public String getResultCode() {        
                return queryResult.getCode();
            }

            @Override
            public boolean isSuccess() {
                return queryResult.getCode().equals(BossQueryResult.SUCCESS.getCode());
            }

            @Override
            public boolean isAsync() {
                return false;
            }

            @Override
            public String getResultDesc() {
                return queryResult.getMsg();
            }

            @Override
            public Object getOperationResult() {
                return null;
            }

            @Override
            public boolean isNeedQuery() {
                return false;
            }

            @Override
            public String getFingerPrint() {
                return null;
            }

            @Override
            public String getSystemNum() {
                return systemNum;
            }

            @Override
            public Long getEntId() {
                return null;
            }
            
        };
    }
}
