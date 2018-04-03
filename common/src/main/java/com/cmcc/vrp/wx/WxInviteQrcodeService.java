package com.cmcc.vrp.wx;

import com.cmcc.vrp.wx.model.WxInviteQrcode;

/**
 * WxInviteQrcodeService.java
 * @author wujiamin
 * @date 2017年2月23日
 */
public interface WxInviteQrcodeService {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    boolean deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    boolean insert(WxInviteQrcode record);

    /** 
     * @Title: insertSelective 
     */
    boolean insertSelective(WxInviteQrcode record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    WxInviteQrcode selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    boolean updateByPrimaryKeySelective(WxInviteQrcode record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    boolean updateByPrimaryKey(WxInviteQrcode record);

    /** 
     * @Title: selectByAdminId 
     */
    WxInviteQrcode selectByAdminId(Long adminId);

    /** 
     * @Title: insertOrUpdateSelective 
     */
    int insertOrUpdateSelective(WxInviteQrcode record);
}
