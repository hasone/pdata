package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.GetDataFromTemplateResp;
import com.google.zxing.common.BitMatrix;

/**
 * 营销模板服务类
 * Created by qinqinyan on 2016/10/13.
 */
public interface ActivityTemplateService {

    /**
     * 通知营销模板关闭活动
     * @param activityUrl 活动url
     * @author qinqinyan
     * */
    boolean notifyTemplateToClose(String activityUrl, String activityId);
    
    /**
     * 向营销模板请求营销数据
     * @param activityUrl 活动url
     * @author qinqinyan
     * */
    GetDataFromTemplateResp getDataFromTemplate(String activityUrl);


    /**
     * 生成二维码
     * @param margin
     * @param matrix
     * */
    //BitMatrix updateBit(BitMatrix matrix, int margin);

    /**
     * 逻辑删除
     * @param id
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record
     * */
    boolean insert(ActivityTemplate record);

    /**
     * 插入
     * @param record
     * */
    boolean insertSelective(ActivityTemplate record);

    /**
     * 根据主键查找
     * @param id
     * */
    ActivityTemplate selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * */
    boolean updateByPrimaryKeySelective(ActivityTemplate record);

    /**
     * 更新
     * @param record
     * */
    boolean updateByPrimaryKey(ActivityTemplate record);

    /**
     * 根据activityId获取ActivityTemplate
     * @param activityId
     * @author qinqinyan
     * */
    ActivityTemplate selectByActivityId(String activityId);

    /**
     * @param margin
     * @param matrix
     * @author qinqinyan
     * */
    BitMatrix updateBit(BitMatrix matrix, int margin);

    /**
     * 校验手机号
     * @param mobile
     * @author qinqinyan
     * */
    boolean invalidMobile(String mobile);


}
