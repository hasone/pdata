package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ActivityBlackAndWhiteMapper;
import com.cmcc.vrp.province.model.ActivityBlackAndWhite;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by qinqinyan on 2016/8/25.
 */
@Service("activityBlackAndWhiteService")
public class ActivityBlackAndWhiteServiceImpl implements ActivityBlackAndWhiteService {
    @Autowired
    ActivityBlackAndWhiteMapper activityBlackAndWhiteMapper;

    @Override
    public List<String> selectPhonesByMap(Map map) {
        return activityBlackAndWhiteMapper.selectPhonesByMap(map);
    }

    @Override
    public boolean batchInsert(String activityId, Integer isWhite, String phones) {
        if (StringUtils.isEmpty(activityId) || isWhite == null || StringUtils.isEmpty(phones)) {
            return false;
        }
        List<ActivityBlackAndWhite> records = initActivityBlackAndWhiteList(activityId, isWhite, phones);

        return activityBlackAndWhiteMapper.batchInsert(records) == records.size();
    }

    private List<ActivityBlackAndWhite> initActivityBlackAndWhiteList(String activityId, Integer isWhite, String phones) {
        String[] phoneArray = phones.split(",");
        //黑白名单去重
        List list = Arrays.asList(phoneArray);
        Set set = new HashSet(list);
        phoneArray = (String[]) set.toArray(new String[0]);

        List<ActivityBlackAndWhite> records = new ArrayList<ActivityBlackAndWhite>();
        for (int i = 0; i < phoneArray.length; i++) {
            ActivityBlackAndWhite record = new ActivityBlackAndWhite();
            record.setActivityId(activityId);
            record.setMobile(phoneArray[i]);
            record.setIsWhite(isWhite);
            record.setDeleteFlag(0);
            record.setUpdateTime(new Date());
            record.setCreateTime(new Date());

            records.add(record);
        }
        return records;
    }

    @Override
    public boolean deleteByActivityId(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return false;
        }
        return activityBlackAndWhiteMapper.deleteByActivityId(activityId) >= 0;
    }

    @Override
    public boolean updateIsWhiteByActivityId(String activityId, Integer isWhite) {
        if (StringUtils.isEmpty(activityId) || isWhite == null) {
            return false;
        }
        return activityBlackAndWhiteMapper.updateIsWhiteByActivityId(activityId, isWhite) >= 0;
    }

    @Override
    public boolean weatherInPhoneList(String mobile, String activityId) {
        // TODO Auto-generated method stub
        if(!StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(activityId)){
            Map map = new HashMap<String, String>();
            map.put("mobile", mobile);
            map.put("activityId", activityId);
            List<String> phoneList = activityBlackAndWhiteMapper.selectPhonesByMap(map);
            if(phoneList!=null && phoneList.size()>0){
                return true;
            }
        }
        return false;
    }
    
    
}
