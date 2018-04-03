package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.enums.WarnStatus;
import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountPrizeMap;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.PresentSerialNum;
import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AccountTransactionService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.cmcc.vrp.util.Constants.MINUS_ACCOUNT_TYPE;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * 默认的帐户服务记录，在平台有企业帐户的时候，需要用这个实现 <p> Created by sunyiwei on 2016/4/14.
 */
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    // 资金账户
    private final String CURRENCTYWARNSTATUS = "currencyWarnStatus";

    // 流量池账户
    private final String FLOWWARNSTATUS = "flowWarnStatus";

    // 预警
    private final String ALERTKEY = "alert";

    // 暂停
    private final String STOPKEY = "stop";

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AccountRecordService accountRecordService;
    @Autowired
    ProductService productService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    AccountTransactionService accountTransactionService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    PresentSerialNumService presentSerialNumService;
    @Autowired
    EntFlowControlService entFlowControlService;
    @Autowired
    ManagerService managerService;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ProductConverterService converterService;
    @Autowired
    AccountService accountService;

    /**
     * 创建企业帐户，只有企业帐户的余额是可以直接创建的，活动帐户必须由企业帐户里转帐
     *
     * @param entId     企业ID
     * @param infos     帐户详情信息，key为产品ID, value为相应的余额
     * @param serialNum 操作流水号
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean createEnterAccount(Long entId, Map<Long, Double> infos, String serialNum) {
        // 0. 参数校验
        if (entId == null || infos == null || infos.isEmpty()) {
            LOGGER.error("无效的参数，创建帐户信息失败.");
            return false;
        }

        AccountType accountType = AccountType.ENTERPRISE;

        // 1. 企业存在
        if (enterprisesService.selectByPrimaryKey(entId) == null) {
            LOGGER.error("企业ID不存在，创建帐户信息失败. EntId = {}. ", entId);
            return false;
        }

        // 2. 相关帐户不存在
        if (!batchValidateExist(entId, infos.keySet(), accountType)) {
            LOGGER.error("帐户信息已存在，不能重复创建，创建帐户信息失败. EntId = {}, PrdIds = {}.", entId, infos.keySet());
            return false;
        }

        // 3. 创建成功, 并且插入相应的帐户记录成功
        List<Account> accounts = batchBuildAccount(entId, entId, accountType, infos);
        LOGGER.info("开始插入帐户记录. Accounts = {}.", JSONObject.toJSONString(accounts));

        if (accountMapper.batchInsert(accounts) == accounts.size()
                && accountRecordService.batchInsert(buidlInitAccountRecords(accounts, serialNum))) {
            LOGGER.info("创建帐户成功. EntId = {}, Infos = {}.", entId, infos);
            return true;
        }

        // 创建失败，回滚数据操作
        LOGGER.error("创建帐户失败. EntId = {}, Infos = {}.", entId, infos);
        throw new RuntimeException();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean createActivityAccount(Long activityId, AccountType accountType, Long entId, Map<Long, Double> infos,
            String serialNum) {
        // 0. 参数校验
        if (entId == null || activityId == null || infos == null || infos.isEmpty()) {
            LOGGER.error("无效的参数，创建活动帐户信息失败.");
            return false;
        }

        // 1. 企业存在
        if (enterprisesService.selectByPrimaryKey(entId) == null) {
            LOGGER.error("企业ID不存在，创建活动帐户信息失败. EntId = {}. ", entId);
            return false;
        }

        // 2. 相关帐户不存在
        if (!batchValidateExist(activityId, infos.keySet(), accountType)) {
            LOGGER.error("帐户信息已存在，不能重复创建，创建活动帐户信息失败. EntId = {}, PrdIds = {}.", entId, infos.keySet());
            return false;
        }

        // 创建空帐户
        Map<Long, Double> map = new HashMap<Long, Double>();
        for (Long prdId : infos.keySet()) {
            map.put(prdId, 0.0D);
        }

        // 3. 插入空活动帐户成功
        // 4. 从企业帐户转帐给活动帐户
        List<Account> accounts = batchBuildAccount(entId, activityId, accountType, map);
        if (accountMapper.batchInsert(accounts) == accounts.size() && updateAccount(accounts, infos)
                && batchTransfer(accounts, serialNum)) {
            LOGGER.info("创建活动帐户成功，ActivityId = {}, EntId = {}, Infos = {}, SerialNum = {}.", activityId, entId, infos,
                    serialNum);
            return true;
        }

        LOGGER.error("创建活动帐户失败，ActivityId = {}, EntId = {}, Infos = {}, SerialNum = {}.", activityId, entId, infos,
                serialNum);
        throw new RuntimeException("创建活动帐户失败");
    }

    /**
     * 15.0 版本起，支持负数充值
     */
    @Override
    public boolean addCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum,
            String desc) {
        if (delta == null || delta == 0) { // 增加0元没必要
            return true;
        }

        // 0. 校验参数
        //if (ownerId == null || prdId == null || StringUtils.isBlank(serialNum) || delta < 0) {
        if (ownerId == null || prdId == null || StringUtils.isBlank(serialNum)) {
            LOGGER.error("无效的参数，增加帐户余额失败.");
            return false;
        }

        LOGGER.debug("debug for test:增加账户余额信息成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                ownerId, prdId, delta, serialNum, desc);

        // 1. 获取帐户信息
        Account account = accountMapper.getByOwnerIdAndPrdId(ownerId, prdId, accountType.getValue());
        if (account == null) {
            LOGGER.error("无法获取相应的账户信息,OwnerId = {}， PrdId = {}.", ownerId, prdId);
            return false;
        }

        LOGGER.debug("debug fot test:增加账户余额信息成功Account:{}" + JSON.toJSONString(account));
        
        //15.0 增加
        if(account.getCount() + delta < account.getMinCount()){
            LOGGER.error("扣费无法成功,OwnerId = {}， PrdId = {},count={},delta={},mincount={}.", 
                    ownerId, prdId,account.getCount(),delta,account.getMinCount());
            return false;
        }

        // 2. 更新帐户信息, 并且插入相应的帐户变更记录
        if (accountMapper.updateCount(account.getId(), delta) == 1
                && accountRecordService.create(buildAccountRecord(account.getEnterId(), ownerId, account.getId(),
                        delta, AccountRecordType.INCOME, serialNum, desc))) {
            LOGGER.info("增加帐户余额信息成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.", ownerId, prdId,
                    delta, serialNum, desc);
            return true;
        }

        LOGGER.error("增加帐户余额信息失败, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.", ownerId, prdId,
                delta, serialNum, desc);
        throw new RuntimeException();
    }

    @Override
    public MinusCountReturnType minusCount(Long ownerId, Long prdId, AccountType accountType, Double delta,
            String serialNum, String desc, boolean isCheckSolde) {
        Product dstProduct = null;

        // 0. 校验参数
        if (ownerId == null || prdId == null || (dstProduct = productService.get(prdId)) == null || delta == null
                || accountType == null || StringUtils.isBlank(serialNum) || delta < 0) {
            LOGGER.error("无效的参数，扣减帐户余额失败.");
            return MinusCountReturnType.PARAM_ERR;
        }

        // 如果目标产品是衍生的，则转化关系使用其原产品
        Long destPrdId = dstProduct.getId();
        Product product = productService.get(destPrdId);
        //如果是衍生类产品，则获取企业流量池产品,校验企业原生流量池产品账户
        Long productSize = 1L;
        if (product != null && product.getFlowAccountFlag() != null
                && FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag().intValue()) {
            destPrdId = product.getFlowAccountProductId();
            //虚拟产品，个数转为产品大小
            //            delta = delta * product.getProductSize();
            productSize = product.getProductSize();
        }

        // 1. 获取帐户信息
        Account account = accountMapper.getByOwnerIdAndPrdId(ownerId, destPrdId, accountType.getValue());
        LOGGER.info("获取帐户信息为{}， OwnerId = {}, PrdId = {}.", JSONObject.toJSONString(account), ownerId, destPrdId);

        // 2. 更新帐户信息, 并且插入相应的帐户变更记录
        if (account != null && account.getCount() - delta * productSize >= 0) {

            //流量包产品逻辑
            if (product.getType().byteValue() == ProductType.FLOW_PACKAGE.getValue()) {
                if (isCheckSolde) {
                    return MinusCountReturnType.OK;
                }

                LOGGER.info("帐户余额充足，直接扣减.");
                if (accountMapper.updateCount(account.getId(), -delta) == 1
                        && accountRecordService.create(buildAccountRecord(account.getEnterId(), ownerId,
                                account.getId(), delta, AccountRecordType.OUTGO, serialNum, desc))) {
                    LOGGER.info("扣减帐户余额信息成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                            ownerId, destPrdId, delta, serialNum, desc);
                    return MinusCountReturnType.OK;
                }
            } else if (product.getType().byteValue() == ProductType.FLOW_ACCOUNT.getValue()
                    || product.getType().byteValue() == ProductType.MOBILE_FEE.getValue()) {//流量池、话费产品
                return tryMinusFlowAccount(dstProduct, account.getOwnerId(), destPrdId, accountType, delta, serialNum,
                        desc, isCheckSolde);
            } else {
                return MinusCountReturnType.PARAM_ERR;
            }
        }

        // 3.寻找出用户的相关联的流量池和现金账户，按照次序进行操作
        List<Integer> prdTypes = ProductType.getMinusAccSeq();

        // 找出企业相关的流量池，现金账户,availPrdList已按流量池，现金排序
        List<Product> availPrdList = productService.getPrdsByType(null, prdTypes, ownerId);

        // 定义一个错误返回类型，默认值为没有匹配产品
        MinusCountReturnType finalType = MinusCountReturnType.NO_PRDMATCH;
        for (Product availPrd : availPrdList) {
            boolean result = false;
            
            //根据Product的type获取扣款类型
            ProductType productType = ProductType.fromValue(availPrd.getType().byteValue());
            if(productType == null){
                //result = false;
            }else if(MINUS_ACCOUNT_TYPE.CURRENCY.getCode().equals(productType.getMinusType())){
                result = !converterService.isInterdictConvert(destPrdId, availPrd.getId())
                        && (finalType = tryMinusCurrencyAccount(dstProduct, ownerId, availPrd.getId(), accountType,
                                delta, serialNum, desc, isCheckSolde)).equals(MinusCountReturnType.OK);// 试钱账户
            }else if(MINUS_ACCOUNT_TYPE.FLOW.getCode().equals(productType.getMinusType())){
                result = !converterService.isInterdictConvert(destPrdId, availPrd.getId())
                        && (finalType = tryMinusFlowAccount(dstProduct, ownerId, availPrd.getId(), accountType, delta,
                                serialNum, desc, isCheckSolde)).equals(MinusCountReturnType.OK); // 试试流量池账户吧
            }
           
            if (result) { // 已成功扣减，返回true
                LOGGER.info("扣减帐户余额信息成功, ownerId = {}, srcPrdId = {}, delta = {}, serialNum = {}, desc = {}.", ownerId,
                        availPrd.getId(), delta, serialNum, desc);
                return MinusCountReturnType.OK;
            }
        }

        // 没有成功，返回错误
        return finalType;
    }

    /**
     * 保留和原版一样
     */
    @Override
    public boolean minusCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum,
            String desc) {
        return minusCount(ownerId, prdId, accountType, delta, serialNum, desc, false).equals(MinusCountReturnType.OK);
    }

    @Override
    public Account get(Long ownerId, Long prdId, Integer accountType) {
        if (ownerId == null || prdId == null || accountType == null) {
            return null;
        }

        return accountMapper.getByOwnerIdAndPrdId(ownerId, prdId, accountType);
    }

    @Override
    public List<Account> getByEntId(Long entId) {
        if (entId == null) {
            return null;
        }

        return accountMapper.getByOwner(entId, AccountType.ENTERPRISE.getValue());
    }

    @Override
    public List<Account> getByEntIdAndProId(Long entId, List<PrizeInfo> prizes) {
        if (entId == null || prizes == null || prizes.isEmpty()) {
            return null;
        }
        return accountMapper.selectByEntIdAndProIds(entId, prizes);
    }

    @Override
    public Double sumEntActivitiesFrozenCount(Long entId) {
        if (entId == null) {
            LOGGER.error("无效的企业ID.");
            return 0.0D;
        }

        Double sum = accountMapper.sumActivitiesFrozenAccount(entId);
        return sum == null ? 0D : sum;
    }

    @Override
    public boolean setMinCount(Long entId, Double minCount) {
        // 获取账户信息
        Account account = get(entId, productService.getCurrencyProduct().getId(), AccountType.ENTERPRISE.getValue());
        if (account == null) {
            return false;
        }
        return accountMapper.updateMinCount(account.getId(), minCount, account.getVersion()) > 0;
    }

    @Override
    @Transactional
    public boolean returnFunds(String serialNum, ActivityType activityType, Long dstPrdId, Integer count) {// 支持批量退款，count:退款产品个数
        if (StringUtils.isBlank(serialNum) || dstPrdId == null || activityType == null) {
            LOGGER.error("无效的序列号，活动类型或平台产品Id, SerialNum = {}, ActivityType = {}, PltPrdId = {}.", serialNum,
                    activityType, dstPrdId);
            return false;
        }

        // 得到这个序列号所有的账户记录，注意，这里得到的账户记录既包括支出，也包括收入，但是退钱只能退支出的那部分！
        ReturnInfo returnInfo = buildReturnInfo(serialNum, activityType, dstPrdId, count); // 根据平台流水号获取相应的退还信息，包括源账户、退还金额以及流水号等信息
        if (returnInfo == null) {
            LOGGER.error("无法根据平台流水号获取相应的退款信息，SerialNum = {}.", serialNum);
            return false;
        }

        Account srcAccount = returnInfo.getSrcAccount();

        if (!addCount(srcAccount.getOwnerId(), srcAccount.getProductId(), AccountType.fromValue(srcAccount.getType()),
                returnInfo.getReturnCount(), returnInfo.getSerialNum(), "充值失败，返回金额")) {
            LOGGER.error("账户返还失败. ReturnInfo = {}.", new Gson().toJson(returnInfo));
            return false;
        }

        return true;
    }

    @Override
    public boolean returnFunds(String serialNum) {
        ChargeRecord chargeRecord = null;
        if (StringUtils.isBlank(serialNum) || (chargeRecord = chargeRecordService.getRecordBySN(serialNum)) == null) {
            LOGGER.error("无效的平台流水号或者无法根据平台流水号获取相应的充值记录. SerialNum = {}.", StringUtils.isBlank(serialNum) ? "空"
                    : serialNum);
            return false;
        }

        if (ChargeRecordStatus.FAILED.getCode().equals(chargeRecord.getStatus())) {
            LOGGER.error("当前充值交易状态为“充值失败”， 不需要再次退款. SerialNum = {}.", StringUtils.isBlank(serialNum) ? "空" : serialNum);
            return false;
        }

        ActivityType activityType = ActivityType.fromValue(chargeRecord.getTypeCode());
        return returnFunds(serialNum, activityType, chargeRecord.getPrdId(), 1); // 默认退款的产品个数为1
    }

    /**
     * 账户余额是否充足（校验单个产品在企业账户，流量包，流量池，现金）
     *
     * @author qinqinyan
     * @Description: 0:现金， 2：流量包 1：流量池
     */
    @Override
    public boolean isEnoughInAccount(Long productId, Long entId) {

        if (productId == null || entId == null) {
            return false;
        }
        Product product = productService.selectProductById(productId);
        //如果是衍生类产品，则获取企业流量池产品,校验企业原生流量池产品账户
        if (product != null && product.getFlowAccountFlag() != null
                && FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag().intValue()) {
            productId = product.getFlowAccountProductId();
        }
        Account account = get(entId, productId, AccountType.ENTERPRISE.getValue());
        if (product == null || account == null) {
            return false;
        }
        if (product.getType().toString().equals("2")) {
            if (account.getCount().longValue() >= 1) {
                // 流量包账户充足
                return true;
            } else {
                // 流量池账户这里校验 有点走不通
                // 先校验现金账户
                if (verifyCashAccount(productId, entId)) {
                    // 现金账户余额充足
                    return true;
                }
                // 现金账户余额不足
                return false;
            }
        } else if (product.getType().toString().equals("0")) {
            if (verifyCashAccount(productId, entId)) {
                // 现金账户余额充足
                return true;
            }
            // 现金账户余额不足
            return false;
        } else {
            if (account.getCount().longValue() > product.getProductSize().longValue()) {
                // 流量池账户充足
                return true;
            } else {
                // 流量池账户不足，需要校验现金账户
                if (verifyCashAccount(productId, entId)) {
                    // 现金账户余额充足
                    return true;
                }
                // 现金账户余额不足
                return false;
            }
        }
    }

    /***
     * 校验现金账户
     *
     * @author qinqinyan
     **/
    @Override
    public boolean verifyCashAccount(Long productId, Long entId) {
        Account cashAccount = getCurrencyAccount(entId);
        Product product = productService.selectProductById(productId);
        EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(productId, entId);
        if (cashAccount != null && entProduct != null && entProduct.getDiscount() != null) {
            Integer price = product.getPrice() * entProduct.getDiscount() / 100;
            if (cashAccount.getCount().longValue() >= price.longValue()) {
                // 现金账户充足
                return true;
            } else {
                if (cashAccount.getMinCount().longValue() + price.longValue() < 0) {
                    // 信用额度充足
                    return true;
                }
            }
        }
        // 现金账户或者信用余额不足
        return false;
    }

    /**
     * 是否负债，负债true
     */
    @Override
    public boolean isDebt2Account(List<AccountPrizeMap> maps, Long enterId) {
        Double accountCount;
        Double prizeCount;
        if (enterId == null || maps == null || maps.isEmpty()) {
            return true;
        }
        Double debt = 0D;
        Double money = 0.0D;// 需要现金账户余额
        for (AccountPrizeMap map : maps) {
            if ((accountCount = map.getAccountCount()) >= (prizeCount = (double) map.getPrizeCount())) {
                continue;
            }
            debt = prizeCount - accountCount;

            // 记录下还需要多少金额
            Product p = productService.selectProductById(map.getPrdId());
            if (p == null) {
                return true;
            }
            EntProduct entProduct;
            if (p.getFlowAccountFlag().intValue() == FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue()) {//虚拟产品
                entProduct = entProductService.selectByProductIDAndEnterprizeID(p.getFlowAccountProductId(), enterId);
            } else {
                entProduct = entProductService.selectByProductIDAndEnterprizeID(p.getId(), enterId);
            }

            if (entProduct == null) {
                return true;
            }

            money = money + debt * p.getPrice() * entProduct.getDiscount() / 100.0D;
        }

        if (money > 0) {
            Account currency = getCurrencyAccount(enterId);
            if ((currency.getCount() - currency.getMinCount()) < money) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取现金产品
     */
    @Override
    public Account getCurrencyAccount(Long enterId) {
        if (enterId == null) {
            return null;
        }
        return accountMapper.selectCurrencyAccount(enterId);
    }

    @Override
    public List<Account> getCurrencyAccounts(List<Long> entIds) {
        if (entIds == null || entIds.isEmpty()) {
            return null;
        }

        return accountMapper.selectCurrencyAccounts(entIds);
    }

    @Override
    public boolean checkAccountByEntIdAndProductId(Long entId, Long productId) {
        if (entId == null || productId == null) {
            return false;
        }
        List<Account> accounts = accountMapper.checkAccountByEntIdAndProductId(entId, productId);
        return (accounts != null && accounts.size() > 0);
    }

    @Override
    public boolean createProductAccount(List<Account> records) {
        return records != null && records.size() > 0 && accountMapper.batchInsert(records) == records.size();
    }

    // 默认实现， 直接返回成功
    @Override
    public SyncAccountResult syncFromBoss(Long entId, Long prdId) {
        return SyncAccountResult.SUCCESS;
    }

    @Override
    public boolean updateCount(Long id, Double delta) {
        if (id == null || delta == null) {
            return false;
        }
        return accountMapper.updateCount(id, delta) == 1;
    }

    /**
     * 修改账户预警值
     */
    @Override
    public boolean updateAlertSelective(Account account) {
        return accountMapper.updateAlertSelective(account) > 0;
    }

    /**
     * 恢复预警缓存
     *
     * @param ownerId 企业ID
     */
    @Override
    public void recoverAlert(Long ownerId, Long prdId) {
        Jedis jedis = null;

        try {
            String alertKey = null;
            String stopKey = null;
            Product product = productService.get(prdId);
            if (product != null) {// 账户类型
                if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().byteValue()) {// 流量池账户
                    alertKey = FLOWWARNSTATUS + prdId + ALERTKEY;
                    stopKey = FLOWWARNSTATUS + prdId + STOPKEY;
                } else {//
                    alertKey = CURRENCTYWARNSTATUS + prdId + ALERTKEY;
                    stopKey = CURRENCTYWARNSTATUS + prdId + STOPKEY;
                }
            }
            jedis = jedisPool.getResource();
            jedis.del(alertKey, stopKey);// 删除状态标识，恢复正常状态
            // jedis.hset(key , ownerId.toString(),
            // WarnStatus.NORMAL.getCode().toString());

        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    private boolean updateAccount(List<Account> accounts, Map<Long, Double> map) {
        for (Account account : accounts) {
            account.setCount(map.get(account.getProductId()));
        }

        return true;
    }

    // 尝试从流量池账户里扣流量，原产品只能是流量包或流量池
    protected MinusCountReturnType tryMinusFlowAccount(Product dstProduct, Long ownerId, Long prdId,
            AccountType accountType, Double delta, String serialNum, String desc, boolean isCheckSolde) {
        LOGGER.info("企业的产品账户余额不足，使用流量池帐户抵扣. OwnerId = {}, PrdId = {}.", ownerId, prdId);
        return tryMinusCount(dstProduct, ownerId, prdId, accountType, delta, serialNum, desc, new Operator() {
            @Override
            public Product getSrcProduct(Long prdId) {
                Product product = productService.get(prdId);
                // 111111111111111111
                if (product != null
                        && FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag()
                                .intValue()) {// 如果是虚拟产品则查找父产品
                    product = productService.get(product.getFlowAccountProductId());
                }
                return product;
            }

            @Override
            public double calcDelta(Product dstProduct, double delta) {
                return delta * dstProduct.getProductSize();
            }

            @Override
            public void sendAlertMsg(Long ownerId, Long prdId, boolean alertFlag, boolean stopFlag) {
                Jedis jedis = null;
                Enterprise enterprise = null;

                try {
                    jedis = jedisPool.getResource();
                    String msg = "";
                    // 尝试从缓存中获取记录
                    // 尝试从缓存中获取记录
                    String key = FLOWWARNSTATUS + prdId;
                    String sendAlertMsmFlag = key + ALERTKEY;
                    String sendStopMsmFlag = key + STOPKEY;
                    String alerTStatusStr = jedis.hget(sendAlertMsmFlag, ownerId.toString());
                    String stopStatusStr = jedis.hget(sendStopMsmFlag, ownerId.toString());
                    // 小于暂停值且 状态发生改变了，发暂停短信
                    if (stopFlag && StringUtils.isBlank(stopStatusStr)) {
                        enterprise = enterprisesService.selectByPrimaryKey(ownerId);
                        msg = enterprise.getName() + "企业在流量自服务平台中流量池账户业务已暂停，请及时充值！";
                        jedis.hset(sendStopMsmFlag, ownerId.toString(), WarnStatus.STOP.getCode().toString());
                        jedis.hset(sendAlertMsmFlag, ownerId.toString(), WarnStatus.ALERT.getCode().toString());

                    } else if (alertFlag && StringUtils.isBlank(alerTStatusStr)) {
                        enterprise = enterprisesService.selectByPrimaryKey(ownerId);
                        msg = enterprise.getName() + "在流量自服务平台中流量池账户余额显示消费已达预警值，请及时充值！";
                        jedis.hset(sendAlertMsmFlag, ownerId.toString(), WarnStatus.ALERT.getCode().toString());
                    }
                    if (!StringUtils.isBlank(msg)) {
                        // 客户经理手机号
                        // String mobile = enterprise.getCmPhone();

                        // 将短信扔到相应的队列中
                        LOGGER.info("准备发送企业账户余额预警短信");
                        /*
                         * if (!StringUtils.isBlank(mobile)) {
                         * taskProducer.produceDeliverNoticeSmsMsg(new
                         * SmsPojo(mobile, msg, null, enterprise.getName(),
                         * null)); }
                         */
                        // 向客户经理发送短信
                        Long entManageId = entManagerService.getManagerIdForEnter(enterprise.getId());
                        if (entManageId == null) {
                            return;
                        }

                        List<Manager> fatherManagers = managerService.selectByParentId(entManageId);

                        for (Manager manager : fatherManagers) {
                            // 如果是父节点是客户经理角色，则给挂在该节点上的企业关键人发送短信
                            if (manager.getRoleId().equals(2L)) {
                                sendMsgByManager(manager, msg, enterprise.getName());
                            }
                        }

                        // 企业管理员手机号
                        List<Administer> adminList = adminManagerService.getAdminForEnter(enterprise.getId());

                        if (!adminList.isEmpty()) {
                            for (Administer administer : adminList) {
                                LOGGER.info("给企业关键人发送短信:" + msg + ",手机号:" + administer.getMobilePhone());
                                taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(administer.getMobilePhone(), msg,
                                        null, enterprise.getName(), null));
                            }
                        }

                        // 向客户经理发送短信
                        Manager currentEntMgr = entManagerService.getManagerForEnter(enterprise.getId());
                        if (currentEntMgr == null) {
                            LOGGER.info("未找到相关企业manager节点,entId=" + enterprise.getId());
                            return;
                        }

                        Manager fatherManager = managerService.selectByPrimaryKey(currentEntMgr.getParentId());
                        if (fatherManager == null) {
                            LOGGER.info("未找到相关manager节点,manageId=" + currentEntMgr.getParentId());
                            return;
                        } else if (!fatherManager.getRoleId().equals(2L)) {
                            LOGGER.info("找到父节点的roleId不为2，managerId为" + fatherManager.getId());
                            return;
                        }

                        // 如果是父节点是客户经理角色，则给挂在该节点上的admin发送短信
                        sendMsgByManager(fatherManager, msg, enterprise.getName());
                    }

                } catch (Exception e) {
                    LOGGER.error("Error: {}", e.getMessage());
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }

            }

            @Override
            public boolean isFlowControl(Double deltaAmount, Long entId) {
                return true;
            }
        }, isCheckSolde);
    }

    // 尝试从现金账户里扣钱
    protected MinusCountReturnType tryMinusCurrencyAccount(Product dstProduct, Long ownerId, Long prdId,
            AccountType accountType, Double delta, String serialNum, String desc, boolean isCheckSolde) {
        LOGGER.info("企业的产品账户及流量池账户余额不足，使用现金帐户抵扣. OwnerId = {}, PrdId = {}.", ownerId, prdId);

        // 获取该企业的企业-产品关系
        Long productId = dstProduct.getId();
        Product product = productService.get(productId);
        if (product != null
                && FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag().intValue()) {// 如果是虚拟产品则查找父产品
            productId = product.getFlowAccountProductId();
        }

        // 获取该企业的企业-产品关系
        EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(productId, ownerId);
        Double discount = 1d;
        if (entProduct != null) {
            discount = entProduct.getDiscount() / 100.0d;
        } else {
            LOGGER.info("企业产品关联不存在. OwnerId = {}, PrdId = {}.", ownerId, prdId);
            return MinusCountReturnType.NO_PRDMATCH;
        }

        final Double finalDiscount = discount;
        return tryMinusCount(dstProduct, ownerId, prdId, accountType, delta, serialNum, desc, new Operator() {
            @Override
            public Product getSrcProduct(Long prdId) {
                return productService.get(prdId);
            }

            @Override
            public double calcDelta(Product dstProduct, double delta) {
                return delta * dstProduct.getPrice() * finalDiscount;
            }

            @Override
            public void sendAlertMsg(Long ownerId, Long prdId, boolean alertFlag, boolean stopFlag) {
                Jedis jedis = null;
                Enterprise enterprise = null;

                try {           
                    jedis = jedisPool.getResource();
                    String msg = "";
                    // 尝试从缓存中获取记录
                    String key = CURRENCTYWARNSTATUS + prdId;
                    String sendAlertMsmFlag = key + ALERTKEY;
                    String sendStopMsmFlag = key + STOPKEY;
                    String alerTStatusStr = jedis.hget(sendAlertMsmFlag, ownerId.toString());
                    String stopStatusStr = jedis.hget(sendStopMsmFlag, ownerId.toString());
                    
                    
                    String accountType = "";
                    Product productSrc = productService.get(prdId);
                    if(productSrc == null || productSrc.getType() == null){
                        return;
                    }else if(productSrc.getType().byteValue() == ProductType.CURRENCY.getValue()){
                        accountType = "资金账户";
                    }else if(productSrc.getType().byteValue() == ProductType.PRE_PAY_CURRENCY.getValue()){
                        accountType = "预付费资金账户";
                    }else{
                        return;
                    }
                    
                    // 小于暂停值且 状态发生改变了，发暂停短信
                    if (stopFlag && StringUtils.isBlank(stopStatusStr)) {
                        enterprise = enterprisesService.selectByPrimaryKey(ownerId);
                        msg = enterprise.getName() + "企业在流量自服务平台中" + accountType + "业务已暂停，请及时充值！";
                        jedis.hset(sendStopMsmFlag, ownerId.toString(), WarnStatus.STOP.getCode().toString());
                        jedis.hset(sendAlertMsmFlag, ownerId.toString(), WarnStatus.ALERT.getCode().toString());

                    } else if (alertFlag && StringUtils.isBlank(alerTStatusStr)) {
                        enterprise = enterprisesService.selectByPrimaryKey(ownerId);
                        msg = enterprise.getName() + "在流量自服务平台中" + accountType + "余额显示消费已达预警值，请及时充值！";
                        jedis.hset(sendAlertMsmFlag, ownerId.toString(), WarnStatus.ALERT.getCode().toString());
                    }

                    if (!StringUtils.isBlank(msg)) {
                        // 客户经理手机号
                        // String mobile = enterprise.getCmPhone();

                        // 将短信扔到相应的队列中
                        LOGGER.info("准备发送企业账户余额预警短信");
                        /*
                         * if (!StringUtils.isBlank(mobile)) {
                         * taskProducer.produceDeliverNoticeSmsMsg(new
                         * SmsPojo(mobile, msg, null, enterprise.getName(),
                         * null)); }
                         */
                        // 向客户经理发送短信
                        Long entManageId = entManagerService.getManagerIdForEnter(enterprise.getId());
                        if (entManageId == null) {
                            return;
                        }

                        List<Manager> fatherManagers = managerService.selectByParentId(entManageId);

                        for (Manager manager : fatherManagers) {
                            // 如果是父节点是客户经理角色，则给挂在该节点上的企业关键人发送短信
                            if (manager.getRoleId().equals(2L)) {
                                sendMsgByManager(manager, msg, enterprise.getName());
                            }
                        }

                        // 企业管理员手机号
                        List<Administer> adminList = adminManagerService.getAdminForEnter(enterprise.getId());

                        if (!adminList.isEmpty()) {
                            for (Administer administer : adminList) {
                                LOGGER.info("给企业关键人发送短信:" + msg + ",手机号:" + administer.getMobilePhone());
                                taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(administer.getMobilePhone(), msg,
                                        null, enterprise.getName(), null));
                            }
                        }

                        // 向客户经理发送短信
                        Manager currentEntMgr = entManagerService.getManagerForEnter(enterprise.getId());
                        if (currentEntMgr == null) {
                            LOGGER.info("未找到相关企业manager节点,entId=" + enterprise.getId());
                            return;
                        }

                        Manager fatherManager = managerService.selectByPrimaryKey(currentEntMgr.getParentId());
                        if (fatherManager == null) {
                            LOGGER.info("未找到相关manager节点,manageId=" + currentEntMgr.getParentId());
                            return;
                        } else if (!fatherManager.getRoleId().equals(2L)) {
                            LOGGER.info("找到父节点的roleId不为2，managerId为" + fatherManager.getId());
                            return;
                        }

                        // 如果是父节点是客户经理角色，则给挂在该节点上的admin发送短信
                        sendMsgByManager(fatherManager, msg, enterprise.getName());
                    }

                } catch (Exception e) {
                    LOGGER.error("Error: {}", e.getMessage());
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }

            }

            @Override
            public boolean isFlowControl(Double deltaAmount, Long entId) {
                return entFlowControlService.isFlowControl(deltaAmount, entId, true);
            }

        }, isCheckSolde);
    }

    /**
     * 给指定manager节点上的管理员发送短信
     */
    private void sendMsgByManager(Manager manager, String msg, String enterName) {
        List<Administer> admins = adminManagerService.getAdminByManageId(manager.getId());
        for (Administer administer : admins) {
            LOGGER.info("给客户经理发送短信:" + msg + ",手机号:" + administer.getMobilePhone());
            taskProducer
                    .produceDeliverNoticeSmsMsg(new SmsPojo(administer.getMobilePhone(), msg, null, enterName, null));
        }
    }

    private MinusCountReturnType tryMinusCount(Product dstProduct, Long ownerId, Long prdId, AccountType accountType,
            Double delta, String serialNum, String desc, Operator operator, boolean isCheckSolde) {
        Product srcProduct = null;
        double dstDelta = 0;

        // 获取源产品信息
        if ((srcProduct = operator.getSrcProduct(prdId)) == null // 源产品
                || dstProduct.getId().equals(srcProduct.getId())) { // 目标产品等于源产品，那么就不需要再抵了..反正抵了也不够...
            LOGGER.error(
                    "获取源产品信息时出错. OwnerId = {},  PrdId = {}, Delta = {}, SerialNum = {}, Desc = {}, SrcProduct = {}, DstProduct = {}.",
                    ownerId, prdId, delta, serialNum, desc, srcProduct == null ? null : new Gson().toJson(srcProduct),
                    new Gson().toJson(dstProduct));
            return MinusCountReturnType.PRD_ERR;
        }

        // 获取源账户信息
        Account srcAccount = accountMapper.getByOwnerIdAndPrdId(ownerId, srcProduct.getId(), accountType.getValue());
        if (srcAccount == null) {
            // 账户信息不存在
            LOGGER.error(
                    "源帐户不存在. OwnerId = {},  PrdId = {}, Delta = {}, SerialNum = {}, Desc = {}, SrcAccount = {}, DstDelta = {}.",
                    ownerId, prdId, delta, serialNum, desc, null, dstDelta);
            return MinusCountReturnType.ACCOUNT_NOTEXIST;
        }

        Double leftCount = srcAccount.getCount() - srcAccount.getMinCount()
                - (dstDelta = operator.calcDelta(dstProduct, delta));

        boolean stopFlag = leftCount < srcAccount.getStopCount();
        boolean alertFlag = leftCount < srcAccount.getAlertCount();

        LOGGER.error(
                "检测扣减账户余额，结果为{},ownerId={},prdId={},Id={},Delta = {},count(当前余额)={},mincount={},dstDelta(本次需要余额)={},本次扣减完成后余额为={}",
                leftCount >= 0.0, ownerId, srcProduct.getId(), srcAccount.getId(), delta, srcAccount.getCount(),
                srcAccount.getMinCount(), dstDelta, leftCount);
        
        //湖南流量平台：当企业余额-充值 < 暂停值时,同步企业余额
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        if(StringUtils.isNotBlank(provinceFlag) && "hun".equals(provinceFlag) && stopFlag){
            SyncAccountResult sar = accountService.syncFromBoss(ownerId, srcProduct.getId());
            if (sar != null && SyncAccountResult.isSuccess(sar)) {//同步账户余额
                LOGGER.info("从BOSS侧同步账户余额成功：ownerId={},prdId={}.", ownerId, srcProduct.getId());
                srcAccount = accountMapper.getByOwnerIdAndPrdId(ownerId, srcProduct.getId(), accountType.getValue());
                
                //同步成功后，重新计算
                leftCount = srcAccount.getCount() - srcAccount.getMinCount() - (dstDelta = operator.calcDelta(dstProduct, delta));
                stopFlag = leftCount < srcAccount.getStopCount();
                alertFlag = leftCount < srcAccount.getAlertCount();
            } else {
                LOGGER.info("从BOSS侧同步账户余额失败：ownerId={},prdId={}.", ownerId, srcProduct.getId());
            }          
        }
        
        if (leftCount < 0.0) { // 检验账户余额            
            return MinusCountReturnType.NO_SUFFIT;
        }

        if (srcAccount.getAlertCount() > 0.0
                && (ProductType.CURRENCY.getValue() == srcProduct.getType() || ProductType.FLOW_ACCOUNT.getValue() == srcProduct
                        .getType() || ProductType.PRE_PAY_CURRENCY.getValue() == srcProduct.getType())
                && "OK".equals(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_ALERT_MSG.getKey()))) {// 如果产品类型是资金和流量池产品,
            // 且短信控制打开
            operator.sendAlertMsg(ownerId, prdId, alertFlag, stopFlag);
        }

        if (srcAccount.getStopCount() > 0 && stopFlag) {
            LOGGER.error("检测到扣除完后余额将会少于暂停值. OwnerId = {},PrdId = {},Id={},Delta = {},"
                    + "count(当前余额)={},dstDelta(本次需要余额) = {}, leftCount(扣除后剩余金额) = {}, " + "StopCount(暂停值) = {}.",
                    ownerId, prdId, srcProduct.getId(), delta, srcAccount.getCount(), dstDelta, leftCount,
                    srcAccount.getStopCount());
            return MinusCountReturnType.PASS_STOPVALUE;
        }

        if (isCheckSolde) {
            return MinusCountReturnType.OK;
        }
        LOGGER.info("开始扣减帐户余额. SrcAccount = {}. Delta = {}. DstProduct = {}.", JSONObject.toJSONString(srcAccount),
                delta, JSONObject.toJSONString(dstProduct));

        if (accountMapper.updateCount(srcAccount.getId(), -dstDelta) == 1
                && accountRecordService.create(buildAccountRecord(srcAccount.getEnterId(), ownerId, srcAccount.getId(),
                        dstDelta, AccountRecordType.OUTGO, serialNum, desc))) {
            LOGGER.info(
                    "扣减帐户余额信息成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {},count={},leftCount={}.",
                    ownerId, prdId, delta, serialNum, desc, srcAccount.getCount(), leftCount);

            // 判断是否超过企业流控
            if ("OK".equalsIgnoreCase(getEntFlowControlFlag())
                    && !operator.isFlowControl(operator.calcDelta(dstProduct, delta), ownerId)) {
                LOGGER.info("此次充值请求超过企业流控金额, OwnerId = {}, Delta = {}.", ownerId, delta);
                return MinusCountReturnType.PASS_LIMITFLOW;
            }
            
            return MinusCountReturnType.OK;
        }

        return MinusCountReturnType.MINUS_FAILURE;
    }

    // 校验帐户是否存在， 存在返回false， 不存在返回true
    private boolean validateExist(Long ownerId, Long prdId, AccountType type) {
        return ownerId != null && prdId != null && type != null
                && accountMapper.getByOwnerIdAndPrdId(ownerId, prdId, type.getValue()) == null;
    }

    // 批量校验帐户是否存在，全部通过返回true, 否则false
    private boolean batchValidateExist(Long ownerId, Set<Long> prdIds, AccountType accountType) {
        for (Long prdId : prdIds) {
            if (!validateExist(ownerId, prdId, accountType)) {
                return false;
            }
        }

        return true;
    }

    private Account buildAccount(Long entId, Long ownerId, AccountType accountType, Map.Entry<Long, Double> info) {
        Account account = new Account();

        account.setEnterId(entId);
        account.setOwnerId(ownerId);
        account.setProductId(info.getKey());
        account.setCount(info.getValue());
        account.setMinCount(0D);
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());
        account.setType(accountType.getValue());
        account.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());
        account.setVersion(0);

        return account;
    }

    // 创建初始化帐户时的帐户记录对象，比如创建企业帐户时，或者创建活动帐户时
    private List<AccountRecord> buidlInitAccountRecords(List<Account> accounts, String serialNum) {
        String description = "创建帐户时的初始余额";

        List<AccountRecord> ars = new LinkedList<AccountRecord>();
        for (Account account : accounts) {
            Double initCount = account.getCount();
            int type = account.getType();
            account = accountMapper.getByOwnerIdAndPrdId(account.getOwnerId(), account.getProductId(), type);

            if (initCount > 0) {
                AccountRecord ar = buildAccountRecord(account.getEnterId(), account.getOwnerId(), account.getId(),
                        initCount, AccountRecordType.INCOME, serialNum, description);
                ars.add(ar);
            }
        }

        return ars;
    }

    protected AccountRecord buildAccountRecord(Long entId, Long ownerId, Long accountId, Double delta,
            AccountRecordType type, String serialNum, String description) {
        AccountRecord accountRecord = new AccountRecord();

        accountRecord.setEnterId(entId);
        accountRecord.setOwnerId(ownerId);
        accountRecord.setAccountId(accountId);
        accountRecord.setType(type.getValue());
        accountRecord.setSerialNum(serialNum);
        accountRecord.setCount(delta);
        accountRecord.setAppKey(null);
        accountRecord.setDescription(description);
        accountRecord.setCreateTime(new Date());
        accountRecord.setUpdateTime(new Date());
        accountRecord.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());
        return accountRecord;
    }

    private List<Account> batchBuildAccount(Long entId, Long ownerId, AccountType accountType, Map<Long, Double> infos) {
        List<Account> accounts = new LinkedList<Account>();
        for (Map.Entry<Long, Double> info : infos.entrySet()) {
            accounts.add(buildAccount(entId, ownerId, accountType, info));
        }

        return accounts;
    }

    private boolean transfer(Long srcOwnerId, AccountType srcGameType, Long dstOwnerId, AccountType dstGameType,
            Long prdId, Double delta, String serialNum, String desc) {
        if (!addCount(dstOwnerId, prdId, dstGameType, delta, serialNum, desc)
                || !minusCount(srcOwnerId, prdId, srcGameType, delta, serialNum, desc)) {
            LOGGER.error("转帐失败。SrcOwnerId = {}, DstOwnerId = {}, PrdId = {}, Count = {}, SerialNum = {}.", srcOwnerId,
                    dstOwnerId, prdId, delta, serialNum);
            throw new RuntimeException("余额不足");
        }

        LOGGER.info("转帐成功。SrcOwnerId = {}, DstOwnerId = {}, PrdId = {}, Count = {}, SerialNum = {}.", srcOwnerId,
                dstOwnerId, prdId, delta, serialNum);
        return true;
    }

    private boolean batchTransfer(List<Account> accounts, String serialNum) {
        for (Account account : accounts) {
            Long entId = account.getEnterId();
            Long ownerId = account.getOwnerId();
            AccountType dstType = AccountType.fromValue(account.getType());
            Long prdId = account.getProductId();
            Double count = account.getCount();
            if (count == 0F) { // 余额为0就不需要再转账了
                continue;
            }

            transfer(entId, AccountType.ENTERPRISE, ownerId, dstType, prdId, count, serialNum, "活动初始余额");
        }

        // 所有活动帐户都已经转账成功
        return true;
    }

    /**
     * 根据充值流水号找到相应的账户扣减流水号 <p> 对于批量赠送而言，扣减账户余额时是批量扣减的，批量扣减的流水号与充值流水号之间有对应关系 除了批量赠送之外，其它的均为一一对应关系
     *
     * @param systemSerialNum 系统充值流水号
     * @return 退款信息，包括退款流水号和退还金额
     */
    protected ReturnInfo buildReturnInfo(String systemSerialNum, ActivityType activityType, Long pltPrdId, Integer count) {
        String returnSerialNum = parseReturnSn(systemSerialNum, activityType);
        if (StringUtils.isBlank(returnSerialNum)) {
            LOGGER.error("无法获取平台流水号信息, PltSn = {}.", systemSerialNum);
            return null;
        }

        Account srcAccount = parseSrcAccount(returnSerialNum);
        if (srcAccount == null) {
            LOGGER.error("无法根据平台流水号获取相应的源账户信息, PltSn = {}.", returnSerialNum);
            return null;
        }

        double returnCount = parseReturnCount(srcAccount, pltPrdId, count);
        return new ReturnInfo(returnSerialNum, returnCount, srcAccount);
    }

    // 获取扣款时的源账户
    private Account parseSrcAccount(String pltSn) {
        // 根据流水号获取出账记录
        AccountRecord accountRecord = accountRecordService.getOutgoingRecordByPltSn(pltSn);
        if (accountRecord == null || accountRecord.getAccountId() == null) { // 没有出账记录，就不用退还了
            return null;
        }

        // 获取出账时的源账户及其产品类型
        return accountMapper.getById(accountRecord.getAccountId());
    }

    /**
     * 获取该企业该产品的折扣信息, 并计算退还金额
     *
     * @param srcAccount 扣款时的源账户
     * @param dstPrdId   充值的目标产品ID
     * @return 应该退还的金额
     */
    private double parseReturnCount(Account srcAccount, Long dstPrdId, Integer count) {
        Long srcPrdId = srcAccount.getProductId(); // 扣款的源账户关联的产品ID
        Product srcProduct = productService.get(srcPrdId); // 扣款的源账户关联的产品
        Product dstProdcut = productService.get(dstPrdId); // 充值的目标产品
        com.cmcc.vrp.ec.bean.Constants.ProductType srcPt = com.cmcc.vrp.ec.bean.Constants.ProductType
                .fromValue(srcProduct.getType().byteValue());
        if (srcPt == null) { // 不是有效的产品类型
            return 0.;
        }

        double returnCount = 0;
        switch (srcPt) {
            case CURRENCY: // 现金产品
                Long entId = srcAccount.getEnterId();
                EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(dstPrdId, entId); // 充值目标产品的折扣
                returnCount = (entProduct == null) ? 0 : (entProduct.getDiscount() / 100. * dstProdcut.getPrice() * count);
                break;
            case FLOW_PACKAGE: // 流量包产品, 直接退还1个即可
                returnCount = 1;
                break;
            case FLOW_ACCOUNT: // 流量池产品，退还流量大小即可
                returnCount = dstProdcut.getProductSize();
                break;
            default: // 其它产品，不处理
                returnCount = 0;
                break;
        }

        return returnCount;
    }

    // 计算退款流水号
    private String parseReturnSn(String systemSerialNum, ActivityType activityType) {
        // 对于批量赠送，返回对应的批量流水号, 对于其它活动类型，直接返回系统流水号
        String returnSerialNum = systemSerialNum;
        if (ActivityType.GIVE == activityType
                || ActivityType.MONTHLY_PRESENT == activityType) {
            PresentSerialNum presentSerialNum = presentSerialNumService.selectByPltSn(systemSerialNum);
            returnSerialNum = (presentSerialNum == null ? null : presentSerialNum.getBlockSerialNum());
        }

        return returnSerialNum;
    }

    public String getEntFlowControlFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_FLOW_CONTROL.getKey());
    }

    /**
     * 检测企业的暂停值和预警值 如果通过，返回null 如果不通过，返回错误信息
     */
    @Override
    public String checkAlertStopValue(Long entId, Double totalPrice) {

        Product currencyPrd = productService.getCurrencyProduct();

        // 获取源账户信息
        Account srcAccount = accountMapper.getByOwnerIdAndPrdId(entId, currencyPrd.getId(),
                AccountType.ENTERPRISE.getValue());
        if (srcAccount == null) {
            LOGGER.info("未找到现金账户信息entId:{},prdId:{}", entId + currencyPrd.getId());
            return "未找到账户信息";
        }

        Double afterValue = srcAccount.getCount() - srcAccount.getMinCount() - totalPrice;

        // 只有现金账户，才需要判断发送短信提醒
        boolean stopFlag = afterValue < srcAccount.getStopCount();
        boolean alertFlag = afterValue < srcAccount.getAlertCount();

        if (srcAccount.getStopCount() > 0 && stopFlag) {
            LOGGER.info("当前扣减本次活动后的余额{}，会少于设置的暂停值{}，活动无法生成。", afterValue, srcAccount.getStopCount());
            return "本次赠送该企业将会达到或超过暂停值，请重新设置产品";
        } else if (srcAccount.getAlertCount() > 0 && alertFlag) {
            LOGGER.info("当前扣减本次活动后的余额{}，会少于设置的预警值{}", afterValue, srcAccount.getAlertCount());
            return null;
        } else {
            LOGGER.info("当前扣减本次活动后的余额{}，依然大于设置的预警值{}，和暂停值{}", afterValue, srcAccount.getAlertCount(),
                    srcAccount.getStopCount());
            return null;
        }
    }

    /**
     * 按照流量池1M等值0.15元进行校验 单位: 钱/分 流量/Kb
     */
    @Override
    public boolean isEnough2Debt(Account currency, Double debt) {
        if (currency == null || debt == null) {
            return true;
        }
        Double debt2Currency = (debt / 1024) * 15;
        Double totalCurrency = currency.getCount() - currency.getMinCount();
        if (totalCurrency.doubleValue() > debt2Currency.doubleValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean cleanAccountByTpye(ProductType productType) {
        if (productType == null) {
            return false;
        }
        return accountMapper.cleanAccountByTpye(Integer.valueOf(productType.getValue())) > 0;
    }

    @Override
    public boolean isEmptyAccount() {
        // TODO Auto-generated method stub
        return false;
    }

    private interface Operator {
        Product getSrcProduct(Long prdId);

        double calcDelta(Product dstProduct, double delta);

        void sendAlertMsg(Long ownerId, Long prdId, boolean alertFlag, boolean stopFlag);

        boolean isFlowControl(Double delta, Long entId);
    }

    protected class ReturnInfo {
        // 流水号
        private String serialNum;

        // 退还金额
        private double returnCount;

        // 退还的账户
        private Account srcAccount;

        public ReturnInfo(String serialNum, double returnCount, Account srcAccount) {
            this.serialNum = serialNum;
            this.returnCount = returnCount;
            this.srcAccount = srcAccount;
        }

        public String getSerialNum() {
            return serialNum;
        }

        public double getReturnCount() {
            return returnCount;
        }

        public Account getSrcAccount() {
            return srcAccount;
        }
    }

    @Override
    public boolean isEnoughInAccount(List<Product> prds, Long entId) {

        if (prds == null || prds.size() <= 0 || entId == null) {
            return true;
        }

        //获取所有类型账户
        List<Integer> prdTypes = new LinkedList<Integer>();
        for (ProductType pt : ProductType.values()) {
            prdTypes.add(new Integer(pt.getValue()));
        }

        //获取企业所有账户
        List<Product> destPrdList = productService.getPrdsByType(null, prdTypes, entId);
        HashMap<String, Double> selfAccountMap = new HashMap<String, Double>();
        HashMap<String, Double> otherAccountMap = new HashMap<String, Double>();
        for (Product srcPrd : prds) {
            //记录当前产品大小,流量池单位：KB，话费产品单位为：分
            Long srcProductSize = srcPrd.getProductSize();

            //记录当前产品价格，单位为：分
            Integer srcProductPrice = srcPrd.getPrice();

            //虚拟产品转为对应的父产品
            if (srcPrd.getFlowAccountFlag().equals(FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode())) {
                srcPrd = productService.get(srcPrd.getFlowAccountProductId());
            }

            //先检验自身账户，自身账户充足，校验通过
            Account selfAccount = accountMapper.getByOwnerIdAndPrdId(entId, srcPrd.getId(),
                    AccountType.ENTERPRISE.getValue());
            if (selfAccount != null) {
                String selfKey = selfAccount.getId() + "#" + srcPrd.getType();
                if (selfAccountMap.get(selfKey) == null) {//之前没扣过
                    selfAccountMap.put(selfKey, selfAccount.getCount());
                }
                Double selfBalance = selfAccountMap.get(selfKey);
                if (ProductType.FLOW_ACCOUNT.getValue() == srcPrd.getType().byteValue()
                        || ProductType.MOBILE_FEE.getValue() == srcPrd.getType().byteValue()) {//流量池产品、话费产品
                    selfBalance -= srcProductSize;
                    if (selfBalance >= 0) {//自身账户余额充足，检验通过，进行下一产品校验
                        selfAccountMap.put(selfKey, selfBalance);
                        continue;
                    }
                }

                if (ProductType.FLOW_PACKAGE.getValue() == srcPrd.getType().byteValue()) {//流量包产品
                    selfBalance -= 1;
                    if (selfBalance >= 0) {//自身账户余额充足，检验通过
                        selfAccountMap.put(selfKey, selfBalance);
                        continue;
                    }
                }

                if (ProductType.CURRENCY.getValue() == srcPrd.getType().byteValue()) {//现金产品
                    selfBalance -= srcProductPrice;
                    if (selfBalance >= 0) {//自身账户余额充足，检验通过
                        selfAccountMap.put(selfKey, selfBalance);
                        continue;
                    }
                }
                
                if (ProductType.PRE_PAY_PRODUCT.getValue() == srcPrd.getType().byteValue()) {//预付费产品
                    selfBalance -= srcProductPrice;
                    if (selfBalance >= 0) {//自身账户余额充足，检验通过
                        selfAccountMap.put(selfKey, selfBalance);
                        continue;
                    }
                }
                
                if (ProductType.PRE_PAY_CURRENCY.getValue() == srcPrd.getType().byteValue()) {//预付费现金产品
                    selfBalance -= srcProductPrice;
                    if (selfBalance >= 0) {//自身账户余额充足，检验通过
                        selfAccountMap.put(selfKey, selfBalance);
                        continue;
                    }
                }

                //开始检验其他账户是否充足
                Boolean isDeduct = false;
                for (Product destPrd : destPrdList) {
                    if (!converterService.isInterdictConvert(srcPrd.getId(), destPrd.getId())) {//原产品是否可以转换为目标产品
                        Account account = accountMapper.getByOwnerIdAndPrdId(entId, destPrd.getId(),
                                AccountType.ENTERPRISE.getValue());
                        if (account != null) {
                            String otherKey = account.getId() + "#" + destPrd.getType();
                            if (otherAccountMap.get(otherKey) == null) {//之前没扣过
                                otherAccountMap.put(otherKey, account.getCount());
                            }
                            Double balance = otherAccountMap.get(otherKey);
                            //扣流量账户、话费账户
                            if (ProductType.FLOW_ACCOUNT.getValue() == destPrd.getType().byteValue()
                                    || ProductType.MOBILE_FEE.getValue() == destPrd.getType().byteValue()) {//流量池产品、话费产品
                                balance -= srcProductSize;
                                if (balance < 0) {//当前账号余额不足,校验下一账户
                                    continue;
                                } else {//当前账号余额充足，检验通过
                                    otherAccountMap.put(otherKey, balance);
                                    isDeduct = true;
                                    break;
                                }
                            }

                            if (ProductType.CURRENCY.getValue() == destPrd.getType().byteValue()) {//扣钱
                                balance -= srcProductPrice;
                                if (balance < 0) {//当前账号余额不足,校验下一账户，
                                    continue;
                                } else {//当前账号余额充足，检验通过
                                    otherAccountMap.put(otherKey, balance);
                                    isDeduct = true;
                                    break;
                                }
                            }

                            if (ProductType.FLOW_PACKAGE.getValue() == destPrd.getType().byteValue()) {//流量包产品
                                balance -= 1;
                                if (balance < 0) {//当前账号余额不足,校验下一账户，
                                    continue;
                                } else {//自身账户余额充足，检验通过
                                    otherAccountMap.put(otherKey, balance);
                                    isDeduct = true;
                                    break;
                                }
                            }
                            
                            if (ProductType.PRE_PAY_PRODUCT.getValue() == destPrd.getType().byteValue()) {//流量包产品
                                balance -= 1;
                                if (balance < 0) {//当前账号余额不足,校验下一账户，
                                    continue;
                                } else {//自身账户余额充足，检验通过
                                    otherAccountMap.put(otherKey, balance);
                                    isDeduct = true;
                                    break;
                                }
                            }
                            
                            if(ProductType.PRE_PAY_CURRENCY.getValue() == destPrd.getType().byteValue()) {//预付费资金
                                balance -= srcProductPrice;
                                if (balance < 0) {//当前账号余额不足,校验下一账户，
                                    continue;
                                } else {//当前账号余额充足，检验通过
                                    otherAccountMap.put(otherKey, balance);
                                    isDeduct = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                //当前产品没地方扣款，说明余额不足
                if (!isDeduct) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 仅用于校验随机红包
     * @author qinqinyan
     * */
    @Override
    public boolean verifyAccountForRendomPacket(Long entId, Long totalProductSize, Long productId) {
        if (totalProductSize == null) {
            return true;
        }
        if (entId != null && productId != null) {
            Product flowPoolProduct;
            Product product = productService.get(productId);
            if (product.getFlowAccountFlag().toString().equals("1")) {
                flowPoolProduct = productService.get(product.getFlowAccountProductId());
            } else {
                flowPoolProduct = product;
            }
            Double balance = 0D;
            if (flowPoolProduct != null) {
                Account flowPoolAccount = get(entId, flowPoolProduct.getId(), AccountType.ENTERPRISE.getValue());
                if (flowPoolAccount != null) {
                    if (flowPoolAccount.getCount().doubleValue() >= totalProductSize.doubleValue()) {
                        return true;
                    } else {
                        balance = totalProductSize.doubleValue() - flowPoolAccount.getCount().doubleValue();
                    }
                }
            }

            Product cashProduct = productService.getCurrencyProduct();
            if (cashProduct != null) {
                EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(flowPoolProduct.getId(),
                        entId);
                Account cashAccount = get(entId, cashProduct.getId(), AccountType.ENTERPRISE.getValue());
                if (entProduct != null && cashAccount != null) {
                    Double totalPrice = balance.doubleValue() / 1024 * flowPoolProduct.getPrice().intValue()
                            * entProduct.getDiscount().intValue() / 100;
                    if (cashAccount.getCount().doubleValue() >= totalPrice.doubleValue()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Account getPaypreAccByType(Long entId, ProductType prdType) {
        if (entId == null || prdType == null) {
            return null;
        }
        
        List<Account> list = accountMapper.getPaypreAccByType(entId, (int)prdType.getValue());
        
        return list.isEmpty()? null : list.get(0);
    }

    @Override
    public boolean updateAlertCount(Long id, Double newCount) {
        return accountMapper.updateAlertCount(id, newCount) > 0 ;
    }

    @Override
    public boolean updateStopCount(Long id, Double newCount) {
        return accountMapper.updateStopCount(id, newCount) > 0;
    }

    @Override
    public boolean updateMinCount(Long id, Double newCount) {
        return accountMapper.updateMinCount2(id, newCount) > 0;
    }

}
