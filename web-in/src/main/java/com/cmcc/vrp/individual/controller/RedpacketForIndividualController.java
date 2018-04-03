package com.cmcc.vrp.individual.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.activity.model.AutoResponsePojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.MatrixToImageWriter;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 个人红包服务类（流量币个人红包）
 * Created by qinqinyan on 2016/9/26.
 */
@Controller
@RequestMapping("/manage/individual/redpacket")
public class RedpacketForIndividualController extends BaseController {
    private final Logger logger = Logger.getLogger(RedpacketForIndividualController.class);

    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ActivityTemplateService activityTemplateService;

    /**
     * @param map
     * @return
     */
    @RequestMapping("redpacketIndex")
    public String redpacketIndex(ModelMap map) {
        Administer administer = getCurrentUser();
        List<Long> endIds = enterprisesService.getEnterpriseIdByAdminId(administer);
        if (endIds != null) {
            map.addAttribute("enterpriseId", endIds.size() > 0 ? endIds.get(0) : -1);
            map.addAttribute("adminId", administer.getId());
        }
        return "individual/processing/redpacket.ftl";
    }

    /**
     * @param response
     * @param enterpriseId
     * @param activityName
     * @param startTime
     * @param endTime
     * @param type
     * @param size
     * @param count
     * @param adminId
     * @throws IOException
     */
    @RequestMapping("saveRedxpacketAjax")
    public void saveRedxpacketAjax(HttpServletResponse response, Long enterpriseId, String activityName, String startTime,
                                   String endTime, Integer type, Long size, Long count, Long adminId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (enterpriseId == null || StringUtils.isEmpty(activityName) || StringUtils.isEmpty(startTime) ||
            StringUtils.isEmpty(endTime) || type == null || size == null || count == null) {
            map.put("success", "fail");
            map.put("errorMsg", "生成活动失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        try{
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDate = fmt.parse(endTime+ ":00");
            if(System.currentTimeMillis()>=endDate.getTime()){
                map.put("success", "fail");
                map.put("errorMsg", "生成活动失败!");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }catch (ParseException e){
            map.put("success", "fail");
            map.put("errorMsg", "活动时间已过期，请修改活动时间！");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        try {
            AutoResponsePojo result = activitiesService.generateActivivty(initActivities(enterpriseId, activityName, startTime,
                endTime, type, adminId, count), initActivityPrize(size, count));

            if (result!=null && result.getCode() == 200) {
                map.put("success", "success");
                map.put("errorMsg", "活动创建成功!");
            } else {
                map.put("success", "fail");
                map.put("errorMsg", result!=null?result.getMsg():"");
            }
        } catch (ParseException e) {
            logger.info("生成活动失败");
            map.put("success", "fail");
            map.put("errorMsg", "生成活动失败!");

        }
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    private Activities initActivities(Long enterpriseId, String activityName, String startTime,
                                      String endTime, Integer type, Long adminId, Long count) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Activities activities = new Activities();
        activities.setEntId(enterpriseId);
        activities.setName(activityName);
        activities.setStartTime(sdf.parse(startTime + ":00"));
        activities.setEndTime(sdf.parse(endTime + ":00"));
        activities.setType(type);
        activities.setCreatorId(adminId);
        activities.setPrizeCount(count);
        return activities;
    }

    private ActivityPrize initActivityPrize(Long size, Long count) {
        ActivityPrize activityPrize = new ActivityPrize();
        //如果是拼手气红包，则是流量币总量
        activityPrize.setSize(size);
        activityPrize.setCount(count);
        return activityPrize;
    }

    /**
     * @param queryObject
     * @param res
     * @param type
     */
    @RequestMapping("history_orders")
    public void historyOrders(QueryObject queryObject, HttpServletResponse res, String type) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("status", queryObject);
        Manager manager = getCurrentUserManager();
        List<Activities> records = null;
        Long count = 0L;
        if (manager != null && !StringUtils.isEmpty(type)) {
            //个人红包以活动开始日期进行检索，而不是活动创建日期
            if (!StringUtils.isEmpty(getRequest().getParameter("startDate"))) {
                queryObject.getQueryCriterias().put("activityStartTime", getRequest().getParameter("startDate"));
                queryObject.getQueryCriterias().put("activityEndTime", getRequest().getParameter("endDate") + " 23:59:59");
            }
            queryObject.getQueryCriterias().put("managerId", manager.getId());
            queryObject.getQueryCriterias().put("type", type);
            queryObject.getQueryCriterias().put("creatorId", getCurrentUser().getId());

            records = activitiesService.selectByMap(queryObject.toMap());
            count = activitiesService.countByMap(queryObject.toMap());
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", records);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param map
     * @param activityId
     * @return
     */
    @RequestMapping("detail")
    public String detail(ModelMap map, String activityId) {

        Activities activities = activitiesService.selectByActivityId(activityId);
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);

        map.addAttribute("activities", activities);
        map.addAttribute("activityInfo", activityInfo);
        return "individual/processing/redpacket-detail.ftl";
    }

    /**
     * @param resp
     * @param activityId
     * @return
     * @throws
     * @Title:getQRCode
     * @Description: 生成二维码
     */
    @RequestMapping("qcode")
    public void getQRCode(HttpServletResponse resp, String activityId) {
        resp.setContentType("image/png");

        try {
            int nWidth = 256;

            ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);

            String url = activityInfo.getUrl();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//			hints.put(EncodeHintType.MARGIN, 0);

            int nMargin = 1; //定义边框大小
            BitMatrix bitMatrix = multiFormatWriter.encode(url,
                BarcodeFormat.QR_CODE, nWidth, nWidth, hints);
            bitMatrix =  updateBit(bitMatrix, nMargin);

            MatrixToImageWriter.writeToStream(bitMatrix, "png",
                resp.getOutputStream());
        } catch (WriterException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * @param matrix
     * @param margin
     * @return
     */
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

    /**
     * @param queryObject
     * @param res
     * @param activityId
     */
    @RequestMapping("records")
    public void records(QueryObject queryObject, HttpServletResponse res, String activityId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("mobile", queryObject);
        List<ActivityWinRecord> records = null;
        Long count = 0L;
        if (!StringUtils.isEmpty(activityId)) {
            queryObject.getQueryCriterias().put("activityId", activityId);
            records = activityWinRecordService.selectByMapForRedpacket(queryObject.toMap());
            count = activityWinRecordService.countByMapForRedpacket(queryObject.toMap());
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", records);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param response
     * @param id
     * @throws IOException
     */
    /** 
     * @Title: closeActivityAjax 
     */
    @RequestMapping("closeActivityAjax")
    public void closeActivityAjax(HttpServletResponse response, Long id) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (id != null) {
            Activities activities = activitiesService.selectByPrimaryKey(id);
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(activities.getActivityId());
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activities.getActivityId());
            if (activityInfo != null && activityPrizes != null && activityPrizes.size() > 0) {
                if (activitiesService.closeActivity(activities, activityInfo, activityPrizes)) {
                    map.put("success", "success");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            }
        }
        map.put("success", "fail");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }   
}
