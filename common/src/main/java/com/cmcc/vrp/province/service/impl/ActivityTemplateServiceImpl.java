package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.OfflineRespCode;
import com.cmcc.vrp.province.dao.ActivityTemplateMapper;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.GetDataFromTemplateResp;
import com.cmcc.vrp.province.model.OfflineActivityResp;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.ActivityConfigService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qinqinyan on 2016/10/13.
 */
@Service("activityTemplateService")
public class ActivityTemplateServiceImpl implements ActivityTemplateService {
    private static final Logger logger = LoggerFactory
        .getLogger(ActivityTemplateServiceImpl.class);

    @Autowired
    ActivityTemplateMapper activityTemplateMapper;

    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    ActivityConfigService activityConfigService;

    @Override
    public boolean notifyTemplateToClose(String activityUrl, String activityId) {
        if (!StringUtils.isEmpty(activityUrl)) {
            String token = parse(activityUrl);
            logger.info("token = {}", token);
            if (!org.apache.commons.lang.StringUtils.isBlank(token)) {
                // 通知营销模板
                String offlineUrl = getDownshelfUrl();
                logger.info("offlineUrl = {}", offlineUrl);
                String response = HttpConnection.doPost(offlineUrl, token, "text/html",
                    "utf-8", true);
                logger.info("营销模板返回的下架结果-{}",response);
                
                //删除营销模板营销数据，这里只对结果进行更新处理
                String delDataUrl = getDelDataUrl();
                logger.info("delDataUrl = {}", delDataUrl);
                String delResponse = HttpConnection.doPost(delDataUrl, token, "text/html",
                        "utf-8", true);
                logger.info("营销模板返回的删除数据结果结果-{}",delResponse);
                if(!org.apache.commons.lang.StringUtils.isBlank(delResponse)){
                    GetDataFromTemplateResp getDataFromTemplateResp = new Gson().fromJson(delResponse,
                            GetDataFromTemplateResp.class);
                    ActivityInfo old = activityInfoService.selectByActivityId(activityId);
                    ActivityInfo updateActivityInfo = new ActivityInfo();
                    updateActivityInfo.setActivityId(activityId);
                    updateActivityInfo.setId(old.getId());
                    updateActivityInfo.setPlayCount(getDataFromTemplateResp.getPlayCount());
                    updateActivityInfo.setVisitCount(getDataFromTemplateResp.getVisitCount());
                    updateActivityInfo.setGivedUserCount(getDataFromTemplateResp.getWinCount());
                    
                    if(!activityInfoService.updateByPrimaryKeySelective(updateActivityInfo)){
                        logger.info("更新活动  = {} 访问游戏次数  = {}, 参与人次 = {}, 中奖人次 = {} 失败",
                                updateActivityInfo.getActivityId(), updateActivityInfo.getVisitCount(),
                                updateActivityInfo.getPlayCount(), updateActivityInfo.getGivedUserCount());
                    }else{
                        logger.info("更新活动  = {} 访问游戏次数  = {}, 参与人次 = {}, 中奖人次 = {} 成功",
                                updateActivityInfo.getActivityId(), updateActivityInfo.getVisitCount(),
                                updateActivityInfo.getPlayCount(), updateActivityInfo.getGivedUserCount());
                    }
                }
                
                if (!org.apache.commons.lang.StringUtils.isBlank(response)) {
                    OfflineActivityResp resp = new Gson().fromJson(response,
                        OfflineActivityResp.class);
                    return resp != null
                        && resp.getCode() == OfflineRespCode.SUCCESS
                        .getValue();
                }
            }
        }
        return false;
    }

    @Override
    public GetDataFromTemplateResp getDataFromTemplate(String activityUrl) {
        if (!StringUtils.isEmpty(activityUrl)) {
            String token = parse(activityUrl);
            logger.info("请求token = {}", token);
            if (!org.apache.commons.lang.StringUtils.isBlank(token)) {
                // 通知营销模板
                String getDataUrl = getActivityGetDataUrl();
                logger.info("请求url = {}", getDataUrl);
                String response = HttpConnection.doPost(getDataUrl, token, "text/html",
                    "utf-8", true);
                logger.info("营销模板返回的结果-{}",response);
                
                if (!org.apache.commons.lang.StringUtils.isBlank(response)) {
                    GetDataFromTemplateResp resp = new Gson().fromJson(response,
                            GetDataFromTemplateResp.class);
                    return resp;
                }
            }
        }
        return null;
    }
    
    public String getActivityGetDataUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_GET_DATA_URL.getKey());
    }



    @Override
    public BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    // 解析活动URL中的特定字符串
    private String parse(String url) {
        String regex = "/lottery/game/(.*)/index.html";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    //获取下架url
    public String getDownshelfUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_OFFLINE_URL
            .getKey());
    }
    
    //获取删除营销数据url
    public String getDelDataUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_DEl_DATA_URL
            .getKey());
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if(id==null){
            return false;
        }
        return activityTemplateMapper.deleteByPrimaryKey(id)==1;
    }

    @Override
    public boolean insert(ActivityTemplate record) {
        if(record==null){
            return false;
        }
        return activityTemplateMapper.insert(record)==1;
    }

    @Override
    public boolean insertSelective(ActivityTemplate record) {
        if(record!=null){
            return activityTemplateMapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public ActivityTemplate selectByPrimaryKey(Long id) {
        if(id==null){
            return null;
        }
        return activityTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(ActivityTemplate record) {
        if(record==null){
            return false;
        }
        return activityTemplateMapper.updateByPrimaryKeySelective(record)==1;
    }

    @Override
    public boolean updateByPrimaryKey(ActivityTemplate record) {
        if(record==null){
            return false;
        }
        return activityTemplateMapper.updateByPrimaryKey(record)==1;
    }

    @Override
    public ActivityTemplate selectByActivityId(String activityId) {
        if(!StringUtils.isEmpty(activityId)){
            return activityTemplateMapper.selectByActivityId(activityId);
        }
        return null;
    }

    @Override
    public boolean invalidMobile(String mobile) {
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        logger.info("mobile = {} ,查询号段结果：{}", mobile, JSON.toJSONString(phoneRegion));

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
                    logger.info("mobile = {} 不在指定省份里面", mobile);
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
                        logger.info("mobile = {} 不在指定运营商里面", mobile);
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
}
