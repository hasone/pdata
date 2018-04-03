package com.cmcc.vrp.province.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketResp;
import com.cmcc.vrp.boss.sichuan.service.ScOrderRedPacketService;
import com.cmcc.vrp.ec.bean.individual.OrderResponse;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.IndividualFlowOrderMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualFlowOrder;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * IndividualFlowOrderServiceImpl.java
 * @author wujiamin
 * @date 2017年1月12日
 */
@Service("individualFlowOrder")
public class IndividualFlowOrderServiceImpl extends AbstractCacheSupport implements IndividualFlowOrderService{
    private static Logger logger = LoggerFactory.getLogger(IndividualFlowOrderServiceImpl.class);
    @Autowired
    IndividualFlowOrderMapper mapper;
    @Autowired
    private AdministerService administerService;
    @Autowired
    private IndividualProductService individualProductService;
    @Autowired
    private IndividualAccountService individualAccountService;
    @Autowired
    private ScOrderRedPacketService scOrderRedPacketService;
    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Override
    public boolean insert(IndividualFlowOrder record) {        
        return mapper.insert(record) == 1;
    }

    @Override
    public boolean insertSelective(IndividualFlowOrder record) {
        return mapper.insertSelective(record) == 1;
    }

    @Override
    public IndividualFlowOrder selectByPrimaryKey(Long id) {      
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(IndividualFlowOrder record) {
        record.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(IndividualFlowOrder record) {     
        return mapper.updateByPrimaryKey(record) == 1;
    }
    
    /** 
     * @Title: orderFlow 
     * @param mobile
     * @param prdCode
     * @param ecSerialNum
     * @return
     * @Author: wujiamin
     * @date 2017年1月12日
    */
    @Override
    public OrderResponse orderFlow(String mobile, String prdCode, String ecSerialNum){
        OrderResponse response = new OrderResponse();
        // 检查被订购用户是否是平台用户
        Administer admin = administerService.selectByMobilePhone(mobile);
        if (admin == null || admin.getId() == null) {
            logger.info("订购流量的用户不是平台用户，创建用户");
            if (!administerService.insertForScJizhong(mobile)) {
                logger.error("订购流量的用户创建失败，mobile={}", mobile);
                response.setCode("0");
                response.setMessage("订购失败");
                return response;
            }
            admin = administerService.selectByMobilePhone(mobile);
        }else{//如果该手机号之前已经是平台的管理员用户，在administer表中会存在该用户，但是该用户并没有个人账户信息，需要检查是否存在账户并插入账户         
            if(!individualAccountService.insertAccountForScJizhong(admin.getId())){
                logger.error("订购流量时平台已存在该用户，但是用户没有个人账户，创建个人账户时失败，mobile={}，adminId={}", mobile, admin.getId());
                response.setCode("0");
                response.setMessage("订购失败");
                return response;
            }
        }
        //1、插入订购记录
        IndividualFlowOrder record = new IndividualFlowOrder();
        record.setMobile(mobile);       
        IndividualProduct product = individualProductService.selectByProductCode(prdCode);
        if(product == null || product.getId() == null){
            response.setCode("0");
            response.setMessage("产品编码不存在");
            return response;
        }
        record.setPrdId(product.getId());
        record.setSize(product.getProductSize()/1024);//订购记录以M为单位
        record.setSystemNum(SerialNumGenerator.buildSerialNum());
        record.setEcSerialNum(ecSerialNum);
        record.setStatus(1);
        record.setDeleteFlag(0);
        record.setCreateTime(new Date());
        
        if(!insertSelective(record)){
            response.setCode("0");
            response.setMessage("订购失败");
            return response;
        }
        
        //向boss发送请求
        boolean success = orderFlowToBoss(mobile, prdCode, record);

        //更新订购结果
        if(!updateByPrimaryKeySelective(record)){
            logger.error("boss流量订购，更新数据库订购记录失败");
        }

        //成功(变更账户)
        if(success){
            logger.info("订购成功，开始变更账户记录");
            IndividualProduct flowProduct = individualProductService.getDefaultFlowProduct();
            
            IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), flowProduct.getId(),
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());
            
            try{
                if(!individualAccountService.changeAccount(account, new BigDecimal(record.getSize()), record.getSystemNum(), 
                        (int)AccountRecordType.INCOME.getValue(), "个人红包流量订购", ActivityType.INDIVIDUAL_REDPACKAGE_ORDER.getCode(), 0)){
                    logger.error("变更账户失败！订购平台序列号：{}", record.getEcSerialNum());
                }
            }catch(Exception e){
                logger.error("boss流量订购，变更数据库流量余额失败，失败原因={}", e.getMessage());
            }
            
            response.setCode("1");
            response.setMessage("订购成功");
            return response;
        }else{
            response.setCode("0");
            response.setMessage(record.getErrorMsg());
            return response;
        }
    }

    /** 
     * 页面订购流量
     * @Title: orderFlowForPage 
     */
    @Override
    public boolean orderFlowForPage(String mobile, String prdCode){
        // 检查被订购用户是否是平台用户
        Administer admin = administerService.selectByMobilePhone(mobile);
        if (admin == null || admin.getId() == null) {
            logger.info("订购流量的用户不是平台用户，创建用户");
            if (!administerService.insertForScJizhong(mobile)) {
                logger.error("订购流量时创建用户失败，mobile={}", mobile);              
                return false;
            }
            admin = administerService.selectByMobilePhone(mobile);
        }else{//如果该手机号之前已经是平台的管理员用户，在administer表中会存在该用户，但是该用户并没有个人账户信息，需要检查是否存在账户并插入账户         
            if(!individualAccountService.insertAccountForScJizhong(admin.getId())){
                logger.error("订购流量时平台已存在该用户，但是用户没有个人账户，创建个人账户时失败，mobile={}，adminId={}", mobile, admin.getId());
                return false;
            }
        }
        
        IndividualProduct product = individualProductService.selectByProductCode(prdCode);
        if(product == null || product.getId() == null){
            logger.error("产品编码不存在,prdCode={}", prdCode);
            return false;
        }
        
        //检查订购次数限制
        //计算当前月剩余的时间
        Long dailyCount = cacheService.getIncrOrUpdate("daily_" + admin.getId().toString(), getSecondToEnd("daily"));
        Long monthlyCount = cacheService.getIncrOrUpdate("monthly_" + admin.getId().toString(), getSecondToEnd("monthly"));
        if(dailyCount>Long.parseLong(globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_DAILY_LIMIT.getKey()))
                ||monthlyCount>Long.parseLong(globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_MONTHLY_LIMIT.getKey()))){
            logger.error("超过限制，订购失败.daily={},monthly={}", cacheService.getDecr("daily_" + admin.getId().toString()), 
                    cacheService.getDecr("monthly_" + admin.getId().toString()));            
            return false;
        }
        
        //1、插入订购记录
        IndividualFlowOrder record = new IndividualFlowOrder();
        record.setMobile(mobile);       
        record.setPrdId(product.getId());
        record.setSize(product.getProductSize()/1024);//订购记录以M为单位
        record.setSystemNum(SerialNumGenerator.buildSerialNum());
        record.setEcSerialNum(null);
        record.setStatus(1);
        record.setDeleteFlag(0);
        record.setCreateTime(new Date());        
        if(!insertSelective(record)){
            logger.error("插入订购记录失败，record={}", JSONObject.toJSONString(record));
            cacheService.getDecr("daily_" + admin.getId().toString());
            cacheService.getDecr("monthly_" + admin.getId().toString());
            return false;
        }
        
        //向boss发送请求
        boolean success = orderFlowToBoss(mobile, prdCode, record);
        
        Date expireTime = DateUtil.getEndNextMonthOfDate(new Date());
        record.setExpireTime(expireTime);
        //更新订购结果
        if(!updateByPrimaryKeySelective(record)){
            logger.error("boss流量订购，更新数据库订购记录失败");
        }
        
        //成功(变更账户)
        if(success){
            logger.info("订购成功，开始变更账户记录");
            IndividualProduct flowProduct = individualProductService.getDefaultFlowProduct();
            
            IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), flowProduct.getId(),
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());            
            try{
                if(!individualAccountService.changeAccount(account, new BigDecimal(record.getSize()), record.getSystemNum(), 
                        (int)AccountRecordType.INCOME.getValue(), "个人红包流量订购", ActivityType.INDIVIDUAL_REDPACKAGE_ORDER.getCode(), 0)){
                    logger.error("变更账户失败！订购平台序列号：{}", record.getEcSerialNum());
                }
                //将过期时间、当前账户中流量所对应的订购id更新到account                
                if(!individualAccountService.updateExpireTimeAndOrderId(account.getId(), expireTime, record.getId())){
                    logger.error("账户过期时间更新失败,accountId={},expireTime={}, currentOrderId={}", account.getId(), 
                            DateUtil.dateToString(expireTime, "yyyy-MM-dd HH:mm:ss"), record.getId());
                }

            }catch(Exception e){
                logger.error("boss流量订购，变更数据库流量余额失败，失败原因={}", e.getMessage());
            }

            return true;

        }else{
            cacheService.getDecr("daily_" + admin.getId().toString());
            cacheService.getDecr("monthly_" + admin.getId().toString());
            return false;
        }
    }
    
    /** 
     * 发送订购请求
     * @Title: orderFlowToBoss 
     */
    private boolean orderFlowToBoss(String mobile, String prdCode, IndividualFlowOrder record){

        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? "false" : dynamicFlag;
        
        //是否是测试的boss
        if(Boolean.parseBoolean(finalFlag)){
            record.setStatus(3);
            record.setBossSerialNum("test");
            record.setErrorMsg("测试-订购成功");
            return  true;
        }else{
            //向BOSS发送流量订购请求
            OrderRedPacketReq req = new OrderRedPacketReq();
            req.setPhoneNo(mobile);
            req.setOperateType("A");//A:订购    D:退订
            req.setProdPrcid(prdCode);
            
            OrderRedPacketResp resp = scOrderRedPacketService.sendRequest(req);
            if(resp !=null){
                if("0000000".equals(resp.getResCode())){
                    record.setStatus(3);
                    if(resp.getOutData()!=null && !StringUtils.isEmpty(resp.getOutData().getOrderId())){
                        record.setBossSerialNum(resp.getOutData().getOrderId());
                    }
                    record.setErrorMsg("订购成功");                
     
                    //校验BOSS返回的资费代码和我们发出的请求是否一致
                    if(!prdCode.equals(resp.getOutData().getProdPrcid())){
                        logger.error("异常！资费代码不一致！请求的资费代码为：{}，BOSS返回的资费代码为：{}", prdCode, resp.getOutData().getProdPrcid());
                        record.setStatus(4);
                        record.setErrorMsg("BOSS订购返回的订购代码不一致");
                        return false;
                    }
                    
                    return true;
                }else{
                    record.setStatus(4);
                    record.setErrorMsg(resp.getResMsg());
                    return false;
                }
            }else{
                record.setStatus(4);
                record.setErrorMsg("BOSS返回异常");
                return false;
            }
        }         
    }
    
    @Override
    public IndividualFlowOrder selectBySystemNum(String systemNum) {      
        return mapper.selectBySystemNum(systemNum);
    }

    @Override
    public Integer countByDate(Date startTime, Date endTime) {
        return mapper.countByDate(startTime, endTime);
    }

    @Override
    protected String getPrefix() {
        return "sc.flowOrder.";
    }

    private Integer getSecondToEnd(String type){        
        if("monthly".equals(type)){
            DateTime now = new DateTime();           
            String strNow = now.toString("yyyy-MM-dd");//转换后时分秒都为0
            DateTime now1 = DateTime.parse(strNow).plusHours(23).plusMinutes(59).plusSeconds(59);
            DateTime end = now1.plusMonths(1).plusDays(-now1.dayOfMonth().get());
            return Seconds.secondsBetween(now, end).getSeconds();
        }
        if("daily".equals(type)){
            DateTime now = new DateTime();
            String strNow = now.toString("yyyy-MM-dd");//转换后时分秒都为0
            DateTime end = DateTime.parse(strNow).plusHours(23).plusMinutes(59).plusSeconds(59);
            return Seconds.secondsBetween(now, end).getSeconds();
        }
        return 0;
        
    }
    
    @Override
    public void validateLimit(Long adminId, Map resultMap){
        String dailyCount = cacheService.get("daily_" + adminId.toString());
        String monthlyCount = cacheService.get("monthly_" + adminId.toString());
        
        if(!StringUtils.isEmpty(dailyCount)
                &&Integer.parseInt(dailyCount)>=Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_DAILY_LIMIT.getKey()))){
            logger.info("超过限制，订购失败.adminId={}，缓存中daily={}", adminId, dailyCount);
            resultMap.put("result", "您已达到当天购买上限");
        }
        if(!StringUtils.isEmpty(monthlyCount)
                &&Integer.parseInt(monthlyCount)>=Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_MONTHLY_LIMIT.getKey()))){
            logger.info("超过限制，订购失败.adminId={}，缓存中monthly={}", adminId, monthlyCount);
            resultMap.put("result", "您已达到当月购买上限");
        }
    }

    @Override
    public List<IndividualFlowOrder> selectByMap(Map map) {
        return mapper.selectByMap(map);
    }
    
    @Override
    public Integer countByMap(Map map) {
        return mapper.countByMap(map);
    }
}
