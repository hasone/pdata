package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.PROVINCETYPE;
import com.cmcc.vrp.enums.QRCodeExtendParam;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 二维码充值服务 <p> Created by sunyiwei on 2016/8/23.
 */
@Controller
@RequestMapping("/manage/qrcode/charge")
public class QrcodeChargeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QrcodeChargeController.class);
    @Autowired
    ActivitiesService activitiesService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ActivityWinRecordService activityWinRecordService;

    @Autowired
    private PhoneRegionService phoneRegionService;

    private String activityUrlKey;

    /**
     * 
     * @Title: phoneQuery 
     * @Description: TODO
     * @param mobile
     * @param resp
     * @throws IOException
     * @return: void
     */
    @RequestMapping(value = "phoneQuery")
    public void phoneQuery(String mobile, HttpServletResponse resp) throws IOException {
        Map result = new HashMap();
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        PROVINCETYPE province = PROVINCETYPE.fromValue(provinceFlag);
        //省平台只能输入本省移动手机号码,其他平台不校验号码        
        if (province.getValue().equals(PROVINCETYPE.ZIYING.getValue())) {//自营平台
            if (phoneRegion == null) {
                result.put("success", province.getDesc());
            } else {
                result.put("success", "true");
            }
        } else {//非自营平台
            if (phoneRegion == null || !"M".equalsIgnoreCase(phoneRegion.getSupplier())
                    || !phoneRegion.getProvince().equalsIgnoreCase(province.getDesc())) {
                result.put("success", province.getDesc() + "移动");
            } else {
                result.put("success", "true");
            }
        }

        try {
            resp.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //token参数为 activityId;activityRecordId 经过AES加密而成

    /**
     * @Title: index
     */
    @RequestMapping(value = "index")
    public String index(@QueryParam("token") String token, ModelMap model) throws DecoderException {
        Activities activities = null;
        List<String> ids = null;
        String actvitityId = null;
        String activityRecordId = null;

        if ((ids = parseUrl(token)) == null || ids.size() != 2 || StringUtils.isBlank(actvitityId = ids.get(0))
                || StringUtils.isBlank(activityRecordId = ids.get(1))
                || (activities = activitiesService.selectByActivityId(actvitityId)) == null) {
            LOGGER.error("无效的活动ID, 活动ID = {}.", actvitityId);
            model.put("errorMsg", "活动参数校验失败");
            return "error.ftl";
        }

        model.put("token", token);
        return "qrcode/chargeIndex.ftl";

    }

    /**
     * @Title: submitCharge
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public void submitCharge(String token, String mobile, ModelMap model, HttpServletResponse res)
            throws DecoderException {
        Activities activities = null;
        List<String> ids = null;
        String actvitityId = null;
        String activityRecordId = null;
        JSONObject json = new JSONObject();

        if ((ids = parseUrl(token)) == null || ids.size() != 2 || StringUtils.isBlank(actvitityId = ids.get(0))
                || StringUtils.isBlank(activityRecordId = ids.get(1))
                || (activities = activitiesService.selectByActivityId(actvitityId)) == null) {
            LOGGER.error("无效的活动ID, 活动ID = {}.", actvitityId);
            json.put("result", "活动信息校验未通过");
            try {
                res.getWriter().write(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<Object, Object> extendParams = new LinkedHashMap<Object, Object>();
        extendParams.put(QRCodeExtendParam.ACTIVITY_RECORD_ID.getKey(), activityRecordId);
        boolean result = activitiesService.participate(actvitityId, mobile, extendParams, null);
        if (result) {
            json.put("result", "success");
        } else {
            json.put("result", "领取失败，请重试。如有疑问，请联系企业!");
        }

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: successIndex
     */
    @RequestMapping(value = "success")
    public String successIndex(String token, ModelMap model) throws DecoderException {
        Activities activities = null;
        List<String> ids = null;
        String actvitityId = null;
        String activityRecordId = null;

        if ((ids = parseUrl(token)) == null || ids.size() != 2 || StringUtils.isBlank(actvitityId = ids.get(0))
                || StringUtils.isBlank(activityRecordId = ids.get(1))
                || (activities = activitiesService.selectByActivityId(actvitityId)) == null) {
            LOGGER.error("无效的活动ID, 活动ID = {}.", actvitityId);
            model.put("errorMsg", "活动参数校验失败");
            return "error.ftl";

        }

        Map map = new HashMap();
        map.put("recordId", activityRecordId);
        List<ActivityWinRecord> records = activityWinRecordService.selectByMap(map);
        if (records.size() != 1) {
            LOGGER.error("根据ID={}获取了{}条记录", activityRecordId, records.size());
            model.put("errorMsg", "充值信息获取失败！");
            return "error.ftl";
        }

        model.put("record", records.get(0));
        return "qrcode/winning.ftl";

    }

    /**
     * 
     * @Title: parseUrl 
     * @Description: TODO
     * @param token
     * @return
     * @return: List<String>
     */
    private List<String> parseUrl(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String decrpStr = AES.decryptString(token, getActivityUrlKey());
        String[] ids = decrpStr.split(";");
        return Arrays.asList(ids);
    }

    /**
     * 
     * @Title: getActivityUrlKey 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getActivityUrlKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_URL_KEY.getKey());
    }
}
