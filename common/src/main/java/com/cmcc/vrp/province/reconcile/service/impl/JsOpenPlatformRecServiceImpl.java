package com.cmcc.vrp.province.reconcile.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.SchedulerGroup;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.quartz.jobs.JSOpenPlatformReconcileJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.reconcile.model.JSModel;
import com.cmcc.vrp.province.reconcile.service.ReconcileService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 江苏开放平台的对账类
 * 目标，数据库捞取数据，生成文件，上传到ftp服务器
 * 
 * 若province_flag不为js，直接结束
 *
 */
@Service("jsOpenPlatformRecServiceImpl")
public class JsOpenPlatformRecServiceImpl implements ReconcileService {
    
    private static final Logger logger = LoggerFactory.getLogger(JsOpenPlatformRecServiceImpl.class);

    @Autowired
    ChargeRecordMapper chargeRecordMapper;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    /**
     * 初始化函数，
     * 1.如果是PROVINCE_FLAG为js，则配置对账任务，否则取消
     */
    @PostConstruct
    public void init(){
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        String dailyCronExp = "0 0 2 * * ? *";
        scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "JSOpenPlatformReconcileJob");
        if("jiangsu".equalsIgnoreCase(provinceFlag)){
            scheduleService.createCronTrigger(JSOpenPlatformReconcileJob.class, dailyCronExp, 
                "JSOpenPlatformReconcileJob", SchedulerGroup.DEFAULT.getCode());
        }
    }
    
    @Override
    public boolean doDailyJob() {
        String date = new DateTime().minusDays(1).toString("yyyyMMdd");
        logger.info("江苏对账任务开始,生成账单日期为" + date);
        
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        if(!"jiangsu".equalsIgnoreCase(provinceFlag)){
            logger.info("非江苏环境，不进行对账，当前的PROVINCE_FLAG为" + provinceFlag);
            return false;
        }
        
        List<JSModel> list = getDataFromDB();//获取数据
        //生成文件且上传到ftp服务器
        if(generateFile(list,date) && uploadFtp(date)){
            logger.info("江苏对账任务成功,账单日期为" + date);
            return true;
        }else{
            logger.info("江苏对账任务失败,账单日期为" + date);
            return false;
        }
    }
    
    /**
     * 从数据库获取充值数据
     */
    private List<JSModel> getDataFromDB(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        //取昨天的前后5分钟
        DateTime startTime = new DateTime().minusDays(1).withTimeAtStartOfDay();
        DateTime endTime = startTime.plusDays(1).plusMinutes(5);
        startTime = startTime.minusMinutes(5);
        
        paramMap.put("billStartTime", startTime.toDate());
        paramMap.put("billEndTime", endTime.toDate());
        return chargeRecordMapper.jsGetReconcileDatas(paramMap);
    }
    
    /**
     * 将数据库的数据插入到生成的文件中
     */
    private boolean generateFile(List<JSModel> list,String date){
        //String date = new DateTime().minusDays(1).toString("yyyyMMdd");
        String fileName = getFilePrefix() + date + ".txt";
        File billFile = new File(getFTPLocalPath() + File.separator +fileName);
        BufferedWriter billWriter = null;
        
        try{
            billWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(billFile),
                    "utf-8"));
            
            for(JSModel model : list){
                writeContents(billWriter, model.toString());
            }
            logger.info("生成文件" + fileName + "完成");
            return true;
            
        }catch (IOException e) {
            logger.error(e.getMessage());
        }finally {
            if (billWriter != null) {
                try {
                    billWriter.close();
                } catch (IOException e) {
                }
            }
        }        
        return false;
    }
    
    /**
     * 将文件上传到ftp服务器上
     */
    private boolean uploadFtp(String date) {
        String fileName = getFilePrefix() + date + ".txt";
        FileInputStream in = null;
        try {
            File file = new File(getFTPLocalPath() + File.separator + fileName);
            in = new FileInputStream(file);

            //ftp将文件上传给开放平台
            boolean flag = FTPUtil.uploadFile(getFTPUrl(), getFTPPort(), getFTPLoginname(), getFTPLoginpass(),
                    getFTPTargetPath(), file.getName(), in);
            if (!flag) {
                logger.error("上传到ftp服务器失败,文件名为: " + file.getName());
                return false;
            } else {
                logger.error("上传到ftp服务器成功,文件名为: " + file.getName());
            }
            return true;

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
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
            //logger.info("打印到文件：" + content);
            writer.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    /**
     * 江苏开放平台对账生成文件前缀
     */
    private String getFilePrefix(){
        return globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FILE_PREFIX.getKey());
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
    
    /**
     * 江苏开放平台对账ftp的本地账单保存路径
     */
    private String getFTPLocalPath(){
        return globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FTP_LOCALPATH.getKey());
    }
    
    /**
     * 江苏开放平台对账ftp的目标文件保存路径
     */
    private String getFTPTargetPath(){
        return globalConfigService.get(GlobalConfigKeyEnum.JS_OPENPLATFORM_FTP_TARGETPATH.getKey());
    }
    
}
