/**
 * @Title: LogEntryController.java
 * @Package com.cmcc.vrp.province.webin.controller
 * @author: sunyiwei
 * @date: 2015年4月3日 上午9:56:16
 * @version V1.0
 */
package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MaskingService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: LogEntryController
 * @Description: 登录登出控制器
 * @author: sunyiwei
 * @date: 2015年4月3日 上午9:56:16
 */
@Controller
@RequestMapping("/manage")
public class LogEntryController extends BaseController {
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    MaskingService maskingService;

    /** 
     * @Title: logIn
     * edit by qinqinyan on 2017/08/14
     * 广东流量卡增加门板 
    */
    @RequestMapping("/index")
    public String logIn(ModelMap model,QueryObject queryObject, String needmask) {
        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        model.put("logoName", getLogoName());
        //判断众筹是否显示蒙版
        /*if (StringUtils.isEmpty(needmask)
                && "gd_mdrc".equals(getProvinceFlag())
                && getIsCrowdfundingPlatform()
                && needCrowdfundingMask()) {
            return "redirect:/manage/crowdFunding/createCrowdFunding.html";
        }*/
        
        //判断省份标示，是否有门板，哈哈哈哈
        if("gd_mdrc".equals(getProvinceFlag())){
            model.put("isUseGdsso", "true");//前端展示gdsso的返回按钮
                     
            Administer administer = getCurrentUser();
            if(administer!=null){
                //Masking masking = maskingService.getByAdminId(administer.getId());
                if(!"YES".equals(isCrowdFunding())){
                    //广东流量卡平台
                    
                    if (StringUtils.isEmpty(needmask) 
                            && needMdrcMask()) {
                        return "mdrcMasking/cardMakeIntro.ftl";
                    }
//                    if (StringUtils.isEmpty(needmask) && 
//                            masking != null && masking.getMdrcMask().intValue() == 1) {
//                        return "mdrcMasking/cardMakeIntro.ftl";
//                    }
                }else{
                    if (StringUtils.isEmpty(needmask) 
                            && needCrowdfundingMask()) {
                        return "redirect:/manage/crowdFunding/createCrowdFunding.html";
                    }
//                    if(StringUtils.isEmpty(needmask) && masking != null 
//                            && masking.getCrowdfundingMask().intValue() == 1){
//                        return "redirect:/manage/crowdFunding/createCrowdFunding.html";
//                    }
                }
            }
        }else{
            model.put("isUseGdsso", "false");//前端不展示gdsso的返回按钮  
        }
        return "index_default.ftl";
    }
    
    /**
     * index_default_gd 功能：1.前端js清除store 2.跳转/manage/index.html
     */
    @RequestMapping("/gdIndex")
    public String gdlogIn(ModelMap model, String needmask) {
        return "index_default_gd.ftl";
    }

    /** 
     * @Title: logOut 
    */
    @RequestMapping("/logout")
    public String logOut() {
        return null;
    }

    public String getLogoName() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey());
    }
    
    /**
     * 是否是广东众筹
     * */
    public String isCrowdFunding() {
        return globalConfigService.get(GlobalConfigKeyEnum.IS_CROWDFUNDING_PLATFORM.getKey());
    }
    
}
