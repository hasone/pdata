package com.cmcc.vrp.province.service;

/**
 * 审核流程 - 发短信类服务类
 * 
 * qihang
 *
 */
public interface ApprovalRequestSmsService {
    /**
     * 根据request发送短信
     * 
     * 该函数触发点为所有审核提交且更新之后
     */
    boolean sendNoticeSms(Long requestId);
}
