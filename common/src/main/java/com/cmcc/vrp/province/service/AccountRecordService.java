package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AccountRecord;

import java.util.List;

/**
 * 帐户记录
 * <p>
 * Created by sunyiwei on 2015/11/13.
 */
public interface AccountRecordService {
    /**
     * 批量插入帐户记录
     *
     * @param records 记录列表
     * @return 成功返回true, 否则false
     */
    boolean batchInsert(List<AccountRecord> records);

    /**
     * 插入帐户记录
     *
     * @param accountRecord 帐户记录对象
     * @return 插入成功返回true, 否则false
     */
    boolean create(AccountRecord accountRecord);

    /**
     * 根据流水号和企业ID找到账户变更记录
     *
     * @param serialNum
     * @param enterId
     * @return
     * @date 2016年6月30日
     * @author wujiamin
     */
    List<AccountRecord> selectBySerialNumAndEnterId(String serialNum, Long enterId);

    /**
     * 根据系统流水号获取相应的出账记录
     *
     * @param pltSn 平台流水号
     * @return 出账记录
     */
    AccountRecord getOutgoingRecordByPltSn(String pltSn);

}
