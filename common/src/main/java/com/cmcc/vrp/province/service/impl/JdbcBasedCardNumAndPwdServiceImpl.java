package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcCardInfoMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.PasswordService;
import com.cmcc.vrp.util.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

/**
 * 基于数据库实现的卡号卡密服务
 * <p>
 * Created by sunyiwei on 2016/11/29.
 */
public abstract class JdbcBasedCardNumAndPwdServiceImpl implements CardNumAndPwdService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(JdbcBasedCardNumAndPwdServiceImpl.class);

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    MdrcCardInfoMapper mdrcCardInfoMapper;

    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;

    @Autowired
    MdrcCardmakerService mdrcCardmakerService;

    @Autowired
    PasswordService passwordService;

    @Override
    public final boolean validate(String cardNum, String password) {
        if (!checkCardNum(cardNum)) {
            LOGGER.error("校验卡号规则失败. CardNum = {}.", cardNum);
            return false;
        }

        if (!checkPassword(password)) {
            LOGGER.error("校验卡密规则失败. Password = {}.", password);
            return false;
        }

        //校验卡号卡密是否匹配
        return checkMatch(cardNum, password);
    }

    @Override
    public MdrcCardInfo get(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return null;
        }

        int year = getIntYear(cardNum);
        if (year == 0) {
            return null;
        }

        return mdrcCardInfoMapper.selectByCardNumber(year, cardNum);
    }

    @Override
    public void generatePasswords(List<MdrcCardInfo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (MdrcCardInfo mdrcCardInfo : list) {
            String clearPwd = genClearPwd(); //生成明文密码
            //将明文密码塞入对象，否则下载明文密码为空
            mdrcCardInfo.setClearPsw(clearPwd);

            //获得加密的密码
            String salt = com.cmcc.vrp.util.StringUtils.randomString(64);
            String encodedPwd = passwordEncoder.encode(clearPwd, salt);

            //设置加密盐和密文密码
            mdrcCardInfo.setSalt(salt);
            mdrcCardInfo.setCardPassword(encodedPwd);
        }
    }
    
    @Override
    public List<MdrcCardInfo> getBycreateTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getBycreateTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getByStoredTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByStoredTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getByBoundTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByBoundTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getByActivatedTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByActivatedTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getByUsedTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByUsedTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getByLockedTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByLockedTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getBySerialNum(Long configId, String year) {
        if (configId == null || year == null) {
            LOGGER.error("无效的查询参数. configId = {}. year = {}", configId, year);
            return null;
        }
        if (year.length() == 4) {
            year = year.substring(2, 4);
        }
        return mdrcCardInfoMapper.getBySerialNum(configId, NumberUtils.toInt(year, 0));
    }
    
    @Override
    public List<MdrcCardInfo> getByDeactivateTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByDeactivateTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    @Override
    public List<MdrcCardInfo> getByUnlockTime(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String.valueOf(start.getYear());
        if (yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByUnlockTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }
    @Override
    public List<MdrcCardInfo> getBill(DateTime start, DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            LOGGER.error("无效的查询参数. StartTime = {}， EndTime = {}.", start, end);
            return null;
        }
        String yearStr = String .valueOf(start.getYear());
        if(yearStr.length() == 4) {
            yearStr = yearStr.substring(2, 4);
        }
        return mdrcCardInfoMapper.getByUsedTime(start.toDate(), end.toDate(), NumberUtils.toInt(yearStr, 0));
    }

    /**
     * 校验卡号卡密是否匹配
     *
     * @param cardNum  卡号
     * @param password 卡密
     * @return
     */
    protected boolean checkMatch(String cardNum, String password) {
        String expectedPwd = null;
        MdrcCardInfo mdrcCardInfo = get(cardNum);
        if (mdrcCardInfo != null && StringUtils.isNotBlank(mdrcCardInfo.getSalt())) {
            password = passwordEncoder.encode(password, mdrcCardInfo.getSalt());
        }

        return mdrcCardInfo != null
            && StringUtils.isNotBlank(expectedPwd = mdrcCardInfo.getCardPassword())
            && StringUtils.isNotBlank(password)
            && expectedPwd.equals(password);
    }

    /**
     * 校验卡号是否符合规则： 长度， 格式等等
     *
     * @param cardNum 卡号
     * @return
     */
    protected abstract boolean checkCardNum(String cardNum);

    /**
     * 校验明文卡密是否符合规则: 长度等等
     *
     * @param password 卡密
     * @return
     */
    protected abstract boolean checkPassword(String password);

    /**
     * 为单张卡号生成相应的密码， 注意这里返回的是明文密码
     *
     * @return
     */
    protected String genClearPwd() {
        final int length = getPasswordLength();

        //生成随机数字
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    /**
     * 卡号长度
     *
     * @return
     */
    protected abstract int getCardNumLength();

    /**
     * 卡密长度
     *
     * @return
     */
    protected abstract int getPasswordLength();

    /**
     * 根据卡号获取年份信息
     *
     * @param cardNum 卡号
     * @return
     */
    protected abstract int getIntYear(String cardNum);
}
