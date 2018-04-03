package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.wx.model.WxInviteRecord;

/**
 * WxInviteRecordMapper.java
 * @author wujiamin
 * @date 2017年2月28日
 */
public interface WxInviteRecordMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(WxInviteRecord record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(WxInviteRecord record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    WxInviteRecord selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(WxInviteRecord record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(WxInviteRecord record);

    /** 
     * @Title: selectBySerialNum 
     */
    WxInviteRecord selectBySerialNum(@Param("serialNum")String serialNum);

    /** 
     * @Title: selectByMap 
     */
    List<WxInviteRecord> selectByMap(Map map);
}