/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.FlowControlType;
import com.cmcc.vrp.province.dao.EntFlowControlMapper;
import com.cmcc.vrp.province.dao.EntFlowControlRecordMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntFlowControl;
import com.cmcc.vrp.province.model.EntFlowControlRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;

import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:EntFlowControlServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月20日
 */
@Service("entFlowControlService")
public class EntFlowControlServiceImpl implements EntFlowControlService {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(EntFlowControlServiceImpl.class);
    
    // 缓存用的hash的key值
    private final String HASH_KEY = "amountUpper";

    private final String HASH_KEY2 = "amountNow";

    private final String HASH_KEY3 = "amountAddition";
    

    @Autowired
    ManagerService managerService;
    @Autowired
    EntFlowControlMapper entFlowControlMapper;
    @Autowired
    EntFlowControlRecordMapper entFlowControlRecordMapper;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    GlobalConfigService globalConfigService;

    /**
     * @Title: showForPageResultCount
     * @Description: TODO
     */
    @Override
    public int showForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map<String, Object> map = queryObject.toMap();
        Long managerId = Long.parseLong(map.get("managerId").toString());
        map.put("managerIds", managerService.getChildNode(managerId));

        return entFlowControlMapper.showForPageResultCount(map);
    }

    /**
     * @Title: showForPageResultList
     * @Description: TODO
     */
    @Override
    public List<EntFlowControl> showForPageResultList(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map<String, Object> map = queryObject.toMap();
        Long managerId = Long.parseLong(map.get("managerId").toString());
        map.put("managerIds", managerService.getChildNode(managerId));

        List<EntFlowControl> entFlowControlList = getPageResultList(map);

        return entFlowControlList;
    }

    @Override
    public EntFlowControl getFlowControlRecordByEntId(Long entId) {

        return entFlowControlMapper.getFlowControlRecordByEntId(entId);
    }

    @Override
    public boolean updateSmsFlagByEntId(Long entId, Integer fcSmsFlag) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("entId", entId);
        map.put("fcSmsFlag", fcSmsFlag);

        return entFlowControlMapper.updateSmsFlagByEntId(map) == 1;

    }

    
    @Override
    public boolean updateEntFlowControl(Long entId, Long countUpper,
                                        Long updatorId, int type) {
            
        EntFlowControl entFlowControl = entFlowControlMapper
            .getFlowControlUpperByEntId(entId);

        boolean flag = false;
        Long cu;
        if (countUpper >= 0) {
            cu = countUpper * 100;
        } else {
            cu = countUpper;
        }

        if (entFlowControl == null) {
            EntFlowControl efc = new EntFlowControl();
            efc.setEnterId(entId);
            efc.setCountUpper(cu);
            efc.setCreatorId(updatorId);
            efc.setUpdatorId(updatorId);
            efc.setDeleteFlag(0);
            EntFlowControlRecord efcr = new EntFlowControlRecord();
            efcr.setEnterId(entId);
            efcr.setType(FlowControlType.setCountUpper.getCode());
            efcr.setCount(cu);
            flag = entFlowControlMapper.insertEntFlowControlUpper(efc) == 1
                && entFlowControlRecordMapper
                .insertEntFlowControlRecord(efcr) == 1;
        } else {
            /**
             * update by wuguoping on 20170710
             */
            EntFlowControl efc = new EntFlowControl();
            efc.setEnterId(entId);
            efc.setCountUpper(cu);
            efc.setUpdatorId(updatorId);
            EntFlowControlRecord efcr = new EntFlowControlRecord();
            efcr.setEnterId(entId);
            efcr.setType(FlowControlType.setCountUpper.getCode());
            efcr.setCount(cu);
            flag = entFlowControlMapper.updateEntFlowControlUpper(efc) == 1
                && entFlowControlRecordMapper
                .insertEntFlowControlRecord(efcr) == 1;
        }

        if (flag) {
            Jedis jedis = null;

            try {
                jedis = jedisPool.getResource();

                jedis.hset(HASH_KEY, entId.toString(), cu.toString());
                
                if(type == 0){//如果遇到type=0  则让 Redis 里面 amountAddition  失效        by wgp on 20170712
                    jedis.del(HASH_KEY3);
                }
                
                return true;

            } catch (Exception e) {
                LOGGER.error("Error: {}", e.getMessage());
                return false;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        } else {
            LOGGER.info("更新数据库的日上限金额失败");
            return false;
        }
    }

    /**
     * @Title: showHistoryForPageResultCount
     */
    @Override
    public int showHistoryForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map<String, Object> map = queryObject.toMap();

        return entFlowControlRecordMapper.showHistoryForPageResultCount(map);
    }

    /**
     * @Title: showHistoryForPageResultList
     */
    @Override
    public List<EntFlowControlRecord> showHistoryForPageResultList(
        QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }

        Map<String, Object> map = queryObject.toMap();

        return entFlowControlRecordMapper.showHistoryForPageResultList(map);
    }

    @Override
    public boolean updateEntFlowControlAddition(Long entId, Long countAddition) {

        EntFlowControlRecord efcr = new EntFlowControlRecord();
        efcr.setEnterId(entId);
        efcr.setCount(countAddition * 100);
        efcr.setType(FlowControlType.setCountAddition.getCode());

        return entFlowControlRecordMapper.insertEntFlowControlRecord(efcr) == 1
            && updateCountRedis(entId, countAddition * 100, HASH_KEY3,
            FlowControlType.setCountAddition.getCode());

    }

    /**
     *
     * @param isUpdate  false:仅用于判断，不更新缓存；true：扣钱判断是采用
     * */
    @Override
    public boolean isFlowControl(Double deltaAmount, Long entId, boolean isUpdate) {
	Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Long epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                parse(sdf1.format(new Date()) + " 23:59:59").getTime() / 1000;

            // 尝试从缓存中获取记录
            String amountUpperStr = jedis.hget(HASH_KEY, entId.toString());
            String amountNowStr = jedis.hget(HASH_KEY2, entId.toString());
            String amountAdditionStr = jedis.hget(HASH_KEY3, entId.toString());

            Long amountUpper = -1L;
            Long amountNow = 0L;
            Long amountAddition = 0L;
            EntFlowControl entFlowControl = null;

            // 缓存没有获取到，查找数据库
            //获取日上线额度
            if (amountUpperStr == null) {
                if ((entFlowControl = getFlowControlRecordByEntId(entId)) != null) {
                    amountUpper = entFlowControl.getCountUpper();
                } else {
                    amountUpper = -1L; // 数据库中依旧没有获取到，企业没有额度上线，设为-1
                }

                jedis.hset(HASH_KEY, entId.toString(), amountUpper.toString()); // 存入缓存中

            } else {
                amountUpper = Long.valueOf(amountUpperStr);
            }

            //当前发送金额(缓存中没有获取到就默认为0)
            amountNow = amountNowStr == null ? 0L : Long.valueOf(amountNowStr);

            Long amountNowNew = (long) (amountNow + deltaAmount);

            //当天日追加额度
            if (amountAdditionStr == null) {
                if (entFlowControl != null) {
                    amountAddition = entFlowControl.getCountAddition();
                } else if ((entFlowControl = getFlowControlRecordByEntId(entId)) != null) {
                    amountAddition = entFlowControl.getCountAddition();
                } else {
                    amountAddition = 0L;
                }

                jedis.hset(HASH_KEY3, entId.toString(),
                    amountAddition.toString()); // 存入缓存中
                jedis.expireAt(HASH_KEY3, epoch);

            } else {
                amountAddition = Long.valueOf(amountAdditionStr);
            }

            if (amountUpper >= 0L && amountNowNew > (amountUpper + amountAddition)) {
                LOGGER.info("超过企业流控金额, 企业当日上限金额 = {}, 当前累积已发送金额= {}, entId = {}, Delta = {}, ",
                    amountUpper + amountAddition, amountNow, entId, deltaAmount);
                return false;
            } else if (isUpdate && amountUpper >= 0L && amountNowNew >= (amountUpper + amountAddition) * 0.9
                && amountNow < (amountUpper + amountAddition) * 0.9) {

                jedis.hset(HASH_KEY2, entId.toString(), amountNowNew.toString());
                jedis.expireAt(HASH_KEY2, epoch);

                if (entFlowControl == null) {
                    entFlowControl = getFlowControlRecordByEntId(entId);
                }

                if (entFlowControl.getFcSmsFlag().equals(1)) {
                    LOGGER.info("准备发送企业流控预警短信");
                    Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
                    double balance = (amountUpper + amountAddition - amountNowNew) * 1.0 / 100;
                    if ("OK".equalsIgnoreCase(getEntFlowControlSmsCManager())) {
                        String mobile = enterprise.getCmPhone();
                        if (!sendSms(enterprise, balance, mobile)) {
                            LOGGER.error("发送流控预警短信(客户经理)失败");
                        }
                    }

                    if ("OK".equalsIgnoreCase(getEntFlowControlSmsAdminForEnter())) {
                        List<Administer> adminList = adminManagerService.getAdminForEnter(enterprise.getId());
                        if (adminList != null && adminList.size() > 0) {
                            Administer admin = adminList.get(0);
                            String mobile = admin.getMobilePhone();
                            if (!sendSms(enterprise, balance, mobile)) {
                                LOGGER.error("发送流控预警短信(企业管理员)失败");
                            }
                        }

                    }
                }

                return true;

            } else if (isUpdate){
                jedis.hset(HASH_KEY2, entId.toString(), amountNowNew.toString());
                jedis.expireAt(HASH_KEY2, epoch);
                return true;
            } else {
        	return true;
            }

        } catch (ParseException e) {
            LOGGER.error("时间格式转换出错Error:{}", e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    //发送流控预警短信
    private boolean sendSms(Enterprise enterprise, double balance, String mobile) {
        String template = "{0}企业流量充值请求将达今日上限金额，剩余可发送金额{1}元，请控制充值请求数量，谢谢！";
        String content = MessageFormat.format(template,
            enterprise.getName(), balance);
        return taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(
            mobile, content, null, enterprise.getName(), null));
    }

    private List<EntFlowControl> getPageResultList(Map<String, Object> map) {
        List<EntFlowControl> list = entFlowControlMapper
            .showForPageResultList(map);

        Jedis jedis = null;

        String amountNowStr;
        String amountAdditionStr;
        try {
            jedis = jedisPool.getResource();

            for (EntFlowControl entFlowControl : list) {
                Long entId = entFlowControl.getEnterId();

                EntFlowControl efc = null;
                amountNowStr = jedis.hget(HASH_KEY2, entId.toString());
                amountAdditionStr = jedis.hget(HASH_KEY3, entId.toString());

                if (amountAdditionStr == null) {
                    efc = entFlowControlMapper
                        .getFlowControlRecordByEntId(entId);
                }

                entFlowControl.setCountNow(amountNowStr != null ? Long
                    .valueOf(amountNowStr) : 0L);
                entFlowControl
                    .setCountAddition(amountAdditionStr != null ? Long
                        .valueOf(amountAdditionStr) : efc
                        .getCountAddition());

                if (entFlowControl.getCountUpper() >= 0) {
                    entFlowControl.setCountUpper(entFlowControl.getCountUpper()
                        + entFlowControl.getCountAddition());
                }
            }

            return list;

        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            return list;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    private boolean updateCountRedis(Long entId, Long count, String hashKey,
                                     Integer type) {
        Jedis jedis = null;

        String amounStr;
        Long amount = 0L;
        try {
            jedis = jedisPool.getResource();

            amounStr = jedis.hget(hashKey, entId.toString());
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            if (amounStr == null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entId", entId);
                map.put("type", type);
                map.put("createTime", sdf1.format(new Date()));

                List<EntFlowControlRecord> list = entFlowControlRecordMapper
                    .selectEntFlowControlRecordByMap(map);

                if (list != null && list.size() > 0) {
                    for (EntFlowControlRecord entFlowControlRecord : list) {
                        amount += entFlowControlRecord.getCount();
                    }
                }

            } else {
                amount = Long.valueOf(amounStr) + count;
            }
            
            Long epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                parse(sdf1.format(new Date()) + " 23:59:59").getTime() / 1000;
            
            jedis.hset(hashKey, entId.toString(), amount.toString());
//            jedis.pexpireAt(hashKey, epoch);
            jedis.expireAt(hashKey, epoch);
            return true;

        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String getEntFlowControlSmsCManager() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.ENT_FLOW_CONTROL_SMS_CMANAGER.getKey());
    }

    public String getEntFlowControlSmsAdminForEnter() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.ENT_FLOW_CONTROL_SMS_ADMINFORENTER.getKey());
    }
}
