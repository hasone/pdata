/**
 * @Title: InterfaceRecordServiceImpl.java
 * @Package com.cmcc.vrp.province.service.impl
 * @author: qihang
 * @date: 2015年11月4日 下午5:34:54
 * @version V1.0
 */
package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.InterfaceRecordMapper;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.google.gson.Gson;

/**
 * @ClassName: InterfaceRecordServiceImpl
 * @Description: TODO
 * @author: qihang
 * @date: 2015年11月4日 下午5:34:54
 */
@Service("interfaceRecordService")
public class InterfaceRecordServiceImpl extends AbstractCacheSupport implements
        InterfaceRecordService {

    private static Logger logger = LoggerFactory
            .getLogger(InterfaceRecordServiceImpl.class);
    private final String PREFIX = "interface.record.";

    @Autowired
    InterfaceRecordMapper interfaceRecordMapper;


    /**
     * 插入记录
     *
     * @param record
     * @return
     */
    @Override
    public boolean insert(InterfaceRecord record) {
        InterfaceRecord tmp = get(record.getEnterpriseCode(),
                record.getSerialNum(), null);
        if (tmp != null) {
            logger.error("订单已存在！findExistRecord");
            return false;
        }

        boolean result = interfaceRecordMapper.insert(record) > 0;
        logger.info("interfaceRecordMapper.insert result = " + result);

        // 插入记录缓存
        addCache(record);

        return result;
    }

    /**
     * 查找已有的记录
     *
     * @param id
     * @return
     */
    @Override
    public InterfaceRecord get(Long id) {
        if (id == null || !cacheService.sExist(getKeySetKey(), getKey(id))) { // set中不存在
            // 直接返回了
            return null;
        }

        InterfaceRecord interfaceRecord = null;
        String valueStr = null;
        if (StringUtils
                .isBlank(valueStr = cacheService.get(String.valueOf(id)))) { // 缓存中没有，尝试从数据库中捞取
            interfaceRecord = interfaceRecordMapper.selectByPrimaryKey(id);
            if (interfaceRecord != null) { // 从数据库中得到了，设置到缓存中
                addCache(interfaceRecord);
            }

            return interfaceRecord;
        } else { // 缓存命中，直接从缓存中读取
            return new Gson().fromJson(valueStr, InterfaceRecord.class);
        }
    }

    @Override
    public InterfaceRecord get(String enterCode, String serialNum, String phoneNum) {
        String key = getKey(enterCode, serialNum, phoneNum);
        if (!cacheService.sExist(getKeySetKey(), key)) { //根据ID查看记录是否存在
            return null;
        }

        List<InterfaceRecord> interfaceRecords = null;
        String valueStr = null;
        if (StringUtils.isBlank(valueStr = cacheService.get(String.valueOf(key)))) { //缓存中没有，尝试从数据库中捞取
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("enterCode", enterCode);
            params.put("serialNum", serialNum);
            params.put("phoneNum", phoneNum);

            interfaceRecords = interfaceRecordMapper.findExistRecord(params);
            if (interfaceRecords != null && interfaceRecords.size() == 1) { //从数据库中得到了，设置到缓存中
                InterfaceRecord record = interfaceRecords.get(0);

                //增加缓存
                addCache(record);

                return record;
            }
        }

        if (StringUtils.isNotBlank(valueStr)) {
            return new Gson().fromJson(valueStr, InterfaceRecord.class);
        }

        return null;
    }

    /**
     * 更新充值状态
     *
     * @param id
     * @param status
     * @param errMsg
     * @return
     */
    @Override
    public boolean updateChargeStatus(Long id, ChargeRecordStatus status, String errMsg) {
        if (id == null || status == null) {
            logger.error("充值状态不能为NULL.");
            return false;
        }

        InterfaceRecord record = get(id);
        if (record == null) {
            logger.error("接口记录为空, 记录ID为{}.", id);
            return false;
        }

        record.setStatus(status.getCode());
        record.setErrMsg(errMsg);
        record.setChargeTime(new Date());
        record.setStatusCode(status.getStatusCode());

        //更新缓存后再更新数据库
        if (cacheService.update(getKeys(record), new Gson().toJson(record))) {
            return interfaceRecordMapper.updateByPrimaryKeySelective(record) > 0;
        }

        logger.error("修改接口记录的缓存时出错，ID为{}，信息为{}.", id, new Gson().toJson(record));
        return false;
    }


    @Override
    public boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errMsg) {
        if (id == null || status == null) {
            logger.error("充值状态不能为NULL.");
            return false;
        }

        InterfaceRecord record = get(id);
        if (record == null) {
            logger.error("接口记录为空, 记录ID为{}.", id);
            return false;
        }

        record.setStatus(status.getCode());
        record.setErrMsg(errMsg);
        record.setChargeTime(new Date());
        record.setStatusCode(status.getStatusCode());

        //更新缓存后再更新数据库
        if (cacheService.update(getKeys(record), new Gson().toJson(record))) {
            return interfaceRecordMapper.updateByPrimaryKeySelective(record) > 0;
        }

        logger.error("修改接口记录的缓存时出错，ID为{}，信息为{}.", id, new Gson().toJson(record));
        return false;
    }

    private String getKey(String entCode, String serialNum, String mobile) {
        return entCode + "." + serialNum + "." + mobile;
    }

    private String getKey(Long id) {
        return String.valueOf(id);
    }

    private List<String> getKeys(InterfaceRecord record) {
        List<String> keys = new LinkedList<String>();

        keys.add(getKey(record.getId()));
        keys.add(getKey(record.getEnterpriseCode(), record.getSerialNum(),
                record.getPhoneNum()));

        return keys;
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    private void addCache(InterfaceRecord record) {
        List<String> keys = getKeys(record);
        cacheService.sadd(getKeySetKey(), keys);
        cacheService.add(getKeys(record), new Gson().toJson(record));
    }

    @Override
    public boolean batchUpdateStatus(List<InterfaceRecord> records) {
        return records != null && interfaceRecordMapper.batchUpdateStatus(records) == records.size();
    }

    /**
     * @Title: updateStatusCode
     * @Description: TODO
     */
    @Override
    public boolean updateStatusCode(Long id, String statusCode) {
        if (id == null) {
            return false;
        }
        return interfaceRecordMapper.updateStatusCode(id, statusCode) == 1;
    }

    @Override
    public List<InterfaceRecord> selectBySerialNum(String serialNum) {        
        return interfaceRecordMapper.selectBySerialNum(serialNum);
    }
}
