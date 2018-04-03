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

import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.email.model.EmailSender;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.reconcile.model.BillModel;
import com.cmcc.vrp.province.reconcile.model.DBModel;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.FtpProperties;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 江苏的对账实现类，继承与BasicReconcileServiceImpl
 * ps:本文件是之前江苏只对账手机号码个数的备份文件
 */
@Service("jxReconcileBakService")
public class JsReconcileServiceBakImpl extends BasicReconcileServiceImpl {
    
    private static final Logger logger = LoggerFactory.getLogger(JsReconcileServiceBakImpl.class);

    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    
    @Autowired
    private ScheduleService scheduleService;
    
    private static final DateTimeFormatter DATETIMEFORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
        
    /**
     *@PostConstruct
     *public void init(){
     *   String dailyCronExp = "";
     *   if(isTestEnvironment()){
     *       dailyCronExp = "0 0/5 * * * ?";
     *   }else{
     *       dailyCronExp = "0 30 4 * * ? *";
     *   }
     *   
     *   scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "JSPayDailyReconcileJob");
     *   if(isUse()){
     *       scheduleService.createCronTrigger(JSReconcileJob.class, dailyCronExp, 
     *               "JSPayDailyReconcileJob", SchedulerGroup.DEFAULT.getCode());
     *   }
     *}
     **/
    
    /**
     * 输出格式
     */
    private final String errPattern = "手机号:{0},产品编码:{1},数据库个数：{2}，账单个数:{3},错误原因:{4}";
    
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
            for(DBModel dbModel : dbDatas){
                String keyWordDb = getKeyWordDb(dbModel);
                int dbCount = dbModel.getPhonePrdCount();
                if(billDataMap.containsKey(keyWordDb)){
                    //对账逻辑
                    
                    int billCount = billDataMap.get(keyWordDb).getPhonePrdCount();
                    
                    if(billCount != dbCount){//对账失败，个数不匹配
                        String errMsg = MessageFormat.format(errPattern,                              
                                dbModel.getPhone(),
                                dbModel.getPrdCode(),
                                dbCount,
                                billCount,
                                "个数不匹配");
                        logger.info(errMsg);
                        writeContents(conflictWriter, errMsg);
                    }
                    
                    billDataMap.remove(keyWordDb);
                }else{
                    int billCount = 0;
                    String errMsg = MessageFormat.format(errPattern, 
                            dbModel.getPhone(),
                            dbModel.getPrdCode(),
                            dbCount,
                            billCount,
                            "账单中不存在该手机号产品的记录");
                    logger.info(errMsg);
                    writeContents(conflictWriter, errMsg);
                }
            }
            for(String key:billDataMap.keySet()){
                BillModel billModel = billDataMap.get(key);
                
                String errMsg = MessageFormat.format(errPattern,
                        billModel.getPhone(),
                        billModel.getPrdCode(),
                        "0",
                        billModel.getPhonePrdCount(),
                        "数据库中中不存在该手机号产品的记录");
                logger.info(errMsg);
                writeContents(conflictWriter, errMsg);
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
     * 内容格式：手机号码|订购产品|订购时间|流水号
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
                if(params.length<4){
                    logger.error("错误的账单记录:"+tempString);
                }else{
                    String phone = params[0];
                    String prdCode = params[1];
                    
                    String key = phone + ":" + prdCode; 
                    if(map.containsKey(key)){//map包含key值 phone:prdCode 计数器+1
                        BillModel model  = map.get(key);
                        model.setPhonePrdCount(model.getPhonePrdCount() + 1);
                    }else{//不包含，则新加入map
                        BillModel model = new BillModel();
                        model.setPhone(phone);
                        model.setPrdCode(prdCode);
                        model.setPhonePrdCount(1);
                        map.put(key, model);
                    }
                    
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
        
        Date startTimeDate = dateTime.withTimeAtStartOfDay().toDate();//当天00:00:00
        Date endTimeDate= dateTime.millisOfDay().withMaximumValue().toDate();//当天23:59:59
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billStartTime", startTimeDate);
        params.put("billEndTime", endTimeDate);

        return chargeRecordMapper.getJsReconcileDatas(params);
    }

    @Override
    String getKeyWordDb(DBModel model) {
        return model.getPhone() + ":" + model.getPrdCode();
    }

    @Override
    boolean isTestEnvironment() {
        return false;
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
