package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AccountRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AccountRecordMapper
 */
public interface AccountRecordMapper {
    /**
     * @param record
     * @Title: insert
     */
    int insert(AccountRecord record);

    /**
     * @param records
     * @Title: batchInsert
     */
    int batchInsert(@Param("records") List<AccountRecord> records);

    List<AccountRecord> getByOwnerId(Long ownerId);

    List<AccountRecord> getByEntId(Long entId);

    /**
     * 根据系统流水号获取相应的出账记录
     *
     * @param pltSn 系统流水号
     * @return 出账记录
     */
    AccountRecord getOutgoingRecordByPltSn(String pltSn);

    /**
     * @param serialNum
     * @param enterId
     * @Title: selectBySerialNumAndEnterId
     */
    List<AccountRecord> selectBySerialNumAndEnterId(@Param("serialNum") String serialNum, @Param("enterId") Long enterId);
}