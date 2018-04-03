/**
 *
 */
package com.cmcc.vrp.province.quartz.jobs;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;


/**
 * EC接口过期提醒定时任务
 * @author wujiamin
 * @date 2016年10月19日
 */
public class EnterpriseInterfaceExpireJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseInterfaceExpireJob.class);

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    GlobalConfigService globalConfigService;

    /**
     * 平台对于分别在倒数10天、5天、3天、1天提醒企业管理员，采用短信及邮箱的方式进行提醒   
     * @Title: execute 
     * @param context
     * @throws JobExecutionException
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        
        logger.info("进入企业EC接口appkey及appsecret到期定时任务");

        //发送短信
        logger.info("进入企业营业执照到期定时任务");

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jsonStr = (String) jobDataMap.get("param");

        logger.info("请求参数：" + jsonStr);
        EnterpriseInterfaceExpireJobPojo pojo = check(jsonStr);//检查参数

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(pojo.getEntId());
        
        List<Administer> admins = adminManagerService.getAdminForEnter(pojo.getEntId());
        String content = enterprise.getName() + "的EC接口key值及secret值将于"
                + pojo.getDate() + "天后过期失效，请尽快登录平台" + getLoginUrl() + "，进行手动更新并替换，谢谢！";
        if ("chongqing".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))) {
            content = enterprise.getName() + "的EC接口key值及secret值将于"
                    + pojo.getDate() + "天后过期失效，请尽快登录重庆流量平台进行手动更新并替换，谢谢！";
        }
        
        //发送短信,发送邮件
        if(admins!=null && admins.size()>0){           
            SmsPojo smsPojo = new SmsPojo(admins.get(0).getMobilePhone(), content, null, null, null);            
            if(!taskProducer.produceDeliverNoticeSmsMsg(smsPojo)){
                logger.info("appkey定时任务过期短信提醒，塞入队列失败！");
            }else{
                logger.info("appkey定时任务过期短信提醒，塞入队列成功！");
            }
            
            String mailAddress = admins.get(0).getEmail();
            if(!StringUtils.isEmpty(mailAddress)){      
                sendMailByLinuxCommand(mailAddress, content);              
            }            
        }
                   
        //给企业邮箱发送邮件
        sendMailByLinuxCommand(enterprise.getEmail(), content);

    }
    
    /**
     * 检查传入参数
     *
     * @return
     */
    private EnterpriseInterfaceExpireJobPojo check(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("参数为空");
            return null;
        }
        EnterpriseInterfaceExpireJobPojo pojo = JSON.parseObject(jsonStr, EnterpriseInterfaceExpireJobPojo.class);
        if (pojo == null || pojo.getEntId() == null || StringUtils.isEmpty(pojo.getDate())) {
            logger.error("参数为空");
            return null;
        }
        return pojo;
    }
        
    /**
     * 获取登录平台的Url
     * @Title: getLoginUrl 
     * @return
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    private String getLoginUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.LOGIN_URL.getKey());
    }
    
    private String getMailHost(){
        return globalConfigService.get(GlobalConfigKeyEnum.MAIL_ADDRESS.getKey());
    }

    
    /**
     * 发送邮件提醒
     * @Title: sendMail 
     * @return
     * @throws MessagingException 
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    private void sendMail(String mailAddress, String content) throws MessagingException {
        
       
        //echo "hello,this is the content of mail.welcome to www.mzone.cc" | mail -s "Hello from mzone.cc by pipe" admin@mzone.cc
        
        
        
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "xxx.163.com");
        // 发件人的账号
        props.put("mail.user", "xxx.163.com");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "xxx.163.com");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress(mailAddress);
        message.setRecipient(RecipientType.TO, to);

        /*// 设置抄送
        InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
        message.setRecipient(RecipientType.CC, cc);

        // 设置密送，其他的收件人不能看到密送的邮件地址
        InternetAddress bcc = new InternetAddress("aaaaa@163.com");
        message.setRecipient(RecipientType.CC, bcc);*/

        // 设置邮件标题
        message.setSubject(content);

        // 设置邮件的内容体
        message.setContent(content, "text/plain;charset=utf-8");   //生成邮件正文    

        // 发送邮件
        Transport.send(message);
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
            System.out.println("command:" + command);
            Process p = r.exec(new String[]{"/usr/bin/bash", "-c", command});
            p.waitFor();
            System.out.println("result:" + p.getOutputStream());
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException:" + ex.getMessage());
           
        } catch (IOException ex) {
            System.out.println("IOException:" + ex.getMessage());
        }

    }

}
