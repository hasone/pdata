package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.WxAdminister;

/**
 * 
 * @ClassName: WxAdministerService 
 * @Description: TODO
 */

public interface WxAdministerService {
    
    /**
     * @Title: selectAdministerById 
     * @Description: TODO
     * @param id
     * @return
     * @return: Administer
     */
    WxAdminister selectWxAdministerById(Long id);
    
    /**
     * 更新手机号
     * 这个功能现在仅限于广东众筹平台
     * @author qinqinyan
     * */
    boolean updateWxAdminster(String oldMobile, String newMobile);
    
    /**
     * @Title: selectByMobilePhone 
     * @Description: TODO
     * @param phone
     * @return
     * @return: WxAdminister
     */
    WxAdminister selectByMobilePhone(String phone);
    
    /**
     * @Title: updateSelective 
     * @Description: TODO
     * @param wxAdminister
     * @return
     * @return: boolean
     */
    boolean updateSelective(WxAdminister wxAdminister);
    
    /** 
     * 微信插入用户，同时构建账户
     * @Title: insertForWx 
     */
    boolean insertForWx(String mobile, String openid);
    
   

}
