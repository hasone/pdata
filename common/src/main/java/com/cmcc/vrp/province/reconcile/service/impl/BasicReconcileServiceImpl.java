package com.cmcc.vrp.province.reconcile.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.vrp.boss.shangdong.email.model.EmailSender;
import com.cmcc.vrp.province.reconcile.model.BillModel;
import com.cmcc.vrp.province.reconcile.model.DBModel;
import com.cmcc.vrp.province.reconcile.service.ReconcileService;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.FtpProperties;

/**
 * 对账服务类，对外只提供doDailyJob的方法
 * 
 * 该类提供了从ftp下载到最后邮件发送的所有方法，各省实现充值对账请继承该类即可.
 * 方法包含:
 * 1.从ftp下载账单
 * 2.分析账单，生成Map格式
 * 3.从数据库获取充值记录
 * 4.对账逻辑（最基础，请override）
 * 5.更新数据库（本类不更新）
 * 6.发送邮件
 * 
 * 如果流程不同，请自行增加方法或者override原方法
 *  
 * by qihang
 *
 */
public abstract class BasicReconcileServiceImpl implements ReconcileService {
    
    private static final Logger logger = LoggerFactory.getLogger(BasicReconcileServiceImpl.class);

    /**
     * 每日job
     */
    @Override
    public boolean doDailyJob() {
        //得到需要对账的日期，yyyy-MM-dd格式
        String reconcileDate =  new DateTime().minusDays(getDayBefore()).toString("yyyy-MM-dd");
        
        logger.info("对账开始" + reconcileDate);
        
        //下载账单
        if(!downloadBill(reconcileDate)){
            return false;
        }
        
        //分析账单
        Map<String, BillModel> billDataMap = analyseBill(reconcileDate);
        
        //读取数据库
        List<DBModel> dbDatas = getDatasFromDb(reconcileDate);
        
        //对账逻辑
        reconcileProcess(billDataMap,dbDatas,reconcileDate);
        
        //更新Db
        updateDb(dbDatas);
        
        //发送邮件
        //sendEmail(reconcileDate);
        doOthers(reconcileDate);
        
        logger.info("对账完成" + reconcileDate);
        
        return true;
    }
    
    

    /**
     * 基本的下载方法，下载文件名称包含getDownLoadFileName(date)的文件，放到指定的文件夹下，不更名。
     * 
     * 如果有特殊要求，请自行重载
     */
    protected boolean downloadBill(String date){
        if(isTestEnvironment()){
            logger.info("测试环境，不从ftp下载文件");
            return true;
        }
        String dlName = getDownLoadFileName(date);//得到下载文件名称
        
        //从ftp上下载指定文件，存放到服务器上的 getLocalBillFilePath()路径下，下载失败返回空
        String dlFileName = FTPUtil.downFileFuzzyName(initFtpProperties(date),dlName);
        
        if(StringUtils.isBlank(dlFileName)){
            logger.info("下载失败，文件名子字符串为" + dlName);
            return false;
        }
        
        logger.info("下载成功，文件名子字符串为" + dlName);
        return true;
    }
    
    /**
     * 基础对账逻辑，请按照该逻辑进行修改
     */
    protected boolean reconcileProcess(Map<String, BillModel> billDataMap,List<DBModel> dbDatas,String date){
        for(DBModel dbModel : dbDatas){
            String keyWordDb = getKeyWordDb(dbModel);
            if(billDataMap.containsKey(keyWordDb)){
                //对账逻辑
                billDataMap.remove(keyWordDb);
            }else{
                //doNothing
            }
        }
        for(String key:billDataMap.keySet()){
            BillModel billModel = billDataMap.get(key);
            logger.error("账单中未找到匹配项," + billModel.getLine());
            
        }
        return true;
    }
    
    /**
     * 更新db方法，默认不更新
     */
    protected boolean updateDb(List<DBModel> dbDatas){
        return true;
    }
    
    /**
     * 更新db方法，默认不更新
     */
    protected boolean doOthers(String reconcileDate){
        return true;
    }
    
    /**
     * 发送邮件，默认发送文件为包含YYYY-MM-DD或者yyyymmdd的所有文件
     */
    protected boolean sendEmail(String date){
        if(isTestEnvironment()){
            return true;
        }
        List<String> folderList = new ArrayList<String>();
        folderList.add(getLocalBillFilePath());
        folderList.add(getLocalTargetFilePath());
        
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
        
        //2、发送邮件
        EmailSender emailSender = initEmailSender(fileMailList);
        if(emailSender !=null){
            emailSender.setAttachmentlist(fileMailList);
            String [] result = emailSender.sendMail();
            
            if(result!=null && result.length >=2){
                logger.info("邮件发送结果：" + result[1]);
            }
        }
        return true;
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
     * 写到文件中
     */
    protected boolean writeContents(BufferedWriter writer, String content) {
        try {
            writer.write(content + "\r\n");
            logger.info("打印到文件：" + content);
            writer.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    /**
     * 得到需要对账几天前的账
     */
    abstract int getDayBefore();
    
    /**
     * 分析账单文件，返回map对象，
     * Map中 key为需要和db中匹配的键值,后续对账逻辑需要使用
     */
    abstract Map<String, BillModel> analyseBill(String date);
    
    /**
     * 从数据库得到数据,根据数据请自行拓展DBModel类
     */
    abstract List<DBModel> getDatasFromDb(String date);
    
    /**
     * 得到关键词，用于和Map<String, BillModel>匹配
     */
    abstract String getKeyWordDb(DBModel model);

    /**
     * 是否测试环境，如果是将不会下载文件和发送email的操作
     */
    abstract boolean isTestEnvironment();
    
    /**
     * 下载文件名称（包含的子字符串）
     */
    abstract String getDownLoadFileName(String date);
    
    /**
     * 初始化ftp属性
     */
    abstract FtpProperties initFtpProperties(String date);
    
    /**
     * initEmailSender
     */
    abstract EmailSender initEmailSender(List<File> fileMailList);
    
    /**
     * 账单保存到服务器本地的路径
     */
    abstract String getLocalBillFilePath();
    
    /**
     * 生成文件保存到服务器本地的路径
     */
    abstract String getLocalTargetFilePath();
}
