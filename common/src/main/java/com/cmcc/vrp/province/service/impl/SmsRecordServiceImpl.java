/**
 * @Title: SmsRecordServiceImpl.java
 * @Package com.cmcc.vrp.province.service.impl
 * @author: sunyiwei
 * @date: 2015年6月10日 下午6:33:41
 * @version V1.0
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.province.dao.SmsRecordMapper;
import com.cmcc.vrp.province.model.SmsRecord;
import com.cmcc.vrp.province.service.SmsRecordService;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SmsRecordServiceImpl
 * @Description: 短信验证码记录服务
 * @author: sunyiwei
 * @date: 2015年6月10日 下午6:33:41
 *
 */
@Service("smsRecordService")
public class SmsRecordServiceImpl implements SmsRecordService {

    @Autowired
    private SmsRecordMapper mapper;

    /*
     * 获取短信验证码记录
     */
    @Override
    public SmsRecord get(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return null;
        }

        return mapper.get(mobile);
    }

    /*
     * 删除短信验证码记录
     */
    @Override
    public boolean delete(String mobile) {
        if (!StringUtils.isValidMobile(mobile)) {
            return false;
        }

        return mapper.delete(mobile) == 1;
    }

    /*
     * 插入验证码记录
     */
    @Override
    public boolean insert(SmsRecord record) {
        try {
            SmsRecord.selfCheck(record);
        } catch (SelfCheckException e) {
            return false;
        }

        //如果已经存在未删除的记录，只要更新就可以了
//        if (mapper.get(record.getMobile()) != null) {
//            return mapper.update(record) == 1;
//        }

        return mapper.insert(record) == 1;
    }

}
