package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.wx.model.WxInviteQrcode;

public interface WxInviteQrcodeMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(WxInviteQrcode record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(WxInviteQrcode record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    WxInviteQrcode selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(WxInviteQrcode record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(WxInviteQrcode record);
    
    /** 
     * @Title: selectByAdminId 
     */
    WxInviteQrcode selectByAdminId(Long adminId);
    
    /** 
     * @Title: insertOrUpdateSelective 
     */
    int insertOrUpdateSelective(WxInviteQrcode record);
}