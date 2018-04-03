package com.cmcc.vrp.province.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FlowCoinExchangeStatus;
import com.cmcc.vrp.enums.FlowcardChargeChannelType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.dao.ActivityWinRecordMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.module.CurrentActivityInfo;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.JudgeIspService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * Created by qinqinyan on 2016/8/17.
 */
@Service("activityWinRecordService")
public class ActivityWinRecordServiceImpl implements ActivityWinRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityWinRecordServiceImpl.class);
    @Autowired
    ActivityWinRecordMapper activityWinRecordMapper;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    JudgeIspService judgeIspService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    AdministerService administerService;

    @Override
    public boolean insertSelective(ActivityWinRecord record) {
        if (record == null) {
            return false;
        }
        return activityWinRecordMapper.insertSelective(record) == 1;
    }

    @Override
    public ActivityWinRecord selectByPrimaryKey(Long id) {
        return activityWinRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public ActivityWinRecord selectByRecordId(String recordId) {
        return activityWinRecordMapper.selectByRecordId(recordId);
    }

    @Override
    public boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg, String phone) {
        if (id == null || status == null) {
            LOGGER.error("无效的记录ID或充值状态. Id = {}, Status = {}.", id, status == null ? null : status.getCode());
            return false;
        }

        return activityWinRecordMapper.updateStatus(id, status.getCode(), errorMsg, phone) == 1;
    }

    @Override
    public boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errorMsg, String phone) {
        if (id == null || status == null) {
            LOGGER.error("无效的记录ID或充值状态. Id = {}, Status = {}.", id, status == null ? null : status.getCode());
            return false;
        }
        return activityWinRecordMapper.updateStatusAndStatusCode(id, status.getCode(), status.getStatusCode(), errorMsg, phone) == 1;
    }

    @Override
    public boolean updateByPrimaryKeySelective(ActivityWinRecord record) {
        return activityWinRecordMapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(ActivityWinRecord record) {
        record.setUpdateTime(new Date());
        return activityWinRecordMapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public boolean batchUpdateStatus(List<String> recordIds, String chargePhone, int newStatus, int oldStatus, Date winTime) {
        return activityWinRecordMapper.batchUpdateStatus(recordIds, chargePhone, newStatus, oldStatus, winTime) == recordIds.size();
    }
    

    @Override
    public boolean batchUpdateForFlowcard(List<String> recordIds, String chargePhone, int newStatus, int oldStatus,
            Date winTime, String channel) {
        // TODO Auto-generated method stub
        if(!StringUtils.isEmpty(channel)){
            channel = FlowcardChargeChannelType.fromValue(channel).getname();
        }
        return activityWinRecordMapper.batchUpdateForFlowcard(recordIds, chargePhone, newStatus, oldStatus, winTime, channel)==recordIds.size();
    }

    @Override
    public CurrentActivityInfo getCurrentActivityInfo(String activityId) {
        CurrentActivityInfo currentActivityInfo = new CurrentActivityInfo(0L, 0L, 0, 0);

        List<ActivityWinRecord> records = activityWinRecordMapper.getCurrentActivityInfo(activityId);
        HashSet<String> userSet = new HashSet<String>();
        if (records != null && records.size() > 0) {
            for (ActivityWinRecord record : records) {
                currentActivityInfo.setCount(currentActivityInfo.getCount() + 1);
                currentActivityInfo.setFlow(currentActivityInfo.getFlow() + record.getProductSize());
                if (record.getPrice() != null) {
                    currentActivityInfo.setMoney(currentActivityInfo.getMoney() + record.getPrice());
                }
                userSet.add(record.getOwnMobile());
            }
        }

        currentActivityInfo.setUserCount(userSet.size());

        //二维码活动，需要对所有参与人数进行统计
        Activities activity = activitiesService.selectByActivityId(activityId);
        if (activity.getType().intValue() == ActivityType.QRCODE.getCode().intValue()) {
            Map map = new HashMap();
            ArrayList<Integer> statusList = new ArrayList<Integer>();
            statusList.add(ChargeRecordStatus.INVALIDED.getCode());
            statusList.add(ChargeRecordStatus.WAIT.getCode());
            statusList.add(ChargeRecordStatus.PROCESSING.getCode());
            statusList.add(ChargeRecordStatus.COMPLETE.getCode());
            statusList.add(ChargeRecordStatus.FAILED.getCode());

            map.put("status", statusList);
            map.put("activityId", activityId);
            currentActivityInfo.setJoinCount(activityWinRecordMapper.countChargeMobileByMap(map));
        }

        return currentActivityInfo;
    }

    @Override
    public int showForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map map = queryObject.toMap();
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }
        return activityWinRecordMapper.showForPageResultCount(map);
    }

    @Override
    public List<ActivityWinRecord> showForPageResult(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map map = queryObject.toMap();
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }
        return activityWinRecordMapper.showForPageResult(map);
    }

    @Override
    public List<ActivityWinRecord> selectByMap(Map map) {
        
        return activityWinRecordMapper.selectByMap(map);
    }

    @Override
    public boolean batchInsertForFlowcard(String activityId, Long cmProductId, Long cuProductId, Long ctProductId,
                                          String cmMobileList, String cuMobileList, String ctMobileList) {
        if (StringUtils.isEmpty(activityId)) {
            return false;
        }
        List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();

        initActivityWinRecord(activityWinRecords, activityId, cmProductId, cmMobileList);
        initActivityWinRecord(activityWinRecords, activityId, cuProductId, cuMobileList);
        initActivityWinRecord(activityWinRecords, activityId, ctProductId, ctMobileList);

        if (activityWinRecords.size() > 0) {
            return activityWinRecordMapper.batchInsert(activityWinRecords) == activityWinRecords.size();
        }
        return false;
    }

    @Override
    public boolean batchInsertForQRcode(String activityId, ActivityInfo activityInfo) {
        if (StringUtils.isEmpty(activityId) || activityInfo == null || activityInfo.getPrizeCount() <= 0) {
            return false;
        }
        List<ActivityWinRecord> activityWinRecords = initActivityWinRecordForQRcode(activityId, activityInfo);
        return activityWinRecordMapper.batchInsert(activityWinRecords) == activityWinRecords.size();
    }

    private List<ActivityWinRecord> initActivityWinRecordForQRcode(String activityId, ActivityInfo activityInfo) {
        List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();
        for (int i = 0; i < activityInfo.getPrizeCount().intValue(); ++i) {
            ActivityWinRecord record = new ActivityWinRecord();
            record.setActivityId(activityId);
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setDeleteFlag(0);
            record.setStatus(ChargeRecordStatus.UNUSED.getCode());
            record.setRecordId(SerialNumGenerator.buildSerialNum());
            record.setIsp(null);
            record.setPrizeId(null);
            record.setOwnMobile(null);

            activityWinRecords.add(record);
        }
        return activityWinRecords;
    }

    private void initActivityWinRecord(List<ActivityWinRecord> activityWinRecords, String activityId, Long productId, String mobiles) {
        if (productId != null && !StringUtils.isEmpty(mobiles)) {
            String[] mobileArray = mobiles.split(",");

            String isp = judgeIspService.judgeIsp(mobileArray[0]);

            for (int i = 0; i < mobileArray.length; i++) {
                ActivityWinRecord record = new ActivityWinRecord();
                record.setActivityId(activityId);
                record.setIsp(isp);
                record.setPrizeId(productId);
                record.setDeleteFlag(0);
                record.setCreateTime(new Date());
                record.setUpdateTime(new Date());
                record.setOwnMobile(mobileArray[i]);
                record.setStatus(ChargeRecordStatus.UNUSED.getCode());
                record.setRecordId(SerialNumGenerator.buildSerialNum());

                activityWinRecords.add(record);
            }
        }
    }

    @Override
    public boolean downLoadPhones(HttpServletRequest request, HttpServletResponse response,
                                  Long operatorId, List<String> phoneList, String filename,
                                  String activityType) {
        if (operatorId == null) {
            return false;
        }

        // 输出文件流
        String activityPath = "";
        if (StringUtils.isEmpty(activityType)) {
            activityPath = getFlowcardFilePath();
        } else {
            activityPath = getQRcodeFilePath();
        }
        String path = activityPath + File.separator + operatorId + File.separator;
        LOGGER.info("reading file from " + path);

        //创建文件
        try {
            if (!createFile(path, filename)) {
                LOGGER.error("用户ID:" + operatorId + "创建文件失败");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        //写入txt
        try {
            contentToTxt(path + filename, phoneList);
        } catch (Exception e) {
            LOGGER.error("用户ID:" + operatorId + "写入txt失败,失败原因:" + e.getMessage());
            return false;
        }

        File file = new File(path + filename);
        try {
            flushData(file, response);
        } catch (Exception e) {
            LOGGER.error("用户ID:" + operatorId + "下载失败,失败原因:" + e.getMessage());
            return false;
        }
        //FileUtils.deleteQuietly(new File(getZipFilePath()));
        return true;
    }

    public String getFlowcardFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOTTERY_FLOWCARD_PATH.getKey());
    }

    /**
     * 二维码下载临时目录
     */
    public String getQRcodeFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOTTERY_QRCODE_PATH.getKey());
    }

    /**
     * 创建文件txt
     */
    private boolean createFile(String path, String filename) throws Exception {
        boolean flag = false;

        // 检查文件是否有效
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File file = new File(path + filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.error("创建txt文件失败：" + file.toString() + " ;失败原因:" + e.getMessage());
        }
        return true;
    }

    /**
     * 将手机号写入txt
     */
    private void contentToTxt(String filePath, List<String> phoneList) throws IOException {
        String s1 = new String();//内容更新
        BufferedReader input = null;
        BufferedWriter output = null;
        try {
            File f = new File(filePath);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
            input = new BufferedReader(isr);

            for (String phone : phoneList) {
                s1 += phone + "\r\n";
            }
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            output = new BufferedWriter(writerStream);
            output.write(s1);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 传输文件数据 <p>
     */
    private void flushData(File file, HttpServletResponse response) throws IOException {
        // 取得文件名。
        String filename = file.getName();
        String encoded = URLEncoder.encode(filename, "utf-8");
        // 清空response
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + encoded);
        response.addHeader("Content-Length", "" + file.length());
        // 以流的形式下载文件。
        byte[] fileData = FileUtils.readFileToByteArray(file);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(fileData);
        outputStream.flush();
    }

    @Override
    public List<ActivityWinRecord> selectByActivityId(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        return activityWinRecordMapper.selectByActivityId(activityId);
    }

    @Override
    public List<ActivityWinRecord> selectByActivityIdAndIsp(String activityId, String isp) {
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(isp)) {
            return null;
        }
        return activityWinRecordMapper.selectByActivityIdAndIsp(activityId, isp);
    }

    @Override
    public boolean deleteByActivityId(String activityId) {
        return activityWinRecordMapper.deleteByActivityId(activityId) > 0;
    }

    @Override
    public List<ActivityWinRecord> selectByMapForRedpacket(Map map) {
        return activityWinRecordMapper.selectByMapForRedpacket(map);
    }

    @Override
    public Long countByMapForRedpacket(Map map) {
        return activityWinRecordMapper.countByMapForRedpacket(map);
    }

    @Override
    public boolean batchInsertFlowcoinPresent(String activityId, String phonesNum, Long productId, String ownerMobile) {
        if (StringUtils.isEmpty(activityId)) {
            return false;
        }
        List<ActivityWinRecord> activityWinRecords = initFlowCoinPresentActivityWinRecord(activityId, phonesNum, productId, ownerMobile);

        if (activityWinRecords != null && activityWinRecords.size() > 0) {
            return activityWinRecordMapper.batchInsert(activityWinRecords) == activityWinRecords.size();
        }
        return false;
    }

    private List<ActivityWinRecord> initFlowCoinPresentActivityWinRecord(String activityId, String mobiles, Long productId, String ownerMobile) {
        if (!StringUtils.isEmpty(mobiles)) {
            String[] mobileArray = mobiles.split(",");

            List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();
            for (int i = 0; i < mobileArray.length; i++) {
                ActivityWinRecord record = new ActivityWinRecord();
                record.setActivityId(activityId);
                record.setPrizeId(productId);
                record.setDeleteFlag(0);
                record.setCreateTime(new Date());
                record.setUpdateTime(new Date());
                record.setChargeMobile(mobileArray[i]);
                record.setOwnMobile(ownerMobile);
                record.setStatus(FlowCoinExchangeStatus.PROCESSING.getCode());
                record.setRecordId(SerialNumGenerator.buildSerialNum());
                activityWinRecords.add(record);
            }
            return activityWinRecords;
        } else {
            return null;
        }
    }

    @Override
    public boolean updateForIndividualRedpacket(CallbackPojo pojo, String serialNum, Integer chargeStatus, String reason) {
        if (pojo != null && !StringUtils.isEmpty(serialNum) && chargeStatus != null && !StringUtils.isEmpty(reason)) {
            ActivityWinRecord record = initUpdateRecord(pojo, serialNum, chargeStatus, reason);
            if (updateByPrimaryKeySelective(record)) {
                return true;
            }
        }
        return false;
    }

    private ActivityWinRecord initUpdateRecord(CallbackPojo pojo, String serialNum, Integer chargeStatus, String reason) {
        ActivityWinRecord record = new ActivityWinRecord();
        record.setRecordId(serialNum);
        record.setChargeTime(new Date());
        record.setUpdateTime(new Date());
        record.setStatus(chargeStatus);
        record.setReason(reason);
        return record;
    }

    @Override
    @Transactional
    public boolean insertForIndividualRedpacket(CallbackPojo pojo, String serialNum) {
        if (pojo != null && !StringUtils.isEmpty(serialNum)) {
            //1、插入中奖纪录
            ActivityWinRecord record = initActivityWinRecord(pojo, serialNum);

            if (!insertSelective(record)) {
                LOGGER.info("插入中奖纪录失败-" + JSONArray.toJSONString(record));
                return false;
            }

            //2、扣减冻结账户
            Activities activities = activitiesService.selectByActivityId(pojo.getActiveId());
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(pojo.getActiveId());

            if (activities != null && activityPrizes != null && activityPrizes.size() > 0) {
                IndividualAccount individualAccount = individualAccountService.getAccountByOwnerIdAndProductId(activities.getId(),
                        activityPrizes.get(0).getProductId(), IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());

                if (individualAccount != null) {
                    if (!individualAccountService.changeAccount(individualAccount,new BigDecimal(Long.valueOf(pojo.getPrizeCount().toString())),
                            serialNum, (int)AccountRecordType.OUTGO.getValue(), "活动中奖，扣减活动账户", 
                            activities.getType(), 0)){
                        LOGGER.info("扣减活动账户-" + individualAccount.getId() + "-失败，扣减数量：" + pojo.getPrizeCount());
                        throw new RuntimeException();
                    }
                    LOGGER.info("插入中奖纪录成功-" + serialNum + " ;扣减活动账户成功-" + individualAccount.getId() + " ;扣减数量：" + pojo.getPrizeCount());
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean insertForIndividualFlowRedpacket(CallbackPojo pojo, String serialNum) {
        if (pojo != null && !StringUtils.isEmpty(serialNum)) {
            //1、插入中奖纪录
            ActivityWinRecord record = initActivityWinRecord(pojo, serialNum);

            if (!insertSelective(record)) {
                LOGGER.info("插入中奖纪录失败-" + JSONArray.toJSONString(record));
                return false;
            }

            //2、扣减流量账户(此处四川流量红包没有活动账户)
            Activities activities = activitiesService.selectByActivityId(pojo.getActiveId());
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(pojo.getActiveId());

            if (activities != null && activityPrizes != null && activityPrizes.size() > 0) {
                IndividualAccount individualAccount = individualAccountService.getAccountByOwnerIdAndProductId(activities.getCreatorId(),
                        activityPrizes.get(0).getProductId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
                if (individualAccount != null) {
                    if (!individualAccountService.changeAccount(individualAccount,new BigDecimal(Long.valueOf(pojo.getPrizeCount().toString())),
                            serialNum, (int)AccountRecordType.OUTGO.getValue(), "活动中奖，扣减账户", 
                            activities.getType(), 0)){
                        LOGGER.info("扣减账户-" + individualAccount.getId() + "-失败，扣减数量：" + pojo.getPrizeCount());
                        throw new RuntimeException();
                    }
                    LOGGER.info("插入中奖纪录成功-" + serialNum + " ;扣减账户成功-" + individualAccount.getId() + " ;扣减数量：" + pojo.getPrizeCount());
                    return true;
                }
            }
        }
        return false;
    }

    private ActivityWinRecord initActivityWinRecord(CallbackPojo pojo, String serialNum) {
        ActivityWinRecord record = new ActivityWinRecord();
        record.setRecordId(serialNum);
        record.setActivityId(pojo.getActiveId());
        record.setOwnMobile(pojo.getMobile());
        record.setChargeMobile(pojo.getMobile());
        record.setPrizeId(Long.parseLong(pojo.getPrizeId()));
        record.setWinTime(new Date());
        record.setStatus(ChargeRecordStatus.WAIT.getCode());
        record.setDeleteFlag(0);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());

        record.setSize(pojo.getPrizeCount().toString());  //中奖大小
        return record;
    }

    /**
     * @Title: showForPageResultCountIndividual
     * @Description: 个人流量币赠送详情列表分页
     */
    @Override
    public int showForPageResultCountIndividualPresent(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return activityWinRecordMapper.showForPageResultCountIndividualPresent(queryObject.toMap());
    }

    /**
     * @Title: showForPageResultIndividual
     * @Description: 个人流量币赠送详情列表
     */
    @Override
    public List<ActivityWinRecord> showForPageResultIndividualPresent(
            QueryObject queryObject) {
        return activityWinRecordMapper.showForPageResultIndividualPresent(queryObject.toMap());
    }

    @Override
    public boolean batchUpdate(List<ActivityWinRecord> records) {
        return records == null ? false : activityWinRecordMapper.batchUpdate(records) == records.size();
    }

    /**
     * @Title: updateStatusCode
     * @Description: TODO
     */
    @Override
    public boolean updateStatusCodeByRecordId(String recordId, String statusCode) {
        if (recordId != null) {
            return activityWinRecordMapper.updateStatusCodeByRecordId(recordId, statusCode) >= 0;
        }
        return false;
    }

    @Override
    public boolean batchUpdateStatusCodeByRecordId(List<String> sns, String statusCode) {
        if (sns != null && sns.size() > 0) {
            return activityWinRecordMapper.batchUpdateStatusCodeByRecordId(sns, statusCode) >= 0;
        }
        return false;
    }
    
    @Override
    public ActivityWinRecord selectByActivityIdAndMobile(String activityId, String mobile) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(activityId) && !StringUtils.isEmpty(mobile)) {
            return activityWinRecordMapper.selectByActivityIdAndMobile(activityId, mobile);
        }
        return null;
    }

    @Override
    public List<ActivityWinRecord> showWinRecords(Map map) {
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }
        if(map.get("payResult") != null){
            String payResult = (String) map.get("payResult");
            map.put("payResult", java.util.Arrays.asList(payResult.split(",")));
        }
        return activityWinRecordMapper.showWinRecords(map);
    }

    @Override
    public int countWinRecords(Map map) {
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }
        if(map.get("payResult") != null){
            String payResult = (String) map.get("payResult");
            map.put("payResult", java.util.Arrays.asList(payResult.split(",")));
        }
        return activityWinRecordMapper.countWinRecords(map);
    }

    /** 
     * 众筹流量领取页面根据手机号码、活动类型筛选众筹成功的记录
     * @Title: getWinRecordsForCrowdFunding 
     * @param map
     * @return
    */
    @Override
    public List<ActivityWinRecord> getWinRecordsForCrowdFunding(Map map) {
        // TODO Auto-generated method stub
        return activityWinRecordMapper.getWinRecordsForCrowdFunding(map);
    }

    @Override
    public List<ActivityWinRecord> selectAllWinRecords(Map map) {
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }
        if(map.get("payResult") != null){
            String payResult = (String) map.get("payResult");
            map.put("payResult", java.util.Arrays.asList(payResult.split(",")));
        }
        return activityWinRecordMapper.selectAllWinRecords(map);
    }

    
    /** 
     * @Title: countChargeMobileByActivityId 
     */
    @Override
    public int countChargeMobileByActivityId(String activityId){
        return activityWinRecordMapper.countChargeMobileByActivityId(activityId);
    }
    
    /** 
     * @Title: selectIndividualFlowRedpacketList 
     */
    @Override
    public List<ActivityWinRecord> selectIndividualFlowRedpacketList(Map map){
        return activityWinRecordMapper.selectIndividualFlowRedpacketList(map);
    }
    
    /** 
     * @Title: countIndividualFlowRedpacketList 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2017年4月20日
    */
    @Override
    public Integer countIndividualFlowRedpacketList(Map map){
        return activityWinRecordMapper.countIndividualFlowRedpacketList(map);
    }
    
    @Override
    public List<ActivityWinRecord> getWinRecordsForCrowdFundingByMap(Map map){
        return activityWinRecordMapper.getWinRecordsForCrowdFundingByMap(map);
    }

    @Override
    public List<String> selectActivityIdByMobile(String mobile) {
        return activityWinRecordMapper.selectActivityIdByMobile(mobile);
    }
}




