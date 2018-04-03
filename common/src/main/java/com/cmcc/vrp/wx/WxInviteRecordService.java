package com.cmcc.vrp.wx;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.wx.model.WxInviteRecord;

/**
 * WxInviteRecordService.java
 * @author wujiamin
 * @date 2017年2月28日
 */
public interface WxInviteRecordService {

    /** 
     * @Title: insert 
     */
    boolean insert(WxInviteRecord record);

    /** 
     * @Title: insertSelective 
     */
    boolean insertSelective(WxInviteRecord record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    WxInviteRecord selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    boolean updateByPrimaryKeySelective(WxInviteRecord record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    boolean updateByPrimaryKey(WxInviteRecord record);
    
    /** 
     * @Title: selectBySerialNum 
     */
    WxInviteRecord selectBySerialNum(String serialNum);

    /** 
     * @Title: selectByMap 
     */
    List<WxInviteRecord> selectByMap(Map map);
}
