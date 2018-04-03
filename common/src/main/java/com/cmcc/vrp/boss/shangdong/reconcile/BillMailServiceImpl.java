package com.cmcc.vrp.boss.shangdong.reconcile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.email.model.EmailSender;
import com.cmcc.vrp.boss.shangdong.email.model.EmailType;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;


/** 
* @ClassName: EmailBillCronJob 
* @Description: 获取对账文件功能类
* @author: Rowe,qihang
* @date: 2016年7月28日 下午11:04:27
*/
@Service
public class BillMailServiceImpl implements BillMailService {

    private static final Logger LOGGER = LoggerFactory.
            getLogger(BillMailServiceImpl.class);
    
    @Autowired
    private GlobalConfigService globalConfigService;
    
    /**
     * 发送email类
     * date格式需要是每日为20161116，每月为201611
     * 
     * 
     */
    @Override
    public boolean sendEmail(String date, EmailType emailType,List<String> filePaths) {
        LOGGER.info("开始发送邮件");
        //主题
        String emailSubject = "山东每日账单文件" ;
        
        //内容
        String emailMessage = "山东每日账单文件见附件。";
        
        switch(emailType){
            case DAY_EMAIL_PACKAGE:
                emailSubject = "山东每日流量包账单文件";
                emailMessage = "山东每日流量包账单文件见附件。";
                break;
            case MONTH_EMAIL_PACKAGE:
                emailSubject = "山东每月流量包账单文件";
                emailMessage = "山东每月流量包账单文件见附件。";
                break;
            case DAY_EMAIL_CELL:
                emailSubject = "山东每日流量池用户使用账单文件";
                emailMessage = "山东每日流量池用户使用文件见附件。";
                break;
            case DAY_EMAIL_CELL_BOSS:
                emailSubject = "山东每日流量池BOSS订购账单文件";
                emailMessage = "山东每日流量池BOSS订购文件见附件。";
                break;
            default :
                break;
        }
        
        
        //1、获取资源
        List<File> list = new ArrayList<File>();
        
        for(String filePath:filePaths){
            File folder = new File(filePath);
            if(!folder.isDirectory()){
                break;
            }
            
            File[] fileList = folder.listFiles();
            addFileToList(list , fileList ,date,emailType);
        }
                
        if(list.size() <= 0){
            LOGGER.info("目录不存在文件");
            emailMessage = "今日未生成文件。";
        }
            
        //2、发送邮件
        String [] result = new EmailSender(getEmailHost(), getEmailSender(), getEmailReceiver(), emailSubject, emailMessage, list, 
                getEmailUsername(), getEmailPassword(), getEmailPort()).sendMail();
        /*String [] result = new EmailSender("smtp.chinamobile.com", 
                "luozuwu@chinamobile.com", 
                "luozuwu@chinamobile.com,fromluozuwu@qq.com", 
                emailSubject, emailMessage, list, "luozuwu", "luozuwu123", "25").sendMail();*/
        
        if(result!=null && result.length >=2){
            LOGGER.info("邮件发送结果：" + result[1]);
        }
        return true;
    }
    
    /**
     * 将指定文件夹下的所有文件，根据名字是否匹配，添加到发送邮件List中
     */
    private void addFileToList(List<File> fileMailList , File[] fileList ,String date,EmailType emailType){

        if(fileList != null && fileList.length > 0){
            for (int i = 0; i < fileList.length; i++) {
                String fileName = fileList[i].getName();
                
                switch(emailType){
                    case MONTH_EMAIL_PACKAGE :
                        if(StringUtils.contains(fileName, "_MON_" + date)){
                            fileMailList.add(fileList[i]);
                        }
                        break;
                    default :
                        if(StringUtils.contains(fileName, date)){
                            fileMailList.add(fileList[i]);
                        }
                        break;
                } 
            }
        }
    }
    
    
    /**
     * email服务器地址
     */
    private String getEmailHost(){
        //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_EMAIL_HOST.getKey());
    }
    
    /**
     * email发送者
     */
    private String getEmailSender(){
        //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_EMAIL_SENDER.getKey());
    }
    
    /**
     * email接收者
     */
    private String getEmailReceiver(){
        //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_EMAIL_RECEIVER.getKey());
    }
    
    /**
     * email登录用户名
     */
    private String getEmailUsername(){
        //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_EMAIL_USERNAME.getKey());
    }
    
    /**
     * email登录用户密码
     */
    private String getEmailPassword(){
        //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_EMAIL_PASSWORD.getKey());
    }
    
    /**
     * email服务器端口
     */
    private String getEmailPort(){
        //return "D:\\shandongBill\\changeRecords"; 
        return globalConfigService.get(
                GlobalConfigKeyEnum.SD_EMAIL_PORT.getKey());
    }
   
}
