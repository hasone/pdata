package com.cmcc.vrp.province.mdrc.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.BossServiceProxyService;
import com.cmcc.vrp.boss.hainan.model.HNBOSSMDRCCharge;
import com.cmcc.vrp.boss.hainan.model.HNMDRCChargeReponse;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.exception.PreconditionRequiredException;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.MdrcCardInfoMapper;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.google.gson.Gson;

/**
 * @ClassName: MdrcCardInfoServiceImpl
 * @Description: luozuwu
 * @author: Rowe
 * @date: 2016年5月30日 下午3:33:11
 */
@Service("mdrcCardInfoService")
public class MdrcCardInfoServiceImpl implements MdrcCardInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdrcCardInfoServiceImpl.class);

    @Autowired
    MdrcCardInfoMapper mdrcCardInfoMapper;

    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    TaskProducer taskProducer;

    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    @Qualifier("haiNanBossService")
    private BossService haiNanBossService;
    @Autowired
    private EntProductService entProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    @Autowired
    private BossServiceProxyService bossServiceProxyService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private CardNumAndPwdService cardNumAndPwdService;

    @Autowired
    private MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService;

    @Autowired
    private ChargeRecordService chargeRecordService;

    @Override
    public int batchInsert(int year, List<MdrcCardInfo> records) {
        final int COUNT = 50000;
        int size = records.size();

        if (size <= COUNT) {
            return mdrcCardInfoMapper.batchInsert(year, records);
        } else {
            int index = 0;

            do {
                mdrcCardInfoMapper.batchInsert(year, records.subList(index, index + COUNT));

                index += COUNT;
            } while (index + COUNT < size);

            mdrcCardInfoMapper.batchInsert(year, records.subList(index, size));
            return size;
        }
    }

    @Override
    public MdrcCardInfo selectByPrimaryKey(int year, Long id) {
        return mdrcCardInfoMapper.selectByPrimaryKey(year, id);
    }

    @Override
    public Long queryMdrcCount(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();
        if (map.get("status") != null) {
            map.put("cardStatus", map.get("status").toString().split(","));
            map.remove("status");
        }
        return mdrcCardInfoMapper.queryMdrcCount(map);
    }

    @Override
    public List<MdrcCardInfo> queryMdrcList(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();
        if (map.get("status") != null) {
            map.put("cardStatus", map.get("status").toString().split(","));
            map.remove("status");
        }
        return mdrcCardInfoMapper.queryMdrcList(map);
    }

    @Override
    public Map<String, Long> pieStatistics(int year, Long configId) {

        Map<String, Long> resultMap = null;//统计结果

        //统计正常状态下的个数 MdrcCardStatus 123456，opstatus=1
        Map<String, Object> map = new HashMap<String, Object>();//数据库查询参数
        map.put("cardStatus", MdrcCardStatus.toNormalStatusMap());
        map.put("year", year);
        if (configId != null) {
            map.put("configId", configId.toString());
        }

        resultMap = mdrcCardInfoMapper.statistics(map);

        //统计op为锁定或销卡时的个数
        map.clear();
        map.put("cardStatus", MdrcCardStatus.toOpStatusUnNoramlMap());
        map.put("year", year);
        if (configId != null) {
            map.put("configId", configId.toString());
        }

        Map<String, Long> resultOpMap = null;
        resultOpMap = mdrcCardInfoMapper.statisticsOpStatus(map);

        //将统计结果合计
        resultMap.putAll(resultOpMap);

        return resultMap;
    }

    @Override
    public int batchExpire(int year, List<String> cardNums) {
        if (cardNums == null || cardNums.size() == 0) {
            return 0;
        }

        return mdrcCardInfoMapper.batchExpire(year, cardNums);
    }

    @Override
    public boolean expire(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return false;
        }

        MdrcCardInfo mdrcCardInfo = cardNumAndPwdService.get(cardNum);
        if (mdrcCardInfo == null) {
            return false;
        }

        return mdrcCardInfoMapper.expire(mdrcCardInfo.getYear(), cardNum) == 1;
    }

    @Override
    public MdrcCardInfo get(String cardNum) {
        return cardNumAndPwdService.get(cardNum);
    }

    @Override
    public List<MdrcCardInfo> listByConfig(MdrcBatchConfig config) {
        if (config == null || config.getId() == null) {
            return null;
        }
        // 获得年份
        int year = getIntYear(config.getThisYear());

        if (year == 0) {
            return null;
        }

        // 查询卡记录
        return mdrcCardInfoMapper.selectByConfigId(year, config.getId());
    }

    @Override
    public int batchUpdatePassword(List<MdrcCardInfo> cards) {
        if (cards == null || cards.size() == 0) {
            return 0;
        }

        // 获得年份
        int year = getYear(cards.get(0).getCardNumber());

        int count = cards.size();
        int startIndex = 0;
        int offset = 5000;

        int updateCount = 0;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("year", year);

        while (startIndex + offset < count) {
            LOGGER.info("Batch update: start = " + startIndex + ", count = " + offset);
            map.put("cards", cards.subList(startIndex, startIndex + offset));
            updateCount += mdrcCardInfoMapper.batchUpdatePassword(map);
            startIndex += offset;
        }

        LOGGER.info("Batch update: start = " + startIndex + ", count = " + (count - startIndex));
        map.put("cards", cards.subList(startIndex, count));
        updateCount += mdrcCardInfoMapper.batchUpdatePassword(map);
        return updateCount;
    }

    //入库操作
    @Override
    public boolean store(String cardNum) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchStore(buildList(cardNum));
    }

    @Override
    public boolean batchStore(List<String> cardNums) throws PreconditionRequiredException {
        MdrcCardStatus targetStatus = MdrcCardStatus.STORED;
        MdrcCardStatus expectedStatus = MdrcCardStatus.NEW;

        for (String cardNum : cardNums) {
            if (!canOperate(cardNum, expectedStatus)) {
                LOGGER.error("入库操作失败, 卡号为{}.", cardNum);
                return false;
            }
        }

        for (String cardNum : cardNums) {
            if (updateStatus(cardNum, targetStatus.getCode(), expectedStatus.getCode()) != 1) {
                LOGGER.error("更新卡状态为入库状态时失败, 卡号为{}.", cardNum);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean activate(String cardNum, String eCode, Long pltPid) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchActivate(buildList(cardNum), eCode, pltPid);
    }

    @Override
    public boolean batchActivate(List<String> cardNums, String eCode, Long pltPid) throws PreconditionRequiredException {
        //参数校验
        MdrcCardInfo cardInfo = null;
        Enterprise enterprise = null;
        List<Long> pltIds = null;

        if (cardNums == null || cardNums.isEmpty() || StringUtils.isBlank(eCode)
                || (enterprise = enterprisesService.selectByCode(eCode)) == null || !isInOperation(enterprise)) {
            LOGGER.error("无效的激活参数, 卡号为:{}, 集团编码:{}, 平台产品ID:{}.", cardNums == null ? "" : new Gson().toJson(cardNums),
                    eCode, pltPid);
            return false;
        }

        //非海南省份，需要校验产品信息
        if (!isHaiNanProvince(cardNums)) {
            LOGGER.info("非海南营销卡卡号，校验产品信息");
            if (pltPid == null || (pltIds = entProductService.selectProIdsByEnterprizeID(enterprise.getId())) == null
                    || pltIds.isEmpty() || !pltIds.contains(pltPid)) {
                LOGGER.error("无效的激活参数,企业未订购该产品, 卡号为:{}, 集团编码:{}, 平台产品ID:{}.", new Gson().toJson(cardNums), eCode,
                        pltPid);
                return false;
            }
        }
        LOGGER.info("海南营销卡卡号，不校验产品信息");
        //校验状态
        for (String cardNum : cardNums) {
            if (!canOperate(cardNum, MdrcCardStatus.STORED)) {
                LOGGER.error("不允许进行激活操作，卡号为{}, 集团编码为{}.", cardNum, eCode);
                return false;
            }
        }

        //绑定企业
        for (String cardNum : cardNums) {
            int year = getYear(cardNum);
            if (year == 0) {
                LOGGER.error("无效的年份信息. CardNum = {}", cardNum);
                return false;
            }

            if (mdrcCardInfoMapper.activate(year, cardNum, enterprise.getId(), pltPid, MdrcCardStatus.STORED.getCode(),
                    MdrcCardStatus.ACTIVATED.getCode()) != 1) {
                LOGGER.error("激活营销卡操作失败, 卡号为{}，集团编码为{}, 产品ID为{}.", cardNum, eCode, pltPid);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean deactive(String cardNum) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchDeactivate(buildList(cardNum));
    }

    @Override
    public boolean use(String cardNum, String password, String mobile, String ip, String serialNum) {
        //0. 校验参数
        if (!validateBeforeUse(cardNum, password, mobile, ip)) {
            LOGGER.error("无效的请求参数，使用卡失败. 卡号为{}, 密码哈希值为{}, 手机号为{}, IP为{}.", cardNum, DigestUtils.md5Hex(password),
                    mobile, ip);
            return false;
        }

        return useWithoutCheck(cardNum, mobile, ip, serialNum);
    }

    @Override
    public boolean extend(String cardNum, Date newExpireDate) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchExtend(buildList(cardNum), newExpireDate);
    }

    @Override
    public boolean batchExtend(List<String> cardNums, Date newExpireDate) throws PreconditionRequiredException {
        MdrcCardStatus[] statuses = { MdrcCardStatus.NEW, MdrcCardStatus.STORED, MdrcCardStatus.ACTIVATED,
                MdrcCardStatus.EXPIRED };

        Map<String, MdrcCardInfo> cardInfos = new HashMap<String, MdrcCardInfo>();
        for (String cardNum : cardNums) {
            if (!canOperate(cardNum, Arrays.asList(statuses))) {
                LOGGER.error("不允许进行延期操作，卡号为{}.", cardNum);
                return false;
            }

            MdrcCardInfo mdrcCardInfo = get(cardNum);
            DateTime newExpireDateTime = new DateTime(newExpireDate);
            if (mdrcCardInfo == null || new DateTime(mdrcCardInfo.getDeadline()).isAfter(newExpireDateTime) //新的截止日期不得在原来的截止日期之前
                    || newExpireDateTime.isBeforeNow()) { //新的截止日期不得在当前时间之前
                LOGGER.error("新的延期时间错误，卡号为{}, 新的延期时间为{}.", cardNum, JSONObject.toJSONString(newExpireDate));
                return false;
            }

            //校验通过了，保存相应的信息
            cardInfos.put(cardNum, mdrcCardInfo);
        }

        //校验都通过了，开始更新数据库
        for (String cardNum : cardNums) {
            MdrcCardInfo mdrcCardInfo = cardInfos.get(cardNum);
            if (!extendInternal(mdrcCardInfo, newExpireDate)) {
                LOGGER.error("执行延期操作时出错，卡号为{}.", cardNum);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean lock(String cardNum) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchLock(buildList(cardNum));
    }

    @Override
    public boolean batchLock(List<String> cardNums) throws PreconditionRequiredException {
        MdrcCardStatus targetStatus = MdrcCardStatus.LOCKED;
        MdrcCardStatus[] statuses = { MdrcCardStatus.NEW, MdrcCardStatus.STORED, MdrcCardStatus.ACTIVATED };

        return batchOpStatusOperate(cardNums, targetStatus, statuses);
    }

    @Override
    public boolean unlock(String cardNum) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchUnlock(buildList(cardNum));
    }

    @Override
    public boolean batchUnlock(List<String> cardNums) throws PreconditionRequiredException {
        MdrcCardStatus targetStatus = MdrcCardStatus.NORMAL;
        MdrcCardStatus[] statuses = { MdrcCardStatus.LOCKED, MdrcCardStatus.EXPIRED };

        return batchOpStatusOperate(cardNums, targetStatus, statuses) && batchProcessExpire(cardNums);
    }

    @Override
    public boolean delete(String cardNum) throws PreconditionRequiredException {
        return StringUtils.isNotBlank(cardNum) && batchDelete(buildList(cardNum));
    }

    @Override
    public boolean batchDelete(List<String> cardNums) throws PreconditionRequiredException {
        MdrcCardStatus targetStatus = MdrcCardStatus.DELETE;
        MdrcCardStatus[] statuses = { MdrcCardStatus.NEW, MdrcCardStatus.STORED, MdrcCardStatus.ACTIVATED };

        return batchOpStatusOperate(cardNums, targetStatus, statuses);
    }

    @Override
    public boolean validateBeforeUse(String cardNum, String password, String mobile, String ip) {
        //0. 校验参数
        if (StringUtils.isBlank(cardNum)) {
            LOGGER.error("无效的卡号，使用卡失败. 卡号为{}, 密码为{}, 手机号为{}, IP为{}.", cardNum, password, mobile, ip);
            return false;
        }

        if (StringUtils.isBlank(password)) {
            LOGGER.error("无效的密码，使用卡失败. 卡号为{}, 密码为{}, 手机号为{}, IP为{}.", cardNum, password, mobile, ip);
            return false;
        }

        if (!com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)) {
            LOGGER.error("无效的手机号，使用卡失败. 卡号为{}, 密码为{}, 手机号为{}, IP为{}.", cardNum, password, mobile, ip);
            return false;
        }

        if (StringUtils.isBlank(ip)) {
            LOGGER.error("无效的ip，使用卡失败. 卡号为{}, 密码为{}, 手机号为{}, IP为{}.", cardNum, password, mobile, ip);
            return false;
        }

        //1. 得到卡信息, 并检验卡密，截止日期以及卡状态等内容
        MdrcCardInfo mdrcCardInfo = get(cardNum);
        if (mdrcCardInfo == null) {
            //LOGGER.error("卡号校验不通过, 卡信息为{}, 用户上传的卡密为{}.", mdrcCardInfo, password);
            LOGGER.error("卡号校验不通过, 卡信息为空, 用户上传的卡密为" + password);
            return false;
        }

        if (!cardNumAndPwdService.validate(cardNum, password)) {
            LOGGER.error("卡号密码校验不通过, 卡信息为{}, 用户上传的卡密为{}.", JSONObject.toJSONString(mdrcCardInfo), password);
            return false;
        }

        if (!checkStartTime(mdrcCardInfo)) {
            LOGGER.error("卡未到生效日期, 卡信息为{}, 用户上传的卡密为{}.", JSONObject.toJSONString(mdrcCardInfo), password);
            return false;
        }

        if (!checkDeadline(mdrcCardInfo)) {
            LOGGER.error("卡已过期, 卡信息为{}, 用户上传的卡密为{}.", JSONObject.toJSONString(mdrcCardInfo), password);
            return false;
        }

        if (!checkStatus(mdrcCardInfo)) {
            LOGGER.error("卡状态校验不通过, 卡信息为{}, 用户上传的卡密为{}.", JSONObject.toJSONString(mdrcCardInfo), password);
            return false;
        }

        return true;
    }

    @Override
    public boolean recharge(String cardNum) {
        if (!needRecharge()) { //没有开启重新充值的开关
            LOGGER.warn("当前平台没有开通重新充值的开关，不能进行重新充值的操作. MdrcCardNum = {}.", cardNum);
            return false;
        }

        MdrcCardInfo mdrcCardInfo = get(cardNum);
        if (StringUtils.isNotBlank(validate(mdrcCardInfo.getCardNumber(), mdrcCardInfo.getCardPassword()))) {
            return false;
        }

        String serialNum = SerialNumGenerator.buildSerialNum();
        //1.重新构建充值记录
        ChargeRecord cr = new ChargeRecord();
        cr.setEnterId(mdrcCardInfo.getEnterId());
        cr.setPrdId(mdrcCardInfo.getProductId());
        cr.setStatus(ChargeRecordStatus.WAIT.getCode());
        cr.setType(ActivityType.MDRC_CHARGE.getname());
        cr.setaName(ActivityType.MDRC_CHARGE.getname());
        cr.setTypeCode(ActivityType.MDRC_CHARGE.getCode());
        cr.setPhone(mdrcCardInfo.getUserMobile());
        cr.setRecordId(mdrcCardInfo.getId());
        cr.setSystemNum(serialNum);
        cr.setChargeTime(new Date());
        cr.setEffectType(0);
        cr.setCount(1);
        if (!chargeRecordService.create(cr)) {
            LOGGER.info("流量卡重新充值生成充值记录失败，chargeRecord = {}", new Gson().toJson(cr));
            return false;
        }

        //2 更新充值请求序列号
        MdrcCardInfo cardUpdate = new MdrcCardInfo();
        cardUpdate.setId(mdrcCardInfo.getId());
        cardUpdate.setReponseSerialNumber("");
        cardUpdate.setRequestSerialNumber(serialNum);
        if(!updateByPrimaryKeySelective(mdrcCardInfo.getYear(), cardUpdate)){
            LOGGER.info("流量卡重新充值更新卡充值序列号失败，chargeRecord = {}", new Gson().toJson(cardUpdate));
            return false;
        }
        
        //2. 重新发起充值操作
        MdrcChargePojo chargePojo = new MdrcChargePojo();
        chargePojo.setMobile(mdrcCardInfo.getUserMobile());
        chargePojo.setCardNum(mdrcCardInfo.getCardNumber());
        chargePojo.setPassword(mdrcCardInfo.getCardPassword());
        chargePojo.setIp(mdrcCardInfo.getUserIp());
        chargePojo.setSerialNum(serialNum);
        return taskProducer.produceMdrcChargeMsg(chargePojo);
    }

    @Override
    public Boolean batchUpdateStatus(Long configId, MdrcCardStatus oldStatus, MdrcCardStatus newStatus) {
        MdrcBatchConfig config = mdrcBatchConfigService.selectByPrimaryKey(configId);
        List<MdrcCardInfo> list = listByConfigIdAndStatus(configId, oldStatus);
        List<String> cardNumList = new ArrayList<String>();
        for (MdrcCardInfo cardInfo : list) {
            cardNumList.add(cardInfo.getCardNumber());
        }
        return mdrcCardInfoMapper.batchUpdateStatus(getIntYear(config.getThisYear()), cardNumList, newStatus.getCode(),
                oldStatus.getCode()) > 0;
    }

    @Override
    public List<MdrcCardInfo> listByConfigIdAndStatus(Long configId, MdrcCardStatus cardStatus) {
        MdrcBatchConfig config = mdrcBatchConfigService.selectByPrimaryKey(configId);
        if (config == null) {
            return null;
        }
        // 获得年份
        int year = getIntYear(config.getThisYear());
        if (year == 0) {
            return null;
        }
        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("year", year);
        if (config.getId() != null) {
            queryMap.put("configId", config.getId());
        }

        if (cardStatus != null) {
            queryMap.put("status", cardStatus.getCode());
        }
        return mdrcCardInfoMapper.queryMdrcList(queryMap);
    }

    @Override
    public Long countByConfigIdAndStatus(Long configId, MdrcCardStatus cardStatus) {
        MdrcBatchConfig config = mdrcBatchConfigService.selectByPrimaryKey(configId);
        if (config == null) {
            return 0L;
        }
        // 获得年份
        int year = getIntYear(config.getThisYear());
        if (year == 0) {
            return 0L;
        }
        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("year", year);
        if (config.getId() != null) {
            queryMap.put("configId", config.getId());
        }
        if (cardStatus != null) {
            queryMap.put("status", cardStatus.getCode());
        }
        return mdrcCardInfoMapper.queryMdrcCount(queryMap);
    }

    @Override
    public boolean internalActivate(Long configId, String beginCardNum, String endCardNum, Long entId, Long proId) {
        MdrcBatchConfig config = mdrcBatchConfigService.selectByPrimaryKey(configId);

        return mdrcCardInfoMapper.internalActivate(getIntYear(config.getThisYear()), beginCardNum, endCardNum, entId,
                proId, MdrcCardStatus.STORED.getCode(), MdrcCardStatus.ACTIVATED.getCode()) > 0;
    }

    @Override
    public boolean batchDeactivate(List<String> cardNums) throws PreconditionRequiredException {
        //校验状态
        for (String cardNum : cardNums) {
            if (!canOperate(cardNum, MdrcCardStatus.ACTIVATED)) {
                LOGGER.error("不允许进行激活操作，卡号为{}.", cardNum);
                return false;
            }
        }

        for (String cardNum : cardNums) {
            int year = getYear(cardNum);
            if (year == 0) {
                LOGGER.error("无效的年份信息. CardNum = {}", cardNum);
                return false;
            }

            if (mdrcCardInfoMapper.deactivate(year, cardNum, MdrcCardStatus.ACTIVATED.getCode(),
                    MdrcCardStatus.STORED.getCode()) != 1) {
                LOGGER.error("去激活操作失败，卡号为{}.", cardNum);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean checkCardNum(Long config, String startSerial, String endSerial) {
        // TODO Auto-generated method stub

        MdrcCardInfo startCardInfo = get(startSerial);
        MdrcCardInfo endCardInfo = get(endSerial);
        if (startCardInfo == null || endCardInfo == null) {
            return false;
        }

        //检查都是已经入库的卡
        long count = countInvalidCard(startSerial, endSerial, MdrcCardStatus.STORED.getCode(), config);

        return count == 0L ? true : false;
    }

    @Override
    public long countInvalidCard(String startSerial, String endSerial, Integer cardStatus, Long configId) {
        int year = getYear(startSerial);
        if (year == 0) {
            return 0;
        }

        return mdrcCardInfoMapper.countInvalidCard(year, startSerial, endSerial, cardStatus, configId);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean activeRange(Long configId, String startSerial, String endSerial, Long entId, Long proId) {
        // TODO Auto-generated method stub

        if (!internalActivate(configId, startSerial, endSerial, entId, proId)) {
            return false;
        }

        Long count = countNotActive(configId, MdrcCardStatus.STORED.getCode(), startSerial, endSerial);

        if (count == null || count.longValue() == 0) {
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(configId);

            MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
            record.setConfigId(mdrcBatchConfig.getId());
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setPreStatus(mdrcBatchConfig.getStatus());
            record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());

            mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.ACTIVATED.getCode());
            if (!mdrcBatchConfigService.update(mdrcBatchConfig)) {
                throw new RuntimeException();
            }

            //插入变更记录
            record.setNowStatus(mdrcBatchConfig.getStatus());
            if (!mdrcBatchConfigStatusRecordService.insertSelective(record)) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    @Override
    public Long countNotActive(Long configId, Integer cardStatus, String startSerial, String endSerial) {
        // TODO Auto-generated method stub
        MdrcBatchConfig config = mdrcBatchConfigService.selectByPrimaryKey(configId);
        // 获得年份
        int year = getIntYear(config.getThisYear());

        Long count = mdrcCardInfoMapper.countNotActive(year, configId, cardStatus, startSerial, endSerial);

        return count;
    }

    @Override
    public List<MdrcCardInfo> getByMdrcBatchConfigAndStatus(MdrcBatchConfig mdrcBatchConfig, Integer mdrcCardStatus) {
        // TODO Auto-generated method stub
        if (mdrcBatchConfig == null || mdrcCardStatus == null || mdrcBatchConfig.getId() == null
                || mdrcBatchConfig.getStatus().toString().equals(MdrcBatchConfigStatus.ACTIVATED.getCode().toString())) {
            return null;
        }

        // 获得年份
        int year = getIntYear(mdrcBatchConfig.getThisYear());
        if (year == 0) {
            return null;
        }
        HashMap<String, Object> queryMap = new HashMap<String, Object>();

        queryMap.put("year", year);
        queryMap.put("configId", mdrcBatchConfig.getId());
        queryMap.put("status", mdrcCardStatus);

        //System.out.println("[year]"+year+" ;[configId]"+mdrcBatchConfig.getId()+" ;[status]"+mdrcCardStatus);

        return mdrcCardInfoMapper.queryNotActiveList(queryMap);
    }

    @Override
    public List<MdrcCardInfo> queryMdrcListByEntId(Map<String, Object> queryCriterias) {
        return mdrcCardInfoMapper.queryMdrcListByEntId(queryCriterias);
    }

    @Override
    public Map<String, Long> pieStatisticsByEntId(int year, Long configId, Long entId) {

        Map<String, Long> resultMap = null;//统计结果

        //统计正常状态下的个数 MdrcCardStatus 3456，opstatus=1
        Map<String, Object> map = new HashMap<String, Object>();//数据库查询参数
        map.put("cardStatus", MdrcCardStatus.toEntNormalStatusMap());
        map.put("year", year);
        map.put("entId", entId);
        if (configId != null) {
            map.put("configId", configId.toString());
        }

        resultMap = mdrcCardInfoMapper.statistics(map);

        //统计op为锁定或销卡时的个数
        map.clear();
        map.put("cardStatus", MdrcCardStatus.toOpStatusUnNoramlMap());
        map.put("year", year);
        map.put("entId", entId);
        if (configId != null) {
            map.put("configId", configId.toString());
        }

        Map<String, Long> resultOpMap = null;
        resultOpMap = mdrcCardInfoMapper.statisticsOpStatus(map);

        //将统计结果合计
        resultMap.putAll(resultOpMap);

        return resultMap;
    }

    @Override
    public Map<String, Object> getAllCardInfoByEnt(QueryObject queryObject) {
        Long count = 0L;
        List<MdrcCardInfo> list = new ArrayList<MdrcCardInfo>();

        List<MdrcBatchConfig> mdrcBatchConfigList = mdrcBatchConfigService.selectAllConfig();

        if (mdrcBatchConfigList != null && mdrcBatchConfigList.size() > 0) {
            for (MdrcBatchConfig mdrcBatchConfig : mdrcBatchConfigList) {
                queryObject.getQueryCriterias().put("year", mdrcBatchConfig.getThisYear().substring(2));
                queryObject.getQueryCriterias().put("configId", mdrcBatchConfig.getId());
                count += queryMdrcCount(queryObject);
                list.addAll(queryMdrcListByEntId(queryObject.getQueryCriterias()));
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", count);
        map.put("list", list);
        return map;
    }

    @Override
    public boolean isHaiNanProvince(List<String> cardNumbers) {
        /*for (String cardNumber : cardNumbers) {
            String provinceCode = getProvinceCode(cardNumber);
            if (StringUtils.isBlank(provinceCode) || !MdrcProvinceCode.HAINAN.getCode().equals(provinceCode)) {
                return false;
            }
        }*/
        if (getProvinceFlag().equals("hainan")) {
            return true;
        }

        return false;
    }

    //卡已激活或者充值失败校验通过
    private boolean checkStatus(MdrcCardInfo mdrcCardInfo) {
        if (retrieveStatus(mdrcCardInfo).equals(MdrcCardStatus.ACTIVATED)
                || (mdrcCardInfo.getChargeStatus() != null && mdrcCardInfo.getChargeStatus().equals(
                        ChargeRecordStatus.FAILED.getCode()))) {
            return true;
        }
        LOGGER.info("cardinfo status check failed, current status=" + mdrcCardInfo.getStatus() + ", cardnumber="
                + mdrcCardInfo.getCardNumber());
        return false;
    }

    private boolean checkDeadline(MdrcCardInfo mdrcCardInfo) {
        Date now = new Date();
        if (mdrcCardInfo.getDeadline() == null || now.after(mdrcCardInfo.getDeadline())) {
            LOGGER.info("cardinfo deadline check failed, deadline=" + mdrcCardInfo.getDeadline() + ", cardnumber="
                    + mdrcCardInfo.getCardNumber());
            return false;
        }
        return true;
    }

    private boolean checkStartTime(MdrcCardInfo mdrcCardInfo) {
        Date now = new Date();
        if (mdrcCardInfo.getStartTime() == null || now.before(mdrcCardInfo.getStartTime())) {
            LOGGER.info("cardinfo startTime check failed, startTime=" + mdrcCardInfo.getStartTime() + ", cardnumber="
                    + mdrcCardInfo.getCardNumber());
            return false;
        }
        return true;
    }

    /**
     * 返回简写年份，如传入2015返回15，异常时返回0
     * <p>
     *
     * @param year
     * @return
     */
    private int getIntYear(String year) {

        if (StringUtils.isNotBlank(year)) {

            String yearStr = null;
            // 长度两位的简写
            if (year.length() == 2) {
                yearStr = year;
            } else if (year.length() == 4) {
                // 长度四位的年份取后两位
                yearStr = year.substring(2, 4);
            }

            if (yearStr != null) {
                return NumberUtils.toInt(yearStr, 0);
            }
        }

        return 0;
    }

    //这个方法和use的区别就是它不是校验卡号卡密的正确性，直接发起充值操作
    private boolean useWithoutCheck(String cardNum, String mobile, String ip, String serialNum) {
        //1. 更新卡状态和使用时间等信息
        LOGGER.info("用户输入的卡密正确, 开始更新卡信息并向BOSS侧发起操作");
        MdrcCardInfo mdrcCardInfo = get(cardNum);

        if (!useInternal(mdrcCardInfo, mobile, ip, serialNum)) {
            LOGGER.error("更新卡状态为已使用状态时出错, 卡信息为{}.", JSONObject.toJSONString(mdrcCardInfo));
            return false;
        }

        //3. 更新成功后向BOSS发起充值请求
        LOGGER.info("更新卡状态成功，开始向BOSS侧发起充值请求.");
        Long entId = mdrcCardInfo.getEnterId();
        Long prdId = mdrcCardInfo.getProductId();
        BossOperationResult chargeResult = null;

        //校验是否海南营销卡
        if (isHaiNanProvince(buildList(cardNum))) {//海南省份直接调用
            chargeResult = haiNanBossService.mdrcCharge(cardNum, mobile);
        } else {//非海南省份直接通过流量包充值
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("systemNum", serialNum);
            List<ChargeRecord> records = chargeRecordService.getMdrcChargeRecords(
                    String.valueOf(mdrcCardInfo.getYear()), map);
            chargeResult = bossServiceProxyService.charge(records.get(0).getId(), entId, prdId, mobile, serialNum);
        }
        //充值成功和充值失败都更新状态，充值失败允许再次充值 
        if (!chargeResult.isSuccess()) {//营销卡充值失败，不更新卡状态，失败后允许重复充值
            mdrcCardInfo.setChargeStatus(ChargeRecordStatus.FAILED.getCode());
            mdrcCardInfo.setChargeMsg(chargeResult.getResultDesc());

        } else {
            mdrcCardInfo.setChargeStatus(ChargeRecordStatus.COMPLETE.getCode());
            mdrcCardInfo.setChargeMsg(ChargeRecordStatus.COMPLETE.getMessage());

            if (chargeResult instanceof HNMDRCChargeReponse) {//海南省份充值结果响应报文包含序列号，填充序列号至数据库
                HNMDRCChargeReponse hnMDRCChargeReponse = (HNMDRCChargeReponse) chargeResult;
                HNBOSSMDRCCharge hNBOSSMDRCCharge = hnMDRCChargeReponse.getMdrcCharge();
                if (hNBOSSMDRCCharge != null && StringUtils.isNotBlank(hNBOSSMDRCCharge.getTradeId())) {//写入充值响应序列号
                    mdrcCardInfo.setReponseSerialNumber(hNBOSSMDRCCharge.getTradeId());
                }
            }
        }

        if (!updateChargeResult(mdrcCardInfo, mdrcCardInfo.getChargeStatus(), mdrcCardInfo.getChargeMsg())) {
            LOGGER.error("更新流量卡充值结果失败, mdrcCardInfo={}", new Gson().toJson(mdrcCardInfo));
        }

        if (chargeResult.isSuccess()) {//充值成功发送短信
            //发送短信
            Product product = productService.get(prdId);
            Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
            String productSize = String.valueOf(SizeUnits.KB.toMB(product.getProductSize()));

            EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService.getChoosedSmsTemplate(entId);
            if (smsmTemplate != null) {
                String template = smsmTemplate.getContent();
                String content = MessageFormat.format(template, productSize, enterprise.getEntName());
                //将短信扔到相应的队列中
                taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(mobile, content, productSize, enterprise
                        .getEntName(), smsmTemplate.getName()));
            }
            return true;
        }else {
            return false;
        }

    }

    private boolean needRecharge() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.MDRC_RECHARGE_WHEN_FAILED.getKey());
        return StringUtils.isNotBlank(value) && "ON".equalsIgnoreCase(value);
    }

    private boolean canOperate(String cardNum, MdrcCardStatus expectedStatus) throws PreconditionRequiredException {
        List<MdrcCardStatus> mdrcCardStatuses = new ArrayList<MdrcCardStatus>(1);
        mdrcCardStatuses.add(expectedStatus);

        return canOperate(cardNum, mdrcCardStatuses);
    }

    /**
     * 判断是否可以执行卡状态变更操作
     *
     * @param cardNum        卡号
     * @param expectedStatus 变更前的状态
     * @return 允许操作返回true, 否则false
     * @throws PreconditionRequiredException
     */
    private boolean canOperate(String cardNum, List<MdrcCardStatus> expectedStatus)
            throws PreconditionRequiredException {
        //0. 校验参数
        //1. 获取卡信息
        MdrcCardInfo cardInfo = null;
        if (StringUtils.isBlank(cardNum) || expectedStatus == null || expectedStatus.isEmpty()
                || (cardInfo = get(cardNum)) == null) {
            String expectedStatusMessage = expectedStatus == null ? "" : JSONObject.toJSONString(expectedStatus);
            LOGGER.error("无效的状态变更参数，不允许变更卡状态. 卡号为 {}, 期望状态为{}.", cardNum, expectedStatusMessage);
            return false;
        }

        //2. 判断是否可进行操作
        MdrcCardStatus status = null;
        if ((status = retrieveStatus(cardInfo)) == null || !expectedStatus.contains(status)) { //当前卡状态不在预期的卡状态中
            LOGGER.error("当前卡状态不符合要求，不允许变更卡状态. 卡号为{},当前状态为{}.", cardNum, status == null ? "" : status.getMessage());
            throw new PreconditionRequiredException("invalid status change");
        }

        //3. 判断是否过期, 只要允许的状态中不包括过期，就要检测当前卡状态是否过期，如果过期则更新卡状态为过期，且操作失败
        if (!expectedStatus.contains(MdrcCardStatus.EXPIRED) && new DateTime(cardInfo.getDeadline()).isBeforeNow()) {
            LOGGER.info("当前卡状态已过期，开始执行过期操作. 卡号为{}.", cardNum);
            if (!expire(cardNum)) {
                LOGGER.error("当前卡状态已过期，在执行过期操作时出错. 卡号为{}.", cardNum);
            }

            return false;
        }

        //everything is done!
        return true;
    }

    //判断企业是否还处于合作期间
    private boolean isInOperation(Enterprise enterprise) {
        if (enterprise == null || new DateTime(enterprise.getEndTime()).isBeforeNow()
                || new DateTime(enterprise.getLicenceEndTime()).isBeforeNow()) {
            LOGGER.error("企业的合同时间已到期，操作失败. 企业信息为{}.", enterprise == null ? "" : new Gson().toJson(enterprise));
            return false;
        }

        return true;
    }

    private MdrcCardStatus retrieveStatus(MdrcCardInfo cardInfo) {
        return cardInfo.getOpStatus().equals(MdrcCardStatus.NORMAL.getCode()) ? MdrcCardStatus.fromValue(cardInfo
                .getStatus()) : MdrcCardStatus.fromValue(cardInfo.getOpStatus());
    }

    //操作卡的运营状态
    private boolean opStatusOperate(String cardNum, MdrcCardStatus targetStatus, MdrcCardStatus[] expectedStatus)
            throws PreconditionRequiredException {
        if (!canOperate(cardNum, Arrays.asList(expectedStatus))) {
            LOGGER.error("不允许将卡状态变更为{}, 卡号为{}.", targetStatus.getMessage(), cardNum);
            return false;
        }

        //进行锁定操作
        if (updateOpStatus(cardNum, targetStatus.getCode()) != 1) {
            LOGGER.error("更新卡状态为{}状态时出错, 卡号为{}.", targetStatus.getMessage(), cardNum);
            return false;
        }

        return true;
    }

    private boolean batchOpStatusOperate(List<String> cardNums, MdrcCardStatus targetStatus,
            MdrcCardStatus[] expectedStatus) throws PreconditionRequiredException {
        for (String cardNum : cardNums) {
            if (!canOperate(cardNum, Arrays.asList(expectedStatus))) {
                LOGGER.error("不允许将卡状态变更为{}, 卡号为{}.", targetStatus.getMessage(), cardNum);
                return false;
            }
        }

        //进行相应操作
        for (String cardNum : cardNums) {
            if (updateOpStatus(cardNum, targetStatus.getCode()) != 1) {
                LOGGER.error("更新卡状态为{}状态时出错, 卡号为{}.", targetStatus.getMessage(), cardNum);
                return false;
            }
        }

        return true;
    }

    private int updateOpStatus(String cardNum, int newOpStatus) {
        // 获得年份
        int year = getYear(cardNum);
        if (year == 0) {
            return 0;
        }

        return mdrcCardInfoMapper.updateOpStatus(year, cardNum, newOpStatus);
    }

    private int getYear(String cardNum) {
        MdrcCardInfo mdrcCardInfo = cardNumAndPwdService.get(cardNum);
        return mdrcCardInfo == null ? 0 : mdrcCardInfo.getYear();
    }

    private int updateStatus(String cardNum, int newStatus, int oldStatus) {
        // 获得年份
        int year = getYear(cardNum);
        if (year == 0) {
            return 0;
        }

        List<Integer> olds = new ArrayList<Integer>(1);
        olds.add(oldStatus);
        return mdrcCardInfoMapper.updateStatus(year, cardNum, newStatus, olds);
    }

    //将卡状态更新成已使用
    private boolean useInternal(MdrcCardInfo mdrcCardInfo, String mobile, String ip, String serial) {
        String cardNum = mdrcCardInfo.getCardNumber();
        int year = mdrcCardInfo.getYear();

        return mdrcCardInfoMapper.use(year, cardNum, mobile, ip, serial) == 1;
    }

    //延期操作
    private boolean extendInternal(MdrcCardInfo mdrcCardInfo, Date newExpireDate) {
        int year = mdrcCardInfo.getYear();
        String cardNum = mdrcCardInfo.getCardNumber();
        return mdrcCardInfoMapper.extend(year, cardNum, newExpireDate,
                mdrcCardInfo.getOpStatus().equals(MdrcCardStatus.EXPIRED.getCode())) == 1;
    }

    private List<String> buildList(String cardNum) {
        List<String> cardNums = new LinkedList<String>();
        cardNums.add(cardNum);
        return cardNums;
    }

    private boolean batchProcessExpire(List<String> cardNums) {
        for (String cardNum : cardNums) {
            MdrcCardInfo mdrcCardInfo = get(cardNum);
            if (new DateTime(mdrcCardInfo.getDeadline()).isBeforeNow() && !expire(cardNum)) {
                LOGGER.error("卡过期操作失败，卡号为{}.", cardNum);
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * */
    //更新流量卡的充值结果状态
    @Transactional
    public boolean updateChargeResult(MdrcCardInfo mdrcCardInfo, int chargeStatus, String chargeResult) {
        //更新流量卡状态:mdrc_card_info
        int year = mdrcCardInfo.getYear();
        String cardNumber = mdrcCardInfo.getCardNumber();

        //更新充值记录状态：charge_record
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("systemNum", mdrcCardInfo.getRequestSerialNumber());
        List<ChargeRecord> records = chargeRecordService.getMdrcChargeRecords(String.valueOf(mdrcCardInfo.getYear()),
                map);
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(records.get(0).getId());
        chargeRecord.setStatus(chargeStatus);
        chargeRecord.setErrorMessage(chargeResult);

        if (mdrcCardInfoMapper.updateChargeResult(year, chargeStatus, chargeResult, cardNumber) != 1) {
            throw new TransactionException("更新卡充值状态失败！");
        }

        if (!chargeRecordService.updateByPrimaryKeySelective(chargeRecord)) {
            throw new TransactionException("更新充值记录失败！");
        }

        return true;
    }

    private String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
    }

    @Override
    public boolean changeStatusByConfigId(Integer status, Long configId) {
        // TODO Auto-generated method stub
        if (status == null || configId == null) {
            return false;
        }
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
        int year = getIntYear(mdrcBatchConfig.getThisYear());
        if (year == 0) {
            return false;
        }
        return mdrcCardInfoMapper.changeStatusByConfigId(status, configId, year) > 0;
    }

    @Override
    public Long countMdrcCardsByMap(Map map) {
        // TODO Auto-generated method stub
        return mdrcCardInfoMapper.countMdrcCardsByMap(map);
    }

    @Override
    public List<MdrcCardInfo> selectMdrcCardInfos(Map map) {
        // TODO Auto-generated method stub
        Calendar a = Calendar.getInstance();
        Integer year = a.get(Calendar.YEAR);
        map.put("year", getIntYear(year.toString()));
        return mdrcCardInfoMapper.selectMdrcCardInfos(map);
    }

    @Override
    public List<MdrcCardInfo> selectForShowChargeResult(Map map) {
        // TODO Auto-generated method stub
        Calendar a = Calendar.getInstance();
        Integer year = a.get(Calendar.YEAR);
        map.put("year", getIntYear(year.toString()));
        return mdrcCardInfoMapper.selectForShowChargeResult(map);
    }

    @Override
    public Integer countForShowChargeResult(Map map) {
        // TODO Auto-generated method stub
        Calendar a = Calendar.getInstance();
        Integer year = a.get(Calendar.YEAR);
        map.put("year", getIntYear(year.toString()));
        return mdrcCardInfoMapper.countForShowChargeResult(map);
    }

    public List<String> getCardNumbersByCount(Long configId, Long count, Integer status) {
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
        int year = getIntYear(mdrcBatchConfig.getThisYear());
        return mdrcCardInfoMapper.getCardNumbersByCount(configId, count, year, status);
    }

    @Override
    public String validate(String cardNum, String password) {
        //非空校验
        if (StringUtils.isBlank(cardNum)) {
            LOGGER.error("无效的卡号，使用卡失败. 卡号为{}, 密码为{}.", cardNum, password);
            return "请输入正确的卡号";
        }

        if (StringUtils.isBlank(password)) {
            LOGGER.error("无效的密码，使用卡失败. 卡号为{}, 密码为{}.", cardNum, password);
            return "请输入正确的卡密";
        }

        //卡信息校验
        MdrcCardInfo mdrcCardInfo = get(cardNum);
        if (mdrcCardInfo == null) {
            LOGGER.error("卡号不存在，使用卡失败。卡号为{}, 密码为{}.", cardNum, password);
            return "请输入正确的卡号";
        }

        //卡号与密码校验
        if (!cardNumAndPwdService.validate(cardNum, password)) {
            LOGGER.error("卡号密码校验不通过,使用卡失败。 卡信息为{}, 用户上传的卡密为{}.", cardNum, password);
            return "请输入正确的卡号";
        }

        //卡生效日期校验
        if (!checkStartTime(mdrcCardInfo)) {
            LOGGER.error("卡未到生效日期,使用卡失败。 卡信息为{}, 用户上传的卡密为{}.", cardNum, password);
            return "该卡未生效";
        }

        //卡失效日期校验
        if (!checkDeadline(mdrcCardInfo)) {
            LOGGER.error("卡已过期,使用卡失败。 卡信息为{}, 用户上传的卡密为{}.", cardNum, password);
            return "该卡已过期";
        }

        //卡状态校验
        if (!checkStatus(mdrcCardInfo)) {
            LOGGER.error("卡状态校验不通过, 使用卡失败。卡信息为{}, 用户上传的卡密为{}.", cardNum, password);
            return "卡状态异常，请联系相关企业";
        }

        //企业和产品校验
        String msg = null;
        if (StringUtils.isNotBlank((msg = entProductService.validateEntAndPrd(mdrcCardInfo.getEnterId(),
                mdrcCardInfo.getProductId())))) {
            LOGGER.error("卡关联的企业与产品信息校验不通过：{}, 使用卡失败。卡信息为{}, 用户上传的卡密为{}.", msg, cardNum, password);
            return "企业状态异常，请联系企业";
        }

        //校验该卡号是否使用过：充值成功或者充值中均不允许再次使用
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardNumber", cardNum);

        List<Integer> statusList = new ArrayList<Integer>();

        statusList.add(ChargeRecordStatus.PROCESSING.getCode());//充值中
        statusList.add(ChargeRecordStatus.COMPLETE.getCode());//充值成功
        map.put("statusList", statusList);
        if (chargeRecordService.countMdrcChargeRecords(String.valueOf(mdrcCardInfo.getYear()), map) > 0) {
            LOGGER.info("流量卡充值失败，该卡已充值成功或充值中, cardNumber = " + cardNum);
            return "该卡已使用";
        }
        return null;
    }

    @Override
    public boolean batchUpdateByConfigId(Long configId, int oldStatus, int newStatus) {
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
        int year = getIntYear(mdrcBatchConfig.getThisYear());
        int result = mdrcCardInfoMapper.batchUpdateByConfigId(configId, year, oldStatus, newStatus);
        return result > 0;
    }

    /**
     * 
     * @Title: bulidChargeRecord 
     * @Description: 构建充值记录
     * @param mobile
     * @param cardNumber
     * @param bossReqNum
     * @return
     * @return: ChargeRecord
     */
    private ChargeRecord bulidChargeRecord(String mobile, String cardNumber, String systemNum) {
        MdrcCardInfo mdrcCardInfo = get(cardNumber);
        ChargeRecord cr = new ChargeRecord();
        cr.setEnterId(mdrcCardInfo.getEnterId());
        cr.setPrdId(mdrcCardInfo.getProductId());
        cr.setStatus(ChargeRecordStatus.WAIT.getCode());
        cr.setType(ActivityType.MDRC_CHARGE.getname());
        cr.setaName(ActivityType.MDRC_CHARGE.getname());
        cr.setTypeCode(ActivityType.MDRC_CHARGE.getCode());
        cr.setPhone(mobile);
        cr.setRecordId(mdrcCardInfo.getId());
        cr.setSystemNum(systemNum);
        cr.setChargeTime(new Date());
        cr.setEffectType(0);
        cr.setCount(1);
        return cr;
    }

    @Override
    public boolean updateByPrimaryKeySelective(int year, MdrcCardInfo mdrcCardInfo) {
        String yearStr = String.valueOf(year);
        if(StringUtils.isBlank(yearStr) || yearStr.length() < 2){
            return false;
        }
        mdrcCardInfo.setYear(Integer.valueOf(yearStr.substring(yearStr.length() - 2, yearStr.length())));
        return mdrcCardInfoMapper.updateByPrimaryKeySelective(mdrcCardInfo) == 1;
    }
}
