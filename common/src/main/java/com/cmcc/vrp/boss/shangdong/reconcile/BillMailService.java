package com.cmcc.vrp.boss.shangdong.reconcile;

import java.util.List;

import com.cmcc.vrp.boss.shangdong.email.model.EmailType;


/**
 * 发送邮件的类,罗祖武原代码
 * @author Administrator
 *
 */
public interface BillMailService {
    
    /**
     * 
     * @Title: sendEmail 
     * @Description: 邮件获取对账信息
     * @param date
     * @param emailType
     * @return: void
     */
    public boolean sendEmail(String fileNameContains,EmailType emailType,List<String> filePaths);
}
