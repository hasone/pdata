package com.cmcc.vrp.province.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.DiscountRecordMapper;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.util.DateUtil;

/**
 * 
 * @ClassName: DiscountRecordServiceImpl 
 * @Description: TODO
 */
@Service("discountRecordService")
public class DiscountRecordServiceImpl implements DiscountRecordService{

    @Autowired
    private DiscountRecordMapper discountRecordMapper;
	
    @Override
    public boolean insert(DiscountRecord discountRecord) {
        if(discountRecord == null 
                || StringUtils.isBlank(discountRecord.getPrdCode())
		|| StringUtils.isBlank(discountRecord.getUserId())){
            return false;
	}
	return discountRecordMapper.insertSelective(discountRecord) > 0;
    }

    @Override
    public boolean batchInsert(List<DiscountRecord> records) {
	if(records == null || records.size() <= 0){
	    return true;
	}
	return discountRecordMapper.batchInsert(records) == records.size();
    }
    
    @Override
    public List<DiscountRecord> getOneDayDiscount(String date, String userId,
            String prdCode) {
        Date startTime=DateUtil.parse("yyyy-MM-dd",date);
        Date endTime  =DateUtil.getEndTimeOfDate(startTime);
        return discountRecordMapper.getOneDayDiscount(startTime,endTime,userId, prdCode);
    }

    @Override
    public String findDiscount(List<DiscountRecord> list, String billTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        
        Long billTimeLong=NumberUtils.toLong(billTime);
        
        //没有找到匹配记录
        if(list==null || billTimeLong.equals(0L)){
            return null;
        }
        
        //将账单中的时间，和list中的所有记录比较时间，找到第一个大于的记录，取出折扣,返回
        for(DiscountRecord discountRecord:list){
            String discountTime = dateFormat.format(discountRecord.getCreateTime());
            Long discountTimeLong = NumberUtils.toLong(discountTime);
            
            if(billTimeLong>=discountTimeLong){
                return String.valueOf(discountRecord.getDiscount());
            }
        }
        return null;
    }

}
