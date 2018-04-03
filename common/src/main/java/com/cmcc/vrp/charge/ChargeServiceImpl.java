package com.cmcc.vrp.charge;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.BossMatchResult;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.BossServiceProxyService;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sunyiwei on 2016/4/8.
 */
@Service
public class ChargeServiceImpl implements ChargeService {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(ChargeServiceImpl.class);

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ProductService productService;

    @Autowired
    BossServiceProxyService bossServiceProxyService;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    AccountService accountService;

    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    EntFlowControlService entFlowControlService;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    AdminManagerService adminManagerService;

    @Override
    public ChargeResult charge(Long chargeRecordId, Long entId,
                               Long activityId, Long prdId, AccountType accountType,
                               String mobile, String serialNum) {
        return charge(chargeRecordId, entId, activityId, prdId, accountType,
            mobile, serialNum, false);
    }

    @Override
    public ChargeResult charge(Long chargeRecordId, Long entId, Long activityId, Long prdId, AccountType accountType,
                               String mobile, String serialNum, boolean needCallback) {
        LOGGER.info("charge method start! entId={},mobile={},prdId{},activityId={},serialNum={}", entId, mobile, prdId, activityId, serialNum);

        if (entId == null || prdId == null || activityId == null || accountType == null) {
            LOGGER.info("empty parameters for charge, entId={},mobile={},prdId{},activityId={},serialNum={}", entId, mobile, prdId, activityId, serialNum);
            return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "数据库中缺少相关信息");
        }

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        Product product = productService.get(prdId);
        if (enterprise == null || product == null) {
            LOGGER.info("enterprise or product record not found, charge failed.");
            return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "数据库中缺少相关信息");
        }
        if (!enterprise.getDeleteFlag().equals(EnterpriseStatus.NORMAL.getCode())) {
            LOGGER.info("enterprise status is not equal to 0.");
            return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "企业处于非正常状态");
        }

        boolean minusFlag = false;
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
        try {
            //账户余额扣减,扣减的都是企业账户。现在没有活动账户了
            if (!tryMinusCount(entId, prdId, accountType, 1, serialNum, "充值")) {
                //余额扣减失败了，从BOSS侧同步相应的余额并再次尝试扣减余额
                if (!syncFromBoss(mobile, entId, prdId) || !tryMinusCount(entId, prdId, accountType, 1, serialNum, "充值")) {
                    //余额扣减失败，检查是否是余额不足引起的，余额不足要返回特定的错误信息
                    Account srcAccount = accountService.getCurrencyAccount(entId);
                    EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(prdId, entId);
                    if (srcAccount != null && entProduct != null
                        && srcAccount.getCount() - srcAccount.getMinCount() - entProduct.getDiscount() / 100.0d * product.getPrice() < 0) {
                        ChargeResult result = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "余额不足");
                        LOGGER.error("账户余额扣减失败，原因是余额不足，返回{}", JSON.toJSONString(result));
                        return result;
                    }
                    ChargeResult result = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "账户余额扣减失败");
                    LOGGER.error("账户余额扣减失败，返回{}", JSON.toJSONString(result));
                    return result;
                }
            }

            minusFlag = true;
            chargeResult = charge(chargeRecordId, entId, prdId, mobile, serialNum);
        } catch (Exception e) {
            LOGGER.error("充值失败, SerialNum = {}, Mobile = {}.", serialNum, mobile);
        } finally {
            if (minusFlag && !chargeResult.isSuccess()) {
                if (!accountService.returnFunds(serialNum)) {
                    LOGGER.error("充值serialNum{},entId{}失败时账户返还失败", serialNum, entId);
                }
            }
        }

        return chargeResult;
    }

    /**
     * 私有方法，开始连接boss进行充值
     *
     * @author: qihang
     */
    @Override
    public ChargeResult charge(Long chargeRecordId, Long entId, Long prdId, String mobile, String serialNum) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        Product product = productService.get(prdId);
        if (enterprise == null
            || product == null
            || !enterprise.getDeleteFlag().equals(
            EnterpriseStatus.NORMAL.getCode())) {
            LOGGER.info("enterprise or product record not found, charge failed.");
            return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "数据库中缺少相关信息");
        }

        try {
            LOGGER.info("Start to connect BOSS.");

            // 检查是否在对账日
            if (duringAccountCheckDate(getCheckAccountDateType(),
                getCheckAccountStartTime(), getCheckAccountStartDay(),
                getCheckAccountEndTime(), getCheckAccountEndDay())) {
                LOGGER.info("当前时间是出账日，无法充值");
                return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "出账日期间无法充值");
            }

            BossOperationResult bor = bossServiceProxyService.charge(chargeRecordId, entId, prdId, mobile, serialNum);
            if (bor.isSuccess()) {
                String productSize = String.valueOf(SizeUnits.KB.toMB(product.getProductSize()));

                // 充值成功，先打个日志，再发个短信
                LOGGER.info("|{}|{}|{}|{}|{}|{}|pdata|", new Date(), mobile,
                    getProvinceFlag(), productSize + "MB", product.getPrice(), 1);

                // TODO: 每个省份发短信的内容是不一样的，这里还需要进一步抽象
                EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService
                    .getChoosedSmsTemplate(entId);
                if (smsmTemplate != null) {
                    String template = smsmTemplate.getContent();
                    String content = MessageFormat.format(template,
                        productSize, enterprise.getEntName());
                    // 将短信扔到相应的队列中
                    taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(mobile,
                        content, productSize, enterprise.getEntName(), MessageType.CHARGE_NOTICE.getCode(),
                        smsmTemplate.getName()));
                }

                // 异步接口返回已发送充值请求，同步接口返回充值成功
                if (bor.isAsync()) {
                    if (bor.isNeedQuery()) {
                        // 将查询队列中发送消息
                        ChargeQueryPojo pojo = new ChargeQueryPojo();
                        pojo.setSystemNum(bor.getSystemNum());
                        pojo.setFingerPrint(bor.getFingerPrint());
                        pojo.setEntId(bor.getEntId());
                        taskProducer.produceAsynChargeQueryMsg(pojo);
                    }
                    return new ChargeResult(ChargeResult.ChargeResultCode.PROCESSING, bor.getResultDesc());
                }
                return new ChargeResult(ChargeResult.ChargeResultCode.SUCCESS, "BOSS侧返回充值成功");
            } else {
                return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, bor.getResultDesc());
            }
        } catch (Exception e) {// 其它异常，如Boss连接失败
            LOGGER.error("failed to connect BOSS:{} ", e);
            return new ChargeResult(ChargeResult.ChargeResultCode.CONNECT_FAILURE, "连接Boss失败");
        }
    }

    private boolean syncFromBoss(String mobile, Long entId, Long prdId) {
        if (bossServiceProxyService.needSyncFromBoss()) { // 只有当proxy需要同步时才真正进行余额同步
            BossMatchResult bmr = bossServiceProxyService.chooseBossService(
                mobile, entId, prdId);

            BossService bossService = null;
            return bmr != null // 匹配结果不为空
                && (bossService = bmr.getBossService()) != null // 选择的BOSS渠道不为空
                && bossService.syncFromBoss(entId, prdId); // 同步余额成功
        }

        return false;
    }

    private boolean tryMinusCount(Long activityId, Long prdId,
                                  AccountType accountType, double delta, String serialNum, String desc) {
        try {
            if (accountService.minusCount(activityId, prdId, accountType,
                delta, serialNum, desc)) {
                LOGGER.info(
                    "扣减帐户余额时成功. ActivityId = {}, PrdId = {},  Delta = {}, SerialNum = {}.",
                    activityId, prdId, delta, serialNum);
                return true;
            } else {
                LOGGER.error(
                    "扣减帐户余额时失败，充值失败. ActivityId = {}, PrdId = {}, Delta = {}, SerialNum = {}.",
                    activityId, prdId, delta, serialNum);
            }
        } catch (RuntimeException e) {
            LOGGER.error(
                "扣减帐户余额时失败，充值失败. EntId = {}, ActivityId = {}, PrdId = {}, SerialNum = {}.",
                activityId, prdId, delta, serialNum);
        }

        LOGGER.error("扣减帐户失败，返回false");
        return false;
    }

    private boolean duringAccountCheckDate(String type, String startTime,
                                           String startDay, String endTime, String endDay) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(startTime)
            || StringUtils.isEmpty(startDay)
            || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(endDay)) {
            return false;
        }
        Date now = new Date();
        Calendar a = Calendar.getInstance();

        int nowDay = now.getDate();
        int monthEndDay;
        int monthBeginDay;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if ("0".equals(type)) {
            return false;
        }
        if ("1".equals(type)) {
            a.set(Calendar.DATE, Integer.parseInt(endDay));// 把日期设置为当月第一天
            Date begin = a.getTime();

            Date beginDate = null;
            Date endDate = null;
            try {
                String beginStr = dateFormat.format(begin);
                beginDate = sdf.parse(beginStr + " " + endTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
            a.roll(Calendar.DATE, -1 * Integer.parseInt(startDay));// 日期回滚一天，也就是最后一天
            Date end = a.getTime();
            try {
                String endStr = dateFormat.format(end);
                endDate = sdf.parse(endStr + " " + startTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((endDate != null && now.after(endDate))
                || (beginDate != null && now.before(beginDate))) {
                return true;
            }
        }
        if ("2".equals(type)) {
            monthEndDay = Integer.parseInt(startDay);
            monthBeginDay = Integer.parseInt(endDay);
            if (monthEndDay > monthBeginDay) {
                if (nowDay > monthEndDay || nowDay < monthBeginDay) {
                    return true;
                }

                String str = dateFormat.format(now);
                Date date = null;
                try {
                    date = sdf.parse(str + " " + startTime);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (nowDay == monthEndDay) {
                    if (now.after(date)) {
                        return true;
                    }
                }
                if (nowDay == monthBeginDay) {
                    if (now.before(date)) {
                        return true;
                    }
                }
            } else {
                a.set(Calendar.DATE, monthEndDay);
                Date begin = a.getTime();

                Date beginDate = null;
                Date endDate = null;
                try {
                    String beginStr = dateFormat.format(begin);
                    beginDate = sdf.parse(beginStr + " " + startTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                a.set(Calendar.DATE, monthBeginDay);
                Date end = a.getTime();
                try {
                    String endStr = dateFormat.format(end);
                    endDate = sdf.parse(endStr + " " + endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (endDate != null && beginDate != null && now.before(endDate)
                    && now.after(beginDate)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getCheckAccountDateType() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey());
    }

    public String getCheckAccountStartDay() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey());
    }

    public String getCheckAccountStartTime() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey());
    }

    public String getCheckAccountEndDay() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey());
    }

    public String getCheckAccountEndTime() {
        return globalConfigService
            .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey());
    }

    public String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG
            .getKey());
    }
}
