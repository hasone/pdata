package com.cmcc.vrp.province.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.YqxOrderRecordMapper;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * YqxOrderRecordServiceImpl.java
 * @author wujiamin
 * @date 2017年5月4日
 */
@Service
public class YqxOrderRecordServiceImpl extends AbstractCacheSupport implements YqxOrderRecordService {
    @Autowired
    YqxOrderRecordMapper yqxOrderRecordMapper;
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public boolean insert(YqxOrderRecord yqxOrderRecord) {
        yqxOrderRecord.setCreateTime(new Date());
        yqxOrderRecord.setDeleteFlag(0);
        yqxOrderRecord.setUpdateTime(new Date());
        return yqxOrderRecordMapper.insert(yqxOrderRecord) == 1;
    }

    @Override
    public List<YqxOrderRecord> selectByMap(Map map) {
        if(map.get("searchTime")!=null && !StringUtils.isEmpty((String) map.get("searchTime"))){
            String time = (String) map.get("searchTime");
            String[] times = time.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        if(map.get("refundStatus") != null){
            String refundStatus = (String) map.get("refundStatus");
            map.put("refundStatus", java.util.Arrays.asList(refundStatus.split(",")));
        }
        return yqxOrderRecordMapper.selectByMap(map);
    }
    
    @Override
    public YqxOrderRecord selectBySerialNum(String serialNum) {
        return yqxOrderRecordMapper.selectBySerialNum(serialNum);
    }
    
    @Override
    public Boolean updateByPrimaryKey(YqxOrderRecord record) {
        if(record == null){
            return false;
        }
        record.setUpdateTime(new Date());
        return yqxOrderRecordMapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    protected String getPrefix() {        
        return "yqx.order.";
    }

    @Override
    public boolean ifOverNum(String mobile) {
        String num = cacheService.get(mobile);
        if(!StringUtils.isEmpty(num)){
            String limitNum = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDER_MONTH_LIMIT.getKey());
            if(!StringUtils.isEmpty(limitNum) && Integer.parseInt(num) >= Integer.parseInt(limitNum)){
                return true;
            }        
        }       
        return false;
    }

    @Override
    public boolean create(YqxOrderRecord yqxOrderRecord) {
        //增加计数
        cacheService.getIncrOrUpdate(yqxOrderRecord.getMobile(), getSecondToMonth());
        return insert(yqxOrderRecord);
    }

    /** 
     * 获取当前时间到该月月末的秒数
     * @Title: getSecondToMonth 
     */
    private int getSecondToMonth() {
        Calendar startTime = Calendar.getInstance();        
        Date endTime = DateUtil.getEndTimeOfMonth(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH)+1);
        long diffDays = endTime.getTime() - startTime.getTimeInMillis();        
        return (int) Math.round(diffDays/1000.0);
    }

    @Override
    public Integer countByMap(Map<String, Object> map) {
        if(map.get("searchTime")!=null && !StringUtils.isEmpty((String) map.get("searchTime"))){
            String time = (String) map.get("searchTime");
            String[] times = time.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        if(map.get("refundStatus") != null){
            String refundStatus = (String) map.get("refundStatus");
            map.put("refundStatus", java.util.Arrays.asList(refundStatus.split(",")));
        }
        
        return yqxOrderRecordMapper.countByMap(map);
    }
    
    @Override
    public boolean duringAccountCheckDate() {
        String type = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey());
        String startTime = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey());
        String startDay = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey());
        String endTime = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey());
        String endDay = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey());
        
        if (org.springframework.util.StringUtils.isEmpty(type) || org.springframework.util.StringUtils.isEmpty(startTime)
                || org.springframework.util.StringUtils.isEmpty(startDay)
                || org.springframework.util.StringUtils.isEmpty(endTime) || org.springframework.util.StringUtils.isEmpty(endDay)) {
            return false;
        }
        Date now = new Date();
        Calendar a = Calendar.getInstance();

        int nowDay = now.getDate();
        int monthEndDay;
        int monthBeginDay;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if ("0".equals(type)) {
            return false;
        }
        if ("1".equals(type)) {
            a.set(Calendar.DATE, Integer.parseInt(endDay));// 把日期设置为当月第一天
            Date begin = a.getTime();

            Date beginDate = null;
            Date endDate = null;
            try {
                String beginStr = dateFormat.format(begin);
                beginDate = sdf.parse(beginStr + " " + endTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
          
            //将日期a设置为当月第一天
            a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
            a.roll(Calendar.DATE, -1 * Integer.parseInt(startDay));// 日期回滚一天，也就是最后一天
            Date end = a.getTime();
            try {
                String endStr = dateFormat.format(end);
                endDate = sdf.parse(endStr + " " + startTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((endDate != null && now.after(endDate))
                    || (beginDate != null && now.before(beginDate))) {
                return true;
            }
        }
        if ("2".equals(type)) {
            monthEndDay = Integer.parseInt(startDay);
            monthBeginDay = Integer.parseInt(endDay);
            if (monthEndDay > monthBeginDay) {
                if (nowDay > monthEndDay || nowDay < monthBeginDay) {
                    return true;
                }

                String str = dateFormat.format(now);
                Date date = null;
                try {
                    date = sdf.parse(str + " " + startTime);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (nowDay == monthEndDay) {
                    if (now.after(date)) {
                        return true;
                    }
                }
                if (nowDay == monthBeginDay) {
                    if (now.before(date)) {
                        return true;
                    }
                }
            } else {
                a.set(Calendar.DATE, monthEndDay);
                Date begin = a.getTime();

                Date beginDate = null;
                Date endDate = null;
                try {
                    String beginStr = dateFormat.format(begin);
                    beginDate = sdf.parse(beginStr + " " + startTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                a.set(Calendar.DATE, monthBeginDay);
                Date end = a.getTime();
                try {
                    String endStr = dateFormat.format(end);
                    endDate = sdf.parse(endStr + " " + endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (endDate != null && beginDate != null && now.before(endDate)
                        && now.after(beginDate)) {
                    return true;
                }
            }
        }

        return false;
    }

    /** 
     * 重庆云企信获取前台展示的虚拟购买数量
     * @Title: getVirtualCqNum 
     * @return
     * @Author: wujiamin
     * @date 2017年7月19日
    */
    @Override
    public Long getVirtualCqNum() {
        Random d = new Random();
        int value = d.nextInt(11) + 1;//获取0-10的随机整数
        Long currentNum = cacheService.incrby("cqnum", value, 2200);
        if(currentNum == null){
            currentNum = 2200L;
        }
        return currentNum;
    }
}
