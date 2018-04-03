package com.cmcc.vrp.province.quartz.jobs;

import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 每天凌晨检查营销卡的过期时间
 * 当卡到期时前30天，前10天，前1天（不含当天）进行邮件和短信提醒，提醒人员为客户经理及企业管理员
 * @author qinqinyan
 */
public class DailyCkeckMdrcExpiredTimeJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(DailyCkeckMdrcExpiredTimeJob.class);
    
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AdministerService administerService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    ManagerService managerService;
    @Autowired
    GlobalConfigService globalConfigService;
    
    //下发短信通知
    private boolean notify(String mobile, String content){
        if (!sendMsgService.sendMessage(mobile, content,
                MessageType.MDRC_EXPIRED.getCode())) {
            logger.info("向用户 = {} 发送  = {} 成功", mobile, content);
        } else {
            logger.info("向用户 = {} 发送  = {} 失败", mobile, content);
        }
        return true;
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {
        logger.info("进入到每天凌晨校验营销卡过期时间定时任务...");
        List<MdrcBatchConfig> mdrcBatchConfigs = mdrcBatchConfigService.selectAllConfig();
        if(mdrcBatchConfigs!=null){
            for(MdrcBatchConfig item : mdrcBatchConfigs){
                if(!item.getStatus().toString().equals(MdrcBatchConfigStatus.USELESS.getCode().toString())){
                    MdrcMakecardRequestConfig mdrcMakecardRequestConfig = mdrcMakecardRequestConfigService.selectByConfigId(item.getId());
                    Enterprise enterprise = enterprisesService.selectById(item.getEnterpriseId());
                    if(mdrcMakecardRequestConfig!=null && enterprise!=null){
                        MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(mdrcMakecardRequestConfig.getRequestId());
                        if(mdrcCardmakeDetail!=null){
                            String content = "【广东流量礼品卡】您好，"+enterprise.getName()+"批次号为"+item.getSerialNumber()+"的部分流量礼品卡即将在"
                        +DateUtil.dateToString(mdrcCardmakeDetail.getEndTime(), "yyyy-MM-dd HH:mm:ss")+"过期";
                            Date today = new Date();
                            long days = DateUtil.getDays(today, mdrcCardmakeDetail.getEndTime())-1;
                            if(days==30 || days==10 || days==1){
                                logger.info("营销卡 configId = {} 还有 {} 过期。", item.getId(), days);
                                
                               //1、企业管理员
                                List<Administer> enterpriseManagers = administerService.selectEMByEnterpriseId(item.getEnterpriseId());
                                if(enterpriseManagers!=null){
                                    for(Administer admin : enterpriseManagers){
                                        //1.1 发短信
                                        notify(admin.getMobilePhone(), content);
                                        
                                        //1.2 发邮件
                                        String mailAddress = admin.getEmail();
                                        if(!StringUtils.isEmpty(mailAddress)){      
                                            sendMailByLinuxCommand(mailAddress, content);              
                                        }
                                    }
                                }
                                
                                //2、向客户经理发送过期短信和邮件提醒
                                notify(enterprise.getCmPhone(), content);
                                if(!StringUtils.isEmpty(enterprise.getCmEmail())){
                                    logger.info("客户经理邮箱 email = {}", enterprise.getCmEmail());
                                    sendMailByLinuxCommand(enterprise.getCmEmail(), content); 
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private String getMailHost(){
        return globalConfigService.get(GlobalConfigKeyEnum.MAIL_ADDRESS.getKey());
    }
    
    /**
     * @param mailAddress
     * @param content
     */
    public void sendMailByLinuxCommand(String mailAddress, String content) {
        logger.info("开始发送邮件：邮箱地址：{}, 内容：{}", mailAddress, content);
        String command = "echo \"" + content + "\" | mail -s \"" + content + "\" -r \"" + getMailHost() + "\" " + mailAddress;
        try {
            Runtime r = Runtime.getRuntime();
            logger.info("command:" + command);
            Process p = r.exec(new String[]{"/usr/bin/bash", "-c", command});
            p.waitFor();
            logger.info("result:" + p.getOutputStream());
        } catch (InterruptedException ex) {
            logger.info("InterruptedException:" + ex.getMessage());
        } catch (IOException ex) {
            logger.info("IOException:" + ex.getMessage());
        }
    }
}



