package com.cmcc.vrp.sms.shandong;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.shangdong.boss.model.SmsIctParam;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.province.model.SmsRecord;
import com.cmcc.vrp.province.service.SmsRecordService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;

/**
 * 山东平台发送短信服务类
 * 请在/etc/pdata/conf/beans.xml  sendMessageService 配置此类
 *
 */
public class SDSendMessageServiceImpl implements SendMessageService {

    private static final Log logger = LogFactory.getLog(SDSendMessageServiceImpl.class);
    
    @Autowired
    SdCloudWebserviceImpl sdCloudWebservice;
    
    @Autowired
    SmsRecordService smsRecordService;
    
    //短信随机号
    private static int serialNo;
    
    static {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        serialNo = random.nextInt(999);
    }
    
    private int getSerialNo() {
        serialNo = (serialNo+1)%1000;
        return serialNo;
    }
    
    @Override
    public boolean send(SmsPojo smsPojo) {
        
        if(smsPojo == null){
            return false;
        }
        SmsRecord record = new SmsRecord();
        record.setContent(smsPojo.getContent());
        record.setMobile(smsPojo.getMobile());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        
        if(!smsRecordService.insert(record)){
            logger.info("生成短信记录失败：mobile = " + record.getMobile() + ",content=" + record.getContent());
        }
        int serialNoTmp = getSerialNo();    
        String serialNo = new DecimalFormat("000").format(serialNoTmp);
        
        Date startTime = new Date();
        Date endTime = new Date(new Date().getTime()+24 * 60 * 60 * 1000);
        
        
        SmsIctParam param = new SmsIctParam();
        param.setMobs(smsPojo.getMobile());
        try {
            //云平台规定，短信内容必需为utf-8编码
            param.setContent(URLEncoder.encode(smsPojo.getContent(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return false;
        }
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setSerialNumber(serialNo);
        
        return sdCloudWebservice.sendMessage(param);
    }

}
