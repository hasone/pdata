package com.cmcc.vrp.weixin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.Encrypter;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.InviteService;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.WxInviteQrcodeService;
import com.cmcc.vrp.wx.beans.WxUserInfo;

/**
 * 微信邀请功能
 * InviteController.java
 * @author wujiamin
 * @date 2017年2月23日
 */
@Controller
@RequestMapping("/wx/invite")
public class InviteController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InviteController.class);
    
    @Autowired
    WxAdministerService wxAdministerService;
    
    @Autowired
    InviteService inviteService;
    
    @Autowired
    WxInviteQrcodeService wxInviteQrcodeService;
    
    @Autowired
    WetchatService wetchatService;
    
    /** 
     * @Title: show 
     */
    @RequestMapping("show")
    public String show(HttpServletRequest request){
        String wxAspect = (String) request.getAttribute("wxAspect");
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            String mobile = getMobile();
            String openid = getWxOpenid();                        
            if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(openid)){
                LOGGER.info("session中用户手机号或openid为空");
                return "gdzc/404.ftl";
            }else{
                //根据手机号找到用户，判断二维码是否过期，过期则重新生成
                if(!inviteService.refreshQrcode(mobile, openid)){
                    LOGGER.info("更新邀请二维码失败");
                    return "gdzc/404.ftl";
                }

                try {
                    String des = Encrypter.encrypt(mobile);
                    return "redirect:show/" + des + ".html";//重定向到带个人手机号码的页面
                } catch (Exception e) {
                    LOGGER.info("adminId加密失败，" + e.getMessage());
                    return "gdzc/404.ftl";
                }                
            }

        }else{
            return wxAspect;
        }
    }
    
    /** 
     * 根据个人信息展示响应的二维码页面
     * @Title: showByAdminId 
     */
    @RequestMapping("show/{decryptMobile}")
    public String showByAdminId(@PathVariable("decryptMobile") String decryptMobile, ModelMap model, HttpServletRequest request){
        String mobile = Encrypter.decrypt(decryptMobile);
        if(StringUtils.isEmpty(mobile)){
            LOGGER.info("用户mobile为空");
            return "gdzc/404.ftl";
        }
        WxAdminister admin = wxAdministerService.selectByMobilePhone(mobile);
        if(admin == null || admin.getId() == null){
            LOGGER.info("根据手机号{}查找的用户为空", mobile);
            return "gdzc/404.ftl";
        }
        Long adminId = admin.getId();
        
        //1、判断用户是否是第一次点邀请（第一次需弹窗），返回标记供前台判断        
        model.put("first", inviteService.getFirstFlag(adminId));
        model.put("qrcodeId", decryptMobile);
        
        //2、获取用户信息回填到页面（昵称）
        WxUserInfo userInfo = wetchatService.getWxUserInfo(mobile);
        if(userInfo == null){
            LOGGER.info("在微信公众号侧查询不到用户的详细信息");
            return "gdzc/404.ftl";
        }
        LOGGER.info("昵称：{}", userInfo.getNickname());
        model.put("nickName", userInfo.getNickname());
        
        return "weixin/invite.ftl";

    }
}
