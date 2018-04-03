package com.cmcc.vrp.province.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cmcc.vrp.util.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.SerialFlagStatus;
import com.cmcc.vrp.enums.SignAchieveCoinType;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.model.WxSerialSignRecord;
import com.cmcc.vrp.province.model.WxSignDetailRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.SignInService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.service.WxSerialSignRecordService;
import com.cmcc.vrp.province.service.WxSignDetailRecordService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 签到服务
 *
 * Created by sunyiwei on 2017/2/23.
 */
@Service
public class SignInServiceImpl extends AbstractCacheSupport implements SignInService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInServiceImpl.class);

    // 每天最多允许签到的次数, 暂时固定为1
    private final int MAX_PER_DAY = 1;

    // 每天最多能签到的次数, 超过这个次数就要被列入黑名单了
    private final int FANSHUA_MAX_PER_DAY = 100;

    @Autowired
    private IndividualAccountService individualAccountService;

    //@Autowired
    //private AdministerService administerService;

    @Autowired
    private IndividualProductService individualProductService;

    @Autowired
    WxSignDetailRecordService wxSignDetailRecordService;

    @Autowired
    WxSerialSignRecordService wxSerialSignRecordService;

    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    WxAdministerService wxAdministerService;

    @Override
    public String getSignTime(Long adminId) {
        // TODO Auto-generated method stub
        String signTime = cacheService.get(getTodaySignTimeKey(adminId.toString()));
        if (StringUtils.isEmpty(signTime)) {
            return null;
        } else {
            Date time = DateUtil.parse("yyyy-MM-dd HH:mm:ss", signTime);
            String str = DateUtil.dateToString(time, "HH:mm");
            return str;
        }
    }

    @Override
    public String getAchieveCoinCountToday(Long adminId) {
        // TODO Auto-generated method stub
        if (cacheService.exists(getSignAchieveCoinKey(adminId.toString()))) {
            return cacheService.get(getSignAchieveCoinKey(adminId.toString()));
        }
        return "0";
    }

    /**
     * 新签到 1、一个月内最多赠送21个流量币（及有效签到数是21）
     * 2、每天只能签到1次，1次赠送一个流量币，一个月内连续签到21天，第21天额外赠送10个流量币 3、不能跨月份进行累加
     * 
     * fix pdata-1958 签到规则修改为： by qinqinyan on 2017/09/06
     * 1、每天早上7点开放签到，前10000名签到者每人可获得1个流量币，每日流量币数量有限，先到先得；
     * 2、10000名后签到者没有流量币奖励，可累积签到次数；每个用户每个自然月累积签到21次， 在第21次签到成功时可获得10个流量币奖励。
     */
    @Override
    public Map<String, String> newSign(String mobile, Date signTime) {
        Map<String, String> map = new HashMap<String, String>();
        LOGGER.info("签到 mobile = {}", mobile);

        Boolean result = false;
        String msg = "";

        Boolean isLimitAfter = false;
        Boolean isBefore21 = false;
        Boolean is21Continue = false;
        Boolean is21After = false;

        WxAdminister administer = null;
        if (!StringUtils.isEmpty(mobile) && ((administer = wxAdministerService.selectByMobilePhone(mobile)) != null)) {
            String signInFlag = cacheService.get(getSignInFlagKey(administer.getId().toString()));
            // 校验用户今天是否已经签到
            if ("true".equals(signInFlag)) {
                LOGGER.info("用户 id = {}， 对应手机号  = {}已经签到过，不可以重复签到。", administer.getId(), mobile);
                msg = "今天已经签到过了，不能重复签到哟!";
            } else {
                // 先设置缓存已经签到，避免重复签到
                if (cacheService.setNxAndExpireTime(getSignInFlagKey(administer.getId().toString()), "true",
                        DateUtil.getLeftTimeToday() + 5 * 60)) {
                    // 设置用户签到列表
                    Long rank = cacheService.lPush(getSignTotalUsersKey(),
                            getSignInFlagKey(administer.getId().toString()), DateUtil.getLeftTimeToday() + 5 * 60);
                    LOGGER.info("用户 id = {}签到排名 rank = {}", administer.getId(), rank);
                    if (rank != null && rank.longValue() > 0) {
                        // 设置用户签到排名,延迟五分钟自动清除该记录，保证业务能正常执行完成
                        if (cacheService.setNxAndExpireTime(getSignRankKey(administer.getId().toString()),
                                rank.toString(), DateUtil.getLeftTimeToday() + 5 * 60)) {
                            // 缓存签到时间
                            if (cacheService.setNxAndExpireTime(getTodaySignTimeKey(administer.getId().toString()),
                                    DateUtil.dateToString(signTime, "yyyy-HH-dd HH:mm:ss"),
                                    DateUtil.getLeftTimeToday())) {
                                // 插入数据库
                                try {
                                    if (insertSignRecord(signTime, administer.getId())) {
                                        LOGGER.info("签到成功。adminId = {}", administer.getId());
                                        msg = "签到成功!";
                                        result = true;

                                        int rankCount = getSignRank(administer.getId());
                                        LOGGER.info("用户 = {} 的排名： rankCount = {}", administer.getId(), rankCount);
                                        if (rankCount > getSignMaxCountPerDay()) {
                                            isLimitAfter = true;
                                        }
                                        // 统计签到总天数
                                        int signCountThisMointh = getSignCount(administer.getId(), signTime);
                                        if (signCountThisMointh < getMaxSerialSignDay()) {
                                            isBefore21 = true;
                                            is21Continue = false;
                                            is21After = false;
                                        } else if (signCountThisMointh == getMaxSerialSignDay()) {
                                            isBefore21 = false;
                                            is21Continue = true;
                                            is21After = false;
                                        } else {
                                            isBefore21 = false;
                                            is21Continue = false;
                                            is21After = true;
                                        }
                                    } else {
                                        LOGGER.info("签到失败。adminId = {}", administer.getId());
                                        msg = "签到失败!";
                                        delSignCache(true, true, true, true, administer.getId());
                                    }
                                } catch (RuntimeException e) {
                                    LOGGER.info("签到失败。adminId = {}, 失败原因：{}", administer.getId(), e.getMessage());
                                    msg = "签到失败!";
                                    delSignCache(true, true, true, true, administer.getId());
                                }
                            } else {
                                LOGGER.info("缓存用户签到时间失败，删除已经签到标示、签到用户列表数据和签到排名.adminId = {}", administer.getId());
                                delSignCache(true, true, true, false, administer.getId());
                            }
                        } else {
                            LOGGER.info("设置用户签到排名失败，删除已经签到标示和签到用户列表数据.adminId = {}", administer.getId());
                            delSignCache(true, true, false, false, administer.getId());
                        }

                    } else {
                        LOGGER.info("设置用户签到列表失败，删除已经签到标示.adminId = {}", administer.getId());
                        delSignCache(true, false, false, false, administer.getId());
                    }
                } else {
                    LOGGER.info("缓存设置签到失败.adminId = {}", administer.getId());
                    msg = "签到失败!";
                }
            }
        } else {
            LOGGER.info("参数异常，mobile = {}签到失败", mobile);
            msg = "参数异常，签到失败!";
        }
        map.put("isLimitAfter", isLimitAfter.toString());
        map.put("isBefore21", isBefore21.toString());
        map.put("is21Continue", is21Continue.toString());
        map.put("is21After", is21After.toString());
        map.put("time", DateUtil.dateToString(signTime, "yyyy-MM-dd HH:mm:ss"));

        map.put("result", result.toString());
        map.put("msg", msg);
        return map;
    }

    /**
     * 签到失败，删除签到缓存数据
     */
    private void delSignCache(Boolean signInFlag, Boolean valueOnList, Boolean signRank, Boolean signTime,
            Long adminId) {
        if (signInFlag) {
            if (!cacheService.delete(getSignInFlagKey(adminId.toString()))) {
                LOGGER.info("删除已经签到标识失败，key = {}", getSignInFlagKey(adminId.toString()));
            }
        }
        if (valueOnList) {
            if (!cacheService.deleteValueOnList(getSignTotalUsersKey(), 1, getSignInFlagKey(adminId.toString()))) {
                LOGGER.info("删除签到用户列表里数据失败，value = {}", getSignInFlagKey(adminId.toString()));
            }
        }
        if (signRank) {
            if (!cacheService.delete(getSignRankKey(adminId.toString()))) {
                LOGGER.info("删除已经签到排名失败，key = {}", getSignInFlagKey(adminId.toString()));
            }
        }
        if (signTime) {
            if (!cacheService.delete(getTodaySignTimeKey(adminId.toString()))) {
                LOGGER.info("删除签到时间失败，key = {}", getSignInFlagKey(adminId.toString()));
            }
        }
    }

    /***
     * 获取当天排名
     */
    private Integer getSignRank(Long adminId) {
        String rank = cacheService.get(getSignRankKey(adminId.toString()));
        try {
            return Integer.parseInt(rank);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取连续签到次数
     */
    private int getSignSerialCount(Long adminId) {
        String count = cacheService.get(getSignSerialDaysKey(adminId.toString()));
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取最大有效签到次数
     */
    public int getMaxSerialSignDay() {
        String count = globalConfigService.get(GlobalConfigKeyEnum.MAX_SERIAL_SIGN_DAY.getKey());
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取每天签到赠送流量币个数
     */
    public int getSignGiveCountPeyDay() {
        String count = globalConfigService.get(GlobalConfigKeyEnum.SIGN_GIVE_COUNT_PER_DAY.getKey());
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取连续签到21天赠送的流量币个数
     */
    public int getSignAwardCount() {
        String count = globalConfigService.get(GlobalConfigKeyEnum.SIGN_AWARD_COUNT.getKey());
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 每天签到能获得流量币最大签到人数
     */
    public int getSignMaxCountPerDay() {
        String count = globalConfigService.get(GlobalConfigKeyEnum.SIGN_MAX_COUNT_PER_DAY.getKey());
        LOGGER.info("每天签到最大奖励人数  = {}", count);
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            LOGGER.error("获取每天签到最大奖励人数失败，失败原因：  {} ", e.getMessage());
            return 0;
        }
    }

    public int getSignCount(Long adminId, Date signTime) {
        int signCount = wxSerialSignRecordService.getTotalCountByAdminIdAndMonth(adminId, signTime);
        return signCount;
    }

    /**
     * eidt by qinqinyan on 2017/09/06 为了兼容线上情况，首先查找用户总共签到次数
     * 
     */
    @Transactional
    public boolean insertSignRecord(Date signTime, Long adminId) {
        WxAdminister administer = wxAdministerService.selectWxAdministerById(adminId);

        // 在操作数据前，先获取用户这个月总签到次数
        int signCountThisMonth = getSignCount(adminId, signTime);
        LOGGER.info("用户adminId = {} 这个月累计签到次数  {} 次", adminId, signCountThisMonth);

        Map map = new HashMap<String, String>();
        map.put("adminId", adminId.toString());
        String startTime = DateUtil.dateToString(DateUtil.getBeginMonthOfDate(DateUtil.getBeginOfDay(signTime)),
                "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(signTime);
        int month = calendar.get(Calendar.MONTH) + 1;// 月
        int year = calendar.get(Calendar.YEAR); // 年
        String endTime = DateUtil.dateToString(DateUtil.getEndTimeOfMonth(year, month), "yyyy-MM-dd HH:mm:ss");
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        List<WxSerialSignRecord> list = wxSerialSignRecordService.selectByMap(map);

        WxSerialSignRecord wxSerialSignRecord = null;
        if (list == null || list.size() < 1) {
            /**
             * 历史说明： 原来是要统计连续签到，因此这个对象的count是连续签到次数，所以会出现一个用户在一个月内有多条该记录
             * 2017/09上新版本后，那么count是统计用户每个月签到总次数
             */
            // 未查到签到次数统计总记录，直接插入
            wxSerialSignRecord = createWxSerialSignRecord(adminId, signTime);
            if (!wxSerialSignRecordService.insertSelective(wxSerialSignRecord)) {
                LOGGER.info("插入连续签到记录失败。signTime = {}, adminId = {}", signTime, adminId);
                throw new RuntimeException();
            }
        } else {
            wxSerialSignRecord = list.get(0);
            boolean flag = true;
            /**
             * 判断是否是连续签到
             * step1：比较今天签到时间月份是否与开始签到时间的月份是否一样，如果不一样，则需要重新插入新纪录；反之就到step2
             * step2：比较今天签到时间和开始签到时间之间间隔的天数是否等于累计的天数，如果不等于，则说明已经不连续，需要重新插入新纪录
             */
            if (DateUtil.verifySameMonth(signTime, wxSerialSignRecord.getStartTime())) {
                // long cnt =
                // DateUtil.getDays(wxSerialSignRecord.getStartTime(),
                // signTime);
                // if(cnt == (wxSerialSignRecord.getCount().longValue()+1)){
                WxSerialSignRecord updateRecord = new WxSerialSignRecord();
                updateRecord.setId(wxSerialSignRecord.getId());
                updateRecord.setCount(wxSerialSignRecord.getCount().intValue() + 1);
                updateRecord.setUpdateTime(new Date());

                wxSerialSignRecord.setCount(wxSerialSignRecord.getCount().intValue() + 1);
                if (!wxSerialSignRecordService.updateByPrimaryKeySelective(updateRecord)) {
                    LOGGER.info("更新签到记录失败。id = {}, count = {}", updateRecord.getId(), updateRecord.getCount());
                    throw new RuntimeException();
                }
                // 将标志置为false，不要重新生成统计签到天数记录
                flag = false;
                // }
            }

            // 说明签到时间与统计签到记录的时间不是同一个月
            if (flag) {
                wxSerialSignRecord = createWxSerialSignRecord(adminId, signTime);
                if (!wxSerialSignRecordService.insertSelective(wxSerialSignRecord)) {
                    LOGGER.info("插入连续签到记录失败。signTime = {}, adminId = {}", signTime, adminId);
                    throw new RuntimeException();
                }
            }
        }

        // 2、插入签到明细
        WxSignDetailRecord signRecord = createWxSignDetailRecord(signCountThisMonth, wxSerialSignRecord, signTime,
                false);
        if (!wxSignDetailRecordService.insertSelective(signRecord)) {
            LOGGER.info("插入签到详细记录失败。serialSignId = {}", signRecord.getSerailSignId());
            throw new RuntimeException();
        }

        /**
         * edit by qinqinyan on 2017/09/06 开始赠送流量币：
         * 1、10000（这个数据可以配置）名以后不允许赠送签到获得的1（这个数据可以配置）个流量币；
         * 2、总签到次数超过21（这个数据可以配置）次后不赠送流量币；
         * 3、在10000（这个数据可以配置）名后签到虽然不赠送流量币，但是累计签到次数
         */
        if (signCountThisMonth + 1 <= getMaxSerialSignDay()) {
            // && getSignRank(adminId)!=null &&
            // getSignRank(adminId).intValue()<=getSignMaxCountPerDay()
            Integer totalAchievieCoinCount = 0;

            // 获取积分产品
            IndividualProduct individualPointProduct = individualProductService.getIndivialPointProduct();
            if (individualPointProduct == null) {
                LOGGER.error("获取个人积分产品时返回空值,请确认是否配置了个人积分产品!");
                LOGGER.error("为用户增加积分时失败,用户签到失败. adminId = {}, SerialNum = {}.", adminId, signRecord.getSerialNum());
                throw new RuntimeException();
            }

            // 签到
            if (signRecord.getCoinCount().intValue() > 0) {
                if (!individualAccountService.addCountForcely(administer.getMobilePhone(),
                        individualPointProduct.getId(), signRecord.getSerialNum(),
                        new BigDecimal(signRecord.getCoinCount()), ActivityType.SIGN_IN, "签到")) {
                    LOGGER.error("为用户增加积分时失败,用户签到失败. adminId = {}, SerialNum = {}.", adminId,
                            signRecord.getSerialNum());
                    throw new RuntimeException();
                }
            }

            totalAchievieCoinCount += signRecord.getCoinCount().intValue();

            // 如果连续签到达到21天，额外增加赠送10个流量币
            if (signCountThisMonth + 1 == getMaxSerialSignDay()) {
                WxSignDetailRecord awardRecord = createWxSignDetailRecord(signCountThisMonth, wxSerialSignRecord,
                        signTime, true);
                if (!wxSignDetailRecordService.insertSelective(awardRecord)) {
                    LOGGER.info("插入签到奖励记录失败。serialSignId = {}", awardRecord.getSerailSignId());
                    throw new RuntimeException();
                }

                if (!individualAccountService.addCountForcely(administer.getMobilePhone(),
                        individualPointProduct.getId(), awardRecord.getSerialNum(),
                        new BigDecimal(awardRecord.getCoinCount()), ActivityType.SIGN_IN, "签到-连续21天奖励")) {
                    LOGGER.error("为用户增加积分时失败,用户签到失败. adminId = {}, SerialNum = {}.", adminId,
                            signRecord.getSerialNum());
                    throw new RuntimeException();
                }

                totalAchievieCoinCount += awardRecord.getCoinCount().intValue();
            }

            // 签到
            setSignAchieveCoinCount(adminId, totalAchievieCoinCount);
        }

        // 将连续签到次数更新到缓存
        cacheService.setNxAndExpireTime(getSignSerialDaysKey(adminId.toString()),
                wxSerialSignRecord.getCount().toString(), 0);
        return true;
    }

    /**
     * 设置今天获得的流量币
     */
    private void setSignAchieveCoinCount(Long adminId, Integer count) {
        if (cacheService.exists(adminId.toString())) {
            try {
                cacheService.incrby(getSignAchieveCoinKey(adminId.toString()), count.longValue());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        } else {
            if (!cacheService.setNxAndExpireTime(getSignAchieveCoinKey(adminId.toString()), count.toString(),
                    DateUtil.getLeftTimeToday() + 5 * 60)) {
                LOGGER.info("设置今天获取到的流量币失败adminId = {}, count = {}", adminId, count);
            }
        }
    }

    /**
     * 构造统计连续签到记录对象
     * 
     * @param adminId
     *            用户id
     * @param signTime
     *            签到时间
     */
    private WxSerialSignRecord createWxSerialSignRecord(Long adminId, Date signTime) {
        WxSerialSignRecord record = new WxSerialSignRecord();
        record.setAdminId(adminId);
        record.setCount(1);
        record.setStartTime(signTime);
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        record.setSerialFlag(SerialFlagStatus.SERIAL.getValue());
        return record;
    }

    /**
     * 构造签到详细记录对象
     * 
     * @param wxSerialSignRecord
     * @param flag
     *            连续签到21天奖励标示，true，该条记录是签到奖励；false：该条记录只是普通签到奖励
     */
    private WxSignDetailRecord createWxSignDetailRecord(int signCountThisMonth, WxSerialSignRecord wxSerialSignRecord,
            Date signTime, boolean flag) {
        WxSignDetailRecord record = new WxSignDetailRecord();
        record.setAdminId(wxSerialSignRecord.getAdminId());
        record.setSerailSignId(wxSerialSignRecord.getId());
        record.setSignTime(signTime);
        record.setDeleteFlag(0);
        record.setUpdateTime(new Date());
        if (flag) {
            record.setCoinCount(getSignAwardCount());
            record.setType(SignAchieveCoinType.SIGN_AWARD_COIN.getCode());
        } else {

            if (signCountThisMonth + 1 <= getMaxSerialSignDay()) {
                // 未超过最大连续签到次数
                if (getSignRank(wxSerialSignRecord.getAdminId()).intValue() <= getSignMaxCountPerDay()) {
                    // 未超过每天赠送最大签到人数
                    record.setCoinCount(getSignGiveCountPeyDay());
                    record.setType(SignAchieveCoinType.SIGN_COIN.getCode());
                } else {
                    record.setCoinCount(0);
                    record.setType(SignAchieveCoinType.OUTNUMBER_MAX_SIGN_PER_DAY.getCode());
                }
            } else {
                record.setCoinCount(0);
                record.setType(SignAchieveCoinType.OUTNUMBER_MAX_SERIAL_SIGN_DAY.getCode());
            }
        }
        record.setSerialNum(SerialNumGenerator.buildSerialNum());
        return record;
    }

    /**
     * 签到
     *
     * 实现要考虑的内容包括:
     *
     * 1. 一定时间间隔内不允许重复签到
     *
     * 2. 签到获得的积分大小可控制
     *
     * @param mobile
     *            用户手机号
     * @param serialNum
     *            操作流水号
     */
    @Override
    public boolean sign(String mobile, String serialNum, int randomPoint) {
        if (StringUtils.isEmpty(mobile) || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isEmpty(serialNum)) {
            LOGGER.debug("无效的手机号码或流水号. Mobile = {}, SerialNum = {}.", mobile, serialNum);
            return false;
        }

        // 记录用户请求签到的次数, 方便后续防刷处理
        long signCount = cacheService.getIncrOrUpdate(mobile, secondsToEnd());
        if (signCount > FANSHUA_MAX_PER_DAY) {
            LOGGER.warn("用户[{}]的签到次数达到上限,将被列入黑名单,次日恢复.", mobile);
            return false;
        }

        // 然后判断当前用户是否已经签到
        if (signCount > MAX_PER_DAY) {// 缓存中的值不为空,且值超过上限,说明不能再次签到了
            LOGGER.info("用户[{}]的签到次数已经达到上限,不能继续签到.", mobile);
            return false;
        }

        // 获取积分产品
        IndividualProduct individualPointProduct = individualProductService.getIndivialPointProduct();
        if (individualPointProduct == null) {
            LOGGER.error("获取个人积分产品时返回空值,请确认是否配置了个人积分产品!");
            LOGGER.error("为用户增加积分时失败,用户签到失败. Mobile = {}, SerialNum = {}.", mobile, serialNum);
            return false;
        }

        // 对于没有签到过的用户, 随机产生积分, 并增加它的积分账户
        if (!individualAccountService.addCountForcely(mobile, individualPointProduct.getId(), serialNum,
                new BigDecimal(randomPoint), ActivityType.SIGN_IN, "签到")) {
            LOGGER.error("为用户增加积分时失败,用户签到失败. Mobile = {}, SerialNum = {}.", mobile, serialNum);
            return false;
        }

        // 将用户的签到记录到缓存
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!cacheService.sadd(getSignRecordsKey(mobile), date)) {
            LOGGER.error("为用户增加签到记录时失败,用户将无法查看到此次的签到记录! Mobile = {}, SerialNum = {}.", mobile, serialNum);

            // 这里不返回false,因为虽然记录失败了,但事实上用户的签到是成功的.
        }

        // 可以了!
        return true;
    }

    /**
     * 获取签到记录
     *
     * @param mobile
     *            用户手机号
     * @return 获取指定用户所有的签到记录, 每个字符串代表一个日期, 格式为yyyy-MM-dd
     */
    @Override
    public Set<String> records(String mobile) {
        return cacheService.smembers(getSignRecordsKey(mobile));
    }

    @Override
    protected String getPrefix() {
        return "sign.in.";
    }

    /**
     * 用户当月签到次数
     */
    private String getSignCountKey(String adminId) {
        return "sign.count." + adminId;
    }

    /**
     * 用户当天签到时间key
     * 
     * @author qinqinyan
     */
    private String getTodaySignTimeKey(String adminId) {
        return "sign.time." + adminId;
    }

    /**
     * 用戶當天是否已經簽到
     * 
     * @author qinqinyan
     */
    private String getSignInFlagKey(String adminId) {
        return "sign.flag." + adminId;
    }

    /**
     * 获取当前签到用户总数
     * 
     * @author qinqinyan
     */
    private String getSignTotalUsersKey() {
        return "sign.total.users";
    }

    /**
     * 签到用户的当天排名
     * 
     * @author qinqinyan
     */
    private String getSignRankKey(String adminId) {
        return "sign.rank." + adminId;
    }

    /**
     * 用户签到连续天数
     */
    private String getSignSerialDaysKey(String adminId) {
        return "sign.serial." + adminId;
    }

    /**
     * 用户今天获得流量币
     */
    private String getSignAchieveCoinKey(String adminId) {
        return "sign.achieve." + adminId;
    }

    // 用户签到记录键
    private String getSignRecordsKey(String mobile) {
        return "sign.records." + mobile;
    }

    // 获取从当前时间到当天最后一秒的时间差,以秒计数
    private int secondsToEnd() {
        DateTime now = new DateTime();
        DateTime end = now.plusDays(1).withTimeAtStartOfDay();

        return Seconds.secondsBetween(now, end).getSeconds();
    }

    @Override
    public Long getTotalSignCount() {
        // TODO Auto-generated method stub
        LOGGER.info("查找总参与人数key：" + getSignTotalUsersKey());
        Long totalSignCount = cacheService.getLengthOfList(getSignTotalUsersKey());
        LOGGER.info("总参与排名： ===" + totalSignCount);
        return totalSignCount == null ? 0L : totalSignCount;
    }

}
