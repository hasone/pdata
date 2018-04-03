package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.SmsRecord;

/**
 * @Title:SmsRecordMapper
 * @Description:
 * */
public interface SmsRecordMapper {
    /**
     * @Title:get
     * @Description:
     * */
    SmsRecord get(String mobile);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(SmsRecord record);

    /**
     * @Title:delete
     * @Description:
     * */
    int delete(String mobile);

    /**
     * @Title:update
     * @Description:
     * */
    int update(SmsRecord record);
}