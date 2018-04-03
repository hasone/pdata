package com.cmcc.vrp.province.reconcile.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.email.model.EmailSender;
import com.cmcc.vrp.enums.SchedulerGroup;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.quartz.jobs.JSReconcileJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.reconcile.model.BillModel;
import com.cmcc.vrp.province.reconcile.model.DBModel;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.FtpProperties;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 江苏的对账实现类，继承与BasicReconcileServiceImpl
 *
 */
@Service("jxReconcileService")
public class JsReconcileServiceImpl extends BasicReconcileServiceImpl {
    
    private static final Logger logger = LoggerFactory.getLogger(JsReconcileServiceImpl.class);

    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    
    @Autowired
    private ScheduleService scheduleService;
    
    private static final DateTimeFormatter DATETIMEFORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    
    /**
     * 初始化对账任务
     * isUse() 为是否使用
     * isTestEnvironment() 为测试还是线上环境
     */
    @PostConstruct
    public void init(){
        String dailyCronExp = "";
        if(isTestEnvironment()){
            dailyCronExp = "0 0/5 * * * ?";
        }else{
          //线上每日对账，每日4点半执行    
            dailyCronExp = "0 30 4 * * ? *";
        }
        
        //取消掉原任务，若不存在直接返回
        scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "JSPayDailyReconcileJob");
        
        /**
         * 如果使用对账，则启动时增加
         */
        if(isUse()){
            scheduleService.createCronTrigger(JSReconcileJob.class, dailyCronExp, 
                    "JSPayDailyReconcileJob", SchedulerGroup.DEFAULT.getCode());
        }
    }
    
    /**
     * 输出格式
     */
    private final String errPattern = "手机号:{0},产品编码:{1},订购时间：{2}，流水号:{3},错误原因:{4}";
    
    /**
     * 继承于BasicReconcileServiceImpl的方法
     */
    @Override
    protected boolean reconcileProcess(Map<String, BillModel> billDataMap,List<DBModel> dbDatas,String date){
        File conflictFile = new File(getLocalTargetFilePath() + File.separator + "conflict" + date + ".txt");
        BufferedWriter conflictWriter = null;
        try{
            conflictWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(conflictFile),
                    "utf-8"));
  
            //将db数据与bill数据匹配，删除billMap已找到的数据
            for(DBModel dbModel : dbDatas){
                String keyWordDb = getKeyWordDb(dbModel);//流水号
                if(billDataMap.containsKey(keyWordDb)){
                    //验证手机号和产品编码是否匹配
                    BillModel billModel = billDataMap.get(keyWordDb);
                    
                    String errMsg ="";
                    if(!billModel.getPhone().equals(dbModel.getPhone())){
                        errMsg += "手机号不一致";
                    }
                    if(!billModel.getPrdCode().equals(dbModel.getPrdCode())){
                        errMsg += "产品编码不一致";
                    }
                    
                    if(StringUtils.isNotBlank(errMsg)){
                        String outputData = MessageFormat.format(errPattern, 
                                dbModel.getPhone(),
                                dbModel.getPrdCode(),
                                new DateTime(dbModel.getChargeTime()).toString("yyyyMMddHHmmss"),
                                keyWordDb,
                                errMsg);
                        logger.info(outputData);
                        writeContents(conflictWriter, outputData);
                    }
                    
                    billDataMap.remove(keyWordDb);//map中删除已匹配记录
                    
                }else{//没有找到匹配到直接输出该条数据库记录
                    String outputData = MessageFormat.format(errPattern, 
                            dbModel.getPhone(),
                            dbModel.getPrdCode(),
                            new DateTime(dbModel.getChargeTime()).toString("yyyyMMddHHmmss"),
                            keyWordDb,
                            "账单中没有该流水号相关记录");
                    logger.info(outputData);
                    writeContents(conflictWriter, outputData);
                }
            }
            //billMap中遗留的数据，认为是db中没有的，全部打印到错误文件中
            for(String key:billDataMap.keySet()){
                BillModel billModel = billDataMap.get(key);
                String outputData = MessageFormat.format(errPattern, 
                        billModel.getPhone(),
                        billModel.getPrdCode(),
                        billModel.getChargeTime(),
                        billModel.getBossRespSeq(),
                        "数据库中没有该流水号相关记录");
                logger.info(outputData);
                writeContents(conflictWriter, outputData);
            }
            
        }catch (IOException e) {
            logger.error(e.getMessage());
        }finally {
            if (conflictWriter != null) {
                try {
                    conflictWriter.close();
                } catch (IOException e) {
                }
            }
        }
        
        
        return true;
    }
    
    @Override
    int getDayBefore() {
        return 1;
    }

    /**
     * 格式：手机号码|套餐编码|办理时间|crm订购实例ID|订单号 
     * 取1,2,3,5位， 订单号是boss返回的流水号
     */
    @Override
    Map<String, BillModel> analyseBill(String date) {
        String fileName = getDownLoadFileName(date);
        Map<String, BillModel> map = new HashMap<String, BillModel>();
        BufferedReader reader = null;
        try{
            File file = new File(getLocalBillFilePath() + File.separator +fileName);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String[] params = tempString.split("\\|");
                if(params.length<5 || StringUtils.isBlank(params[4])){
                    logger.error("错误的账单记录:"+tempString);
                }else{                    
                    BillModel model = new BillModel();
                    model.setPhone(params[0]);
                    model.setPrdCode(params[1]);
                    model.setChargeTime(params[2]);
                    model.setBossRespSeq(params[4]);
                    
                    map.put(model.getBossRespSeq(), model);                    
                }
            }
            return map;
            
        }catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return map;
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return map;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return map;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    List<DBModel> getDatasFromDb(String date) {       
        
        //date格式
        DateTime dateTime = DateTime.parse(date, DATETIMEFORMAT); 
        
        Date startTimeDate = dateTime.withTimeAtStartOfDay().minusMinutes(5).toDate();//前一天23:55:55
        Date endTimeDate= dateTime.millisOfDay().withMaximumValue().toDate();//当天23:59:59
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billStartTime", startTimeDate);
        params.put("billEndTime", endTimeDate);

        return chargeRecordMapper.jsReconcileDatasWithRespSeq(params);
    }

    @Override
    String getKeyWordDb(DBModel model) {
        return model.getBossRespSerialNum();
    }

    @Override
    boolean isTestEnvironment() {
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_IS_TEST.getKey()));
    }

    @Override
    String getDownLoadFileName(String date) {
        return "boss2llpt_" + getPlusOneDayStr(date).replaceAll("-", "") + ".txt";
    }

    @Override
    FtpProperties initFtpProperties(String date) {
        FtpProperties ftpProperties = new FtpProperties();
        ftpProperties.setUrl(getFtpUrl());
        ftpProperties.setPort(getFtpPort());
        ftpProperties.setUsername(getFtpLoginName());
        ftpProperties.setPassword(getFtpLoginPass());
        ftpProperties.setRemotePath(getFtpRemotePath());
        ftpProperties.setLocalPath(getLocalBillFilePath());
        return ftpProperties;
    }

    @Override
    EmailSender initEmailSender(List<File> fileMailList) {
        String emailMessage;
        if(fileMailList == null || fileMailList.isEmpty()){
            emailMessage = "江苏每日对账文件未生成任何文件";
        }else{
            emailMessage = "江苏每日对账文件请见附件";
        }
        
        return new EmailSender(getEmailHost(), getEmailSender(), getEmailReceiver(), "江苏每日对账文件", emailMessage, 
                fileMailList, getEmailUsername(), getEmailPassword(), getEmailPort());
    }
    
    /**
     * override 上传到ftp指定路径
     */
    @Override
    protected boolean doOthers(String reconcileDate){
        if(isTestEnvironment()){
            logger.info("测试环境，不会上传文件到ftp服务器");
            return true;
        }
        
        String fileName = getLocalTargetFilePath() + File.separator + "conflict" + reconcileDate + ".txt";

        FileInputStream in = null;
        try {
            File file = new File(fileName);
            in = new FileInputStream(file);
            
            //ftp将文件上传给开放平台
            boolean flag = FTPUtil.uploadFile(getFTPUrl(), getFTPPort(), getFTPLoginname(), getFTPLoginpass(),
                    "/crmRecords", file.getName(), in);
            if (!flag) {
                logger.error("上传到ftp服务器失败,文件名为: " + fileName);
                logger.error("{},{},{},{},{}",getFTPUrl(),getFTPPort(),getFTPLoginname(),getFTPLoginpass(),"/crmRecords");
                return false;
            } else {
                logger.error("上传到ftp服务器成功,文件名为: " + fileName);
            }

            
        }catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        
        String toDayDate = new DateTime().toString("yyyyMMdd");
        fileName = getLocalBillFilePath() + File.separator + "boss2llpt_" + toDayDate + ".txt";

        FileInputStream inBill = null;
        try {
            File file = new File(fileName);
            inBill = new FileInputStream(file);
            
            //ftp将文件上传给开放平台
            boolean flag = FTPUtil.uploadFile(getFTPUrl(), getFTPPort(), getFTPLoginname(), getFTPLoginpass(),
                    "/crmRecords", file.getName(), inBill);
            if (!flag) {
                logger.error("上传到ftp服务器失败,文件名为: " + fileName);
                logger.error("{},{},{},{},{}",getFTPUrl(),getFTPPort(),getFTPLoginname(),getFTPLoginpass(),"/crmRecords");
                return false;
            } else {
                logger.error("上传到ftp服务器成功,文件名为: " + fileName);
            }
            return true;
            
        }catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (inBill != null) {
                try {
                    inBill.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        

    }

    @Override
    String getLocalBillFilePath() {
      //return "G:\\yqxpay\\bill";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_LOCALPATH.getKey());
    }

    @Override
    String getLocalTargetFilePath() {
      //return "G:\\yqxpay\\changeRecords";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_TARGETPATH.getKey());
    }
    
    /**
     * 2017-06-26  ->  2017-06-27
     */
    private String getPlusOneDayStr(String date){
        DateTime dateTime = DateTime.parse(date, DATETIMEFORMAT);
        return dateTime.plusDays(1).toString(DATETIMEFORMAT);
    }
    
    
    /**
     * 是否使用对账
     */
    protected boolean isUse(){
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_IS_USE.getKey()));
    }


    /**
     * getFtpUrl
     */
    protected String getFtpUrl(){
        //return "127.0.0.1";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_URL.getKey());
    }
    
    /**
     * getFtpPort
     */
    protected int getFtpPort(){
        //return NumberUtils.toInt("21");
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_PORT.getKey()));
    }
    
    /**
     * getLoginName
     */
    protected String getFtpLoginName() {
        //return "qihang";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_LOGINNAME.getKey());
    }

    /**
     * getFtpLoginPass
     */
    protected String getFtpLoginPass() {
        //return "xiaoqi160";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_LOGINPASS.getKey());
    }

    
    /**
     * getEmailHost
     */
    private String getEmailHost(){
        //return "smtp.chinamobile.com";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_EMAIL_HOST.getKey());
    }
    
    /**
     * getEmailSender
     */
    private String getEmailSender(){
        //return "luozuwu@cmhi.chinamobile.com";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_EMAIL_SENDER.getKey());
    }
    
    /**
     * getEmailReceiver
     */
    private String getEmailReceiver(){
        //return "luozuwu@cmhi.chinamobile.com";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_EMAIL_PORT.getKey());
    }
    
    /**
     * getEmailUsername
     */
    private String getEmailUsername(){
        //return "luozuwu";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_EMAIL_USERNAME.getKey());
    }
    
    /**
     * getEmailPassword
     */
    private String getEmailPassword(){
        //return "luozuwu123";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_EMAIL_PASS.getKey());
    }
    
    /**
     * getEmailPort
     */
    private String getEmailPort(){
        //return "25";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_EMAIL_PORT.getKey());
    }
    
    /**
     * getFtpRemotePath
     */
    private String getFtpRemotePath(){
        //return "aaa";
        return globalConfigService.get(GlobalConfigKeyEnum.JS_RECONCILE_FTP_REMOTEPATH.getKey());
    }

    
    /**
     * 江苏开放平台对账ftp的url
     */
    private String getFTPUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FTP_URL.getKey());
    }
    
    /**
     * 江苏开放平台对账ftp的port
     */
    private int getFTPPort(){
        String port = globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FTP_PORT.getKey());
        return NumberUtils.toInt(port);
    }
    
    /**
     * 江苏开放平台对账ftp的LOGINNAME
     */
    private String getFTPLoginname(){
        return globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FTP_LOGINNAME.getKey());
    }
    
    /**
     * 江苏开放平台对账ftp的LOGINPASS
     */
    private String getFTPLoginpass(){
        return globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FTP_LOGINPASS.getKey());
    }
}
