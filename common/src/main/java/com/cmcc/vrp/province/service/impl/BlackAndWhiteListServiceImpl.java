package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.BlackAndWhiteListMapper;
import com.cmcc.vrp.province.model.BlackAndWhiteList;
import com.cmcc.vrp.province.service.BlackAndWhiteListService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * BlackAndWhiteListServiceImpl
 * */
@Service("blackAndWhiteListService")
public class BlackAndWhiteListServiceImpl implements BlackAndWhiteListService {

    @Autowired
    private BlackAndWhiteListMapper mapper;

    @Override
    public List<String> getPhonesByActivityId(Long activityId) {
        if (activityId == null) {
            return null;
        }
        List<BlackAndWhiteList> bawList = mapper.getPhonesByActivityId(activityId);

        if (bawList == null) {
            return null;
        }

        List<String> phonesList = new ArrayList<String>();
        for (int i = 0; i < bawList.size(); ++i) {
            phonesList.add(bawList.get(i).getPhone());

        }
        return phonesList;
    }

    /*
     * 根据对象删除记录
     */
    @Override
    public boolean delete(List<String> phonelist, Long activityId) {
        // TODO Auto-generated method stub
        if (phonelist == null) {
            return false;
        }
        if (activityId == null) {
            return false;
        }
//		for(String phone : phonelist){
//			mapper.delete(phone,activityId);
//		}

        return mapper.batchDelete(phonelist, activityId) == phonelist.size();
    }

    /*
     * 根据活动id删除所有相应的手机号
     */
    @Override
    public int deleteByActivityId(Long activityId) {
        // TODO Auto-generated method stub
        if (activityId == null) {
            return 0;
        }
        return mapper.deleteByActivityId(activityId);
    }

    /*
     * 生成名单对象
     */
    private BlackAndWhiteList getBlackAndWhiteList(String phone, String activityType, Long activityId, Integer isWhiteFlag) {
        if (!StringUtils.isValidMobile(phone)) {
            return null;
        }
        if (activityType == null || activityType.length() <= 0) {
            return null;
        }
        if (activityId == null) {
            return null;
        }
        if (isWhiteFlag == null) {
            return null;
        }

        BlackAndWhiteList blackAndWhiteList = new BlackAndWhiteList();
        blackAndWhiteList.setActivityType(activityType);
        blackAndWhiteList.setPhone(phone);
        blackAndWhiteList.setActivityId(activityId);
        blackAndWhiteList.setIsWhiteFlag(isWhiteFlag);
        blackAndWhiteList.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return blackAndWhiteList;
    }


    /*
     * 批量插入记录
     * 事务回滚
     */
    @Override
    public boolean insertBatch(List<String> list, String activityType, Long activityId, Integer isWhiteFlag) {
        // TODO Auto-generated method stub
        if (list == null) {
            return false;
        }
        if (StringUtils.isEmpty(activityType)) {
            return false;
        }
        if (activityId == null) {
            return false;
        }
        if (isWhiteFlag == null) {
            return false;
        }

        List<BlackAndWhiteList> insertListObj = new ArrayList<BlackAndWhiteList>();

        if (list.size() > 0) {
            //构造成名单对象
            for (String phone : list) {
                if (getBlackAndWhiteList(phone, activityType, activityId, isWhiteFlag) != null) {
                    insertListObj.add(getBlackAndWhiteList(phone, activityType, activityId, isWhiteFlag));
                }
            }
            //批量插入
            if (mapper.insertBatch(insertListObj) != insertListObj.size()) {
                throw new TransactionException();
            }
            return true;
        } else {
            return true;
        }
    }

}
