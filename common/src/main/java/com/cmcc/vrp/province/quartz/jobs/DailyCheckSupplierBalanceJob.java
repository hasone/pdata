package com.cmcc.vrp.province.quartz.jobs;

import com.cmcc.vrp.boss.jilin.utils.StringUtil;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.model.SupplierSuccessTotalUse;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
import com.cmcc.vrp.province.service.SupplierSuccessTotalUseService;
import com.cmcc.vrp.util.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 校验供应商余额，低于5万，则向客户经理发短信通知
 * @author qinqinyan
 */
public class DailyCheckSupplierBalanceJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(DailyCheckSupplierBalanceJob.class);

    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    SupplierFinanceRecordService supplierFinanceRecordService; 
    @Autowired
    SupplierSuccessTotalUseService supplierSuccessTotalUseService;
    @Autowired
    ManagerService managerService; 
    @Autowired
    AdministerService administerService;
    @Autowired
    SendMsgService sendMsgService;
    
    @Override
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {
        if(getNoticeBalanceNotEnough()){
            //获取所有供应商财务记录（条件：供应商上架，未被删除）
            List<SupplierFinanceRecord> supplierFinanceRecords = supplierFinanceRecordService.getAllSupplierFinanceRecords();
            List<SupplierFinanceRecord> updateRecords = new ArrayList<SupplierFinanceRecord>();
            
            for(SupplierFinanceRecord supplierFinanceRecord : supplierFinanceRecords){
                List<SupplierSuccessTotalUse> list = 
                        supplierSuccessTotalUseService.selectBySupplierId(supplierFinanceRecord.getSupplierId());
                if(list!=null && list.size()>0){
                    SupplierFinanceRecord updateRecord = new SupplierFinanceRecord();
                    updateRecord.setSupplierId(supplierFinanceRecord.getSupplierId());
                    updateRecord.setTotalMoney(supplierFinanceRecord.getTotalMoney());
                    updateRecord.setSupplierName(supplierFinanceRecord.getSupplierName());
                    updateRecord.setUsedMoney(list.get(0).getTotalUseMoney());
                    
                    Double balance = supplierFinanceRecord.getTotalMoney().doubleValue()-list.get(0).getTotalUseMoney().doubleValue();
                    updateRecord.setBalance(balance);
                    
                    updateRecord.setUpdateTime(new Date());
                    
                    supplierFinanceRecord.setBalance(balance);
                    
                    updateRecords.add(updateRecord);
                }
            }
            
            if(updateRecords!=null && updateRecords.size()>0){
                if(!supplierFinanceRecordService.batchUpdate(updateRecords)){
                    logger.info("更新供应商使用余额失败。");
                }else{
                    logger.info("更新供应商使用余额成功。");
                }
            }
            
            //发送短信给客户经理
            List<Long> managerIds = managerService.getByRoleId(getRoleId());
            List<Administer> administers = administerService.getByManageIds(managerIds);
            for(Administer administer : administers){
                for(SupplierFinanceRecord item : supplierFinanceRecords){
                    if(item.getBalance().doubleValue()<=getSupplierMinBalance().doubleValue()){
                        notifySupplierBalance(administer.getMobilePhone(), item);
                    }
                }
            }
        }
    }
    
    //下发短信通知
    private boolean notifySupplierBalance(String mobile, SupplierFinanceRecord item){
        String content = "您好，"+item.getSupplierName()+"账户余额为"+item.getBalance()/1000000+"万元，请注意预付。";
        if (!sendMsgService.sendMessage(mobile, content,
                MessageType.SUPPLIER_BALANCE.getCode())) {
            logger.info("向用户 = {} 发送供应商 = {} 余额 = {} 万元不足失败。", mobile, 
                    item.getSupplierName(), item.getBalance()/1000000);
        } else {
            logger.info("向用户 = {} 发送供应商 = {} 余额 = {} 万元不足提醒成功, 短信内容：{}。", mobile,
                item.getSupplierName(), item.getBalance()/1000000, content);
        }
        return true;
    }
    
    //获取是否要通知余额不足标志位
    private boolean getNoticeBalanceNotEnough(){
        String flag = globalConfigService.get("NOTICE_BALANCE_NOT_ENOUGH");
        if("YES".equals(flag)){
            return true;
        }
        return false;
    }
    //获取供应商余额最小值
    private Double getSupplierMinBalance(){
        String minBalance = globalConfigService.get("SUPPLIER_MIN_BALANCE");
        if(StringUtil.isNumeric(minBalance)){
            return Double.valueOf(minBalance)*1000000;
        }
        return 0D;
    }
    
    //获取通知角色Id
    private Long getRoleId(){
        String roleIdStr = globalConfigService.get("ACCOUNT_MANAGER_ROLE_ID");
        if(!StringUtils.isEmpty(roleIdStr)){
            try{
                Long roleId = Long.valueOf(roleIdStr);
                return roleId;
            }catch(Exception e){
                logger.info("获取角色失败,请检查供应商余额不足时，通知角色配置是否正确。");
                return null;
            }
        }
        logger.info("获取角色失败,请检查供应商余额不足时，通知角色配置是否未设置。");
        return null;
    }
}

