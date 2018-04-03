package com.cmcc.vrp.pay.service.impl;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.email.model.EmailSender;
import com.cmcc.vrp.enums.SchedulerGroup;
import com.cmcc.vrp.enums.YqxReconcileStatus;
import com.cmcc.vrp.pay.enums.PayMethodType;
import com.cmcc.vrp.pay.model.PayBillModel;
import com.cmcc.vrp.pay.service.PayYqxReconcileService;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.quartz.jobs.YqxPayReconcileJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;


/**
 * PayYqxReconcileServiceImpl
 *
 */
@Service("payYqxReconcileService")
public class PayYqxReconcileServiceImpl implements PayYqxReconcileService {
    
    private static final Logger logger = LoggerFactory.getLogger(PayYqxReconcileServiceImpl.class);
    
    protected final String billPlusFileName = "billPlus";
    
    protected final String dbPlusFileName = "dbPlus";
    
    protected final String confilctFileName = "confilct";
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    YqxPayRecordService yqxPayRecordService;
    
    @Autowired
    GlobalConfigService globalConfigService;

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
        scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "YqxPayDailyReconcileJob");
        
        /**
         * 如果使用对账，则启动时增加
         */
        if(isUse()){
            scheduleService.createCronTrigger(YqxPayReconcileJob.class, dailyCronExp, 
                    "YqxPayDailyReconcileJob", SchedulerGroup.DEFAULT.getCode());
        }
    }
    
    @Override
    public boolean doDailyJob() {

        String date =  new DateTime().minusDays(getDayBefore()).toString("yyyy-MM-dd");

        //1.下载文件
        if(!isTestEnvironment() && !downloadFile(date)){
            logger.error("下载" + date +"账单失败");
            return false;
        }
        
        //2.从数据库得到当天所有数据
        List<YqxPayRecord> dbList = yqxPayRecordService.reconcileRangeTime(date);
        
        //3.对账开始，包括读取账单，对账逻辑和将错误信息打印
        File billPlusFile = new File(getLocalTargetFilePath() + File.separator +
                billPlusFileName + date +".txt");
        BufferedWriter billPlusWriter = null;
        
        File dbPlusFile = new File(getLocalTargetFilePath() + File.separator +
                dbPlusFileName + date +".txt");
        BufferedWriter dbPlusFileWriter = null;
        
        File confilctFile = new File(getLocalTargetFilePath() + File.separator +
                confilctFileName + date +".txt");
        BufferedWriter confilctWriter = null;
        
        try {
            billPlusWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(billPlusFile),
                    "utf-8"));
            dbPlusFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dbPlusFile),
                    "utf-8"));
            confilctWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(confilctFile),
                    "utf-8"));
            Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
            writerMap.put(billPlusFileName, billPlusWriter);
            writerMap.put(dbPlusFileName, dbPlusFileWriter);
            writerMap.put(confilctFileName, confilctWriter);
            
            //3.0.1 读取所有下载的账单
            Map<String,PayBillModel> billsMap = readFromBills(date.replaceAll("-", ""),billPlusWriter);
            //3.0.2 对账逻辑
            reconcileProcess(billsMap , dbList,writerMap);
            
        }catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (billPlusWriter != null) {
                try {
                    billPlusWriter.close();
                } catch (IOException e) {
                }
            }
            if (dbPlusFileWriter != null) {
                try {
                    dbPlusFileWriter.close();
                } catch (IOException e) {
                }
            }
            if (confilctWriter != null) {
                try {
                    confilctWriter.close();
                } catch (IOException e) {
                }
            }
        }

        //4.批量更新db
        updateDbAndRecharge(dbList);
        
        //5.发送邮件
        if(!isTestEnvironment()){
            sendEmail(date);
        }
        
        return true;
    }
    
    /**
     * date格式为YYYYMMDD,请调用前转化完成
     */
    protected boolean downloadFile(String date){
        String dateFormate = date.replaceAll("-", "");
        String fileNamePrefix = "PAY_" + dateFormate;
        
        //创建文件夹（不存在则创建）
        String directoryPath = getLocalBillFilePath() + File.separator +dateFormate;
        File directoryFile = new File(directoryPath);
        
        if(!(directoryFile.exists() && directoryFile.isDirectory())){
            if(!directoryFile.mkdir()){
                logger.error("生成文件夹失败，路径" + directoryPath);
                return false;
            }
        }
        
        //从FTP上下载指定文件到本地服务器指定目录
        String billFileName = FTPUtil.downFileFuzzyName(getFtpUrl(), getFtpPort(), getFtpLoginName(), getFtpLoginPass(),
                dateFormate, fileNamePrefix, directoryPath);
        
        if(StringUtils.isBlank(billFileName)){
            logger.error("下载云企信对账账单失败,日期" + date);
            return false;
        }     
        return true;
    }
    
    /**
     * 从所有的账单中读取合法记录，保存到内存中，返回Map
     * date格式为YYYYMMDD,请调用前转化完成
     * 文件格式例如:PAY_20170612.1.txt
     */
    protected Map<String,PayBillModel> readFromBills(String date, BufferedWriter billPlusWriter){

        Map<String,PayBillModel> map = new HashMap<String, PayBillModel>();
        for(int i=1;;i++){
            BufferedReader reader = null;
            try{
                //对账单文件名格式：PAY_yyyymmdd.{?}.txt
                String fileName = "PAY_" + date + "." + i +".txt";
                String billFileName = getLocalBillFilePath() + File.separator + date + File.separator + fileName;
                File file = new File(billFileName);
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String tempString = null;
                while ((tempString = reader.readLine()) != null) {
                    if(StringUtils.isBlank(tempString)){//跳过空行
                        continue;
                    }
                    PayBillModel model = convertStrToModel(tempString,billPlusWriter);
                    if(model != null){//将Orderid|TransactionId作为key保存到map中
                        String key = model.getOrderid() + "|" + model.getTransactionId();
                        map.put(key, model);
                        model.setDate(date);
                        yqxPayRecordService.insertYqxBill(model);
                    }
                }
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
    }
    
    /**
     * 将账单中每行的内容转化为PayBillModel
     */
    private PayBillModel convertStrToModel(String line,BufferedWriter billPlusWriter){
        PayBillModel model = PayBillModel.readFromLineNew(line);
        if(!checkParamsValid(model)){
            writeContents(billPlusWriter, "错误的账单记录."+line);
            return null;
        }
        return model;   
    }
    
    /**
     * 检测params中的内容是否合法
     */
    private boolean checkParamsValid(PayBillModel model){
        if(model ==null){
            return false;
        }
        return true;
    }
    
    /**
     * reconcileProcess 对账逻辑
     */
    protected boolean reconcileProcess(Map<String,PayBillModel> billsMap , List<YqxPayRecord> dbList,Map<String, BufferedWriter> writerMap){
        //存放账单比db多的记录
        BufferedWriter billPlusWriter = writerMap.get(billPlusFileName);

        //存放db比账单多的记录
        BufferedWriter dbPlusFileWriter = writerMap.get(dbPlusFileName);

        //存放两者冲突的记录
        BufferedWriter confilctWriter = writerMap.get(confilctFileName);
        
        if(billPlusWriter ==null || dbPlusFileWriter == null || confilctWriter==null){
            return false;
        }
        
        for(YqxPayRecord dbRecord : dbList){
            String key = dbRecord.getPayOrderId() + "|" + dbRecord.getPayTransactionId();
            
            if(billsMap.containsKey(key)){//账单找到匹配流水号记录的对象
                //比较 支付金额、支付状态、支付方式一致
                //支付订单流水号、支付金额一致，平台支付状态为失败、未支付、支付中，则更改平台支付状态为支付成功，对账失败
                PayBillModel bill = billsMap.get(key);
                
                String errMsg = checkPaySoldeMethod(bill,dbRecord);
                if(StringUtils.isBlank(errMsg)){//支付方式一致
                    if(dbRecord.getStatus().equals(0)){
                        dbRecord.setReconcileStatus(YqxReconcileStatus.SUCCESS.getCode());
                        dbRecord.setReconcileMsg("对账成功");
                    }else{          
                        writeContents(confilctWriter, "支付状态不一致,数据库id=" + dbRecord.getId()+",原状态为"+dbRecord.getStatus());
                        dbRecord.setNeedChangeDbStatus(true);
                        dbRecord.setStatus(0);
                        dbRecord.setDoneCode(bill.getPayOrderid());
                        dbRecord.setReconcileStatus(YqxReconcileStatus.FAILED.getCode());
                        dbRecord.setReconcileMsg("支付状态不一致");           
                    } 
                }else{
                    writeContents(confilctWriter, "支付信息不一致,数据库id=" + dbRecord.getId()+",错误信息为"+errMsg);
                    dbRecord.setReconcileStatus(YqxReconcileStatus.FAILED.getCode());
                    dbRecord.setReconcileMsg(errMsg);
                }
                billsMap.remove(key);//账单已找到匹配，删除该记录
                                
            }else{//账单未找到匹配流水号记录的对象
                writeContents(dbPlusFileWriter, "数据库id:" + dbRecord.getId()+",详细信息为:" + new Gson().toJson(dbRecord));
                dbRecord.setReconcileStatus(YqxReconcileStatus.FAILED.getCode());
                dbRecord.setReconcileMsg("账单无法找到相应记录");
            }
        }
        
        //对账剩余的账单部分,进行输出
        for(String key:billsMap.keySet()){
            PayBillModel model = billsMap.get(key);
            //打印到文件中
            writeContents(billPlusWriter, "db中无法找到记录."+model.getLine());
        }
        return true;
    }
    
    /**
     * 写到文件中
     */
    protected boolean writeContents(BufferedWriter writer, String content) {
        try {
            writer.write(content + "\r\n");
            logger.error("打印到文件：" + content);
            writer.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

    }

    /**
     * 更新数据库对账状态，以及重新充值
     */
    protected void updateDbAndRecharge(List<YqxPayRecord> dbList){
        for(YqxPayRecord record : dbList){
            //service update逻辑,更新支付状态，doneCode，对账状态，对账信息
            yqxPayRecordService.updateReconcileInfo(record);
            
            //如果更改了支付状态，则需要重新置入充值队列中,待确认
            /*if(record.isNeedChangeDbStatus()){
                
            }*/
        }
    }
    
    /**
     * 按找日期名称发送邮件
     */
    protected void sendEmail(String date){
        List<String> folderList = new ArrayList<String>();
        folderList.add(getLocalBillFilePath());
        folderList.add(getLocalTargetFilePath());
        
        //主题
        String emailSubject = "云企信支付每日账单文件" ;
        
        //内容
        String emailMessage = "云企信支付每日账单文件见附件。";
        
        //1、获取资源
        List<File> fileMailList = new ArrayList<File>();
        
        for(String filePath:folderList){
            File folder = new File(filePath);
            if(!folder.isDirectory()){
                break;
            }
            
            File[] fileList = folder.listFiles();
            addFileToList(fileMailList , fileList ,date);
        }
        
        if(fileMailList.size() <= 0){
            logger.info("目录不存在文件");
            emailMessage = "今日未生成文件。";
        }
        
        //2、发送邮件
        String [] result = new EmailSender(getEmailHost(), getEmailSender(), getEmailReceiver(), emailSubject, emailMessage, fileMailList, 
                getEmailUsername(), getEmailPassword(), getEmailPort()).sendMail();
        
        if(result!=null && result.length >=2){
            logger.info("邮件发送结果：" + result[1]);
        }

    }
    
    /**
     * 将指定文件夹下的所有文件，根据名字是否匹配，添加到发送邮件List中
     */
    private void addFileToList(List<File> fileMailList , File[] fileList ,String date){
        if(fileList != null && fileList.length > 0){
            for (int i = 0; i < fileList.length; i++) {
                String fileName = fileList[i].getName();
                //名字包含yyyy-mm-dd或yyyymmdd就发送该附件
                if(StringUtils.contains(fileName, date) || StringUtils.contains(fileName, date.replaceAll("-", ""))){
                    fileMailList.add(fileList[i]);
                }
            }
        }
    }
            
    
    /**
     * 比较 支付金额、支付方式一致
     */
    private String checkPaySoldeMethod(PayBillModel bill,YqxPayRecord dbRecord){
        String errMsg = "";
        if(dbRecord.getPayPrice() == null){
            errMsg+="支付金额不一致"; 
        }else{
            String payPrice = String.format("%.2f", dbRecord.getPayPrice().doubleValue()/100);
            if(!payPrice.equals(bill.getPayAmount())){
                errMsg+="支付金额不一致"; 
            }
        }
        
        //bill.getType()1、和包；2、支付宝；4、微信；5、银联
        //dbRecord.getPayType()支付类型：1-微信，2-支付宝
        
        if(dbRecord.getPayType().equals(2) && PayMethodType.ALIPAY.getCode().equals(bill.getType())){
            return errMsg;
        }else if (dbRecord.getPayType().equals(1) && PayMethodType.WECHAT.getCode().equals(bill.getType())){
            return errMsg;
        }else{
            errMsg +="支付方式不一致";
            return errMsg;
        }
    }
    
    /**
     * 是否使用对账
     */
    protected boolean isUse(){
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_IS_USE.getKey()));
    }

   
    /**
     * 得到对账时间,n天以前
     */
    protected int getDayBefore() {
        return 2;
    }

    /**
     * getFtpUrl
     */
    protected String getFtpUrl(){
        //return "127.0.0.1";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_FTP_URL.getKey());
    }
    
    /**
     * getFtpPort
     */
    protected int getFtpPort(){
        //return NumberUtils.toInt("21");
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_FTP_PORT.getKey()));
    }
    
    /**
     * getLoginName
     */
    protected String getFtpLoginName() {
        //return "qihang";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_FTP_LOGINNAME.getKey());
    }

    /**
     * getFtpLoginPass
     */
    protected String getFtpLoginPass() {
        //return "xiaoqi160";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_FTP_LOGINPASS.getKey());
    }

    /**
     * ftp存放账单路径
     */
    protected String getLocalBillFilePath() {
        //return "G:\\yqxpay\\bill";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_FTP_LOCALPATH.getKey());
    }
    
    /**
     * ftp存放生成文件
     */
    protected String getLocalTargetFilePath() {
        //return "G:\\yqxpay\\changeRecords";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_FTP_TARGETPATH.getKey());
    }

    /**
     * 是否测试环境,如果是测试环境，不会从ftp下载账单，和发送邮件
     */
    protected boolean isTestEnvironment(){
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_IS_TEST.getKey()));
    }
    
    /**
     * getEmailHost
     */
    private String getEmailHost(){
        //return "smtp.chinamobile.com";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_EMAIL_HOST.getKey());
    }
    
    /**
     * getEmailSender
     */
    private String getEmailSender(){
        //return "luozuwu@cmhi.chinamobile.com";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_EMAIL_SENDER.getKey());
    }
    
    /**
     * getEmailReceiver
     */
    private String getEmailReceiver(){
        //return "luozuwu@cmhi.chinamobile.com";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_EMAIL_PORT.getKey());
    }
    
    /**
     * getEmailUsername
     */
    private String getEmailUsername(){
        //return "luozuwu";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_EMAIL_USERNAME.getKey());
    }
    
    /**
     * getEmailPassword
     */
    private String getEmailPassword(){
        //return "luozuwu123";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_EMAIL_PASS.getKey());
    }
    
    /**
     * getEmailPort
     */
    private String getEmailPort(){
        //return "25";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_RECONCILE_EMAIL_PORT.getKey());
    }
    
}
