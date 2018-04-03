package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.ActivityConfig;
import com.cmcc.vrp.province.service.ActivityConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinqinyan on 2017/8/30.
 * 平台营销活动中奖范围配置
 * 中奖范围是指：用户手机号归属地及运营商
 */
@Controller
@RequestMapping("/manage/activityConfig")
public class ActivityWinScopeController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ActivityWinScopeController.class);

    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    ActivityConfigService activityConfigService;

    @RequestMapping("getConfig")
    public String getConfig(ModelMap modelMap){
        ActivityConfig activityConfig = activityConfigService.getActivityConfig();
        if(activityConfig != null){
            modelMap.put("province", activityConfig.getProvince());
            modelMap.put("isp", activityConfig.getIsp());
        }else{
            modelMap.put("province", "");
            modelMap.put("isp", "");
        }
        modelMap.put("activityConfig", activityConfig);
        return "activityConfig/provinceConfig.ftl";
    }

    /*@RequestMapping("testCheckMobile")
    public void testCheckMobile(String mobile){
        //String mobile = "18867103717";
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        System.out.println(JSON.toJSONString(phoneRegion));

        String provinceStr = activityConfigService.getProvince(Constants.ACTIVITY_CONFIG_KEY.PROVINCE.getResult());
        String ispStr = activityConfigService.getIsp(Constants.ACTIVITY_CONFIG_KEY.ISP.getResult());

        boolean flag = false;
        //先判断省份
        if(!StringUtils.isEmpty(provinceStr)){
            String[] provinceList = provinceStr.split(",");
            if(!(provinceList.length == 1 && "全国".equals(provinceList[0]))){
                boolean provinceFlag = true;
                for(String province : provinceList){
                    if(province.equals(phoneRegion.getProvince())){
                        provinceFlag = false;
                        break;
                    }
                }

                if(provinceFlag){
                    System.out.println("不在指定省份里面");
                    flag = true;
                }
            }
        }

        //判断运营商
        if(!flag){
            if(!StringUtils.isEmpty(ispStr)){
                String[] ispList = ispStr.split(",");
                if(!(ispList.length==1 && "A".equals(ispList[0]))){
                    //不支持三网
                    boolean ispFlag = true;
                    for(String isp : ispList){
                        if(isp.equals(phoneRegion.getSupplier())){
                            ispFlag = false;
                            break;
                        }
                    }

                    if(ispFlag){
                        System.out.println("不在指定运营商里面");
                        flag = true;
                    }
                }
            }
        }

        if(flag){
            System.out.println("手机号校验不通过");
        }

    }*/

    //相应内容{"mobile":"18867103717","province":"浙江","city":"杭州市","supplier":"M"}.

    @RequestMapping("setConfigAjax")
    public void setConfigAjax(HttpServletResponse response, HttpServletRequest request){
        Map map = new HashMap<String, String>();
        Boolean result = false;

        String provinceStr = request.getParameter("province");
        String ispStr = request.getParameter("isp");
        logger.info("前端 province = {}, isp = {}", provinceStr, ispStr);
        if(!StringUtils.isEmpty(provinceStr) && !StringUtils.isEmpty(ispStr)){
            ispStr = verifyIsp(ispStr);
            provinceStr = verifyProvince(provinceStr);

            ActivityConfig activityConfig = activityConfigService.getActivityConfig();
            if(activityConfig!=null){
                //编辑
                if(activityConfigService.updateByPrimaryKeySelective(createActivityConfig(provinceStr, ispStr, activityConfig.getId()))){
                    logger.info("中奖设置：成功将原 oldProvince = {} ; oldIsp = {} 修改为 newProvince = {} ; newIsp = {}",
                        activityConfig.getProvince(), activityConfig.getIsp(), provinceStr, ispStr);
                    result = true;
                }else{
                    logger.info("中奖设置：将原 oldProvince = {} ; oldIsp = {} 修改为 newProvince = {} ; newIsp = {} 失败",
                        activityConfig.getProvince(), activityConfig.getIsp(), provinceStr, ispStr);
                }
            }else{
                //新增
                if(activityConfigService.insert(createActivityConfig(provinceStr, ispStr, null))){
                    logger.info("成功插入中奖设置： newProvince = {} ; newIsp = {}", provinceStr, ispStr);
                    result = true;
                }else{
                    logger.info("插入中奖设置： newProvince = {} ; newIsp = {} 失败", provinceStr, ispStr);
                }
            }
        }
        map.put("result", result.toString());
        try{
            response.getWriter().write(JSON.toJSONString(map));
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private String verifyIsp(String ispStr){
        if(ispStr.indexOf("A")!=-1){
            logger.info("ispStr = {} 包含三网标示“A”, 所以将 {} 变更为A");
            return "A";
        }
        return ispStr;
    }

    private String verifyProvince(String provinceStr){
        if(provinceStr.indexOf("全国")!=-1){
            logger.info("provinceStr = {} 包含“全国”, 所以将 {} 变更为“全国”");
            return "全国";
        }
        return provinceStr;
    }

    private ActivityConfig createActivityConfig(String provinceStr, String ispStr, Long id){
        ActivityConfig activityConfig = new ActivityConfig();
        if(id!=null){
            activityConfig.setId(id);
        }else{
            activityConfig.setCreateTime(new Date());
            activityConfig.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        }
        activityConfig.setProvince(provinceStr);
        activityConfig.setIsp(ispStr);
        activityConfig.setUpdateTime(new Date());
        return activityConfig;
    }


}
