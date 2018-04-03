package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.service.GlobalConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 默认的卡号卡密校验: 27位数字卡号+14位数字卡密
 * <p>
 * <p>
 * 卡密加密有两种情况
 * 1. 明文加密， 这时候card_password为明文，card_salt为空
 * 2. 哈希盐加密， card_password为密文，card_salt为加密盐
 * Created by sunyiwei on 2016/11/29.
 */
public class DefaultCardNumAndPwdServiceImpl extends JdbcBasedCardNumAndPwdServiceImpl {
    //序列号的Key
    private final String SERIAL_NUM_KEY = "serialNumKey";

    //密码序列号的最大值
    private final int MAX_VALUE = 1000000;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public void generatePasswords(List<MdrcCardInfo> list) {
        List<Integer> sns = getSerialNums(list.size());
        if (sns == null || sns.size() != list.size()) {
            return;
        }

        //生成顺序数字
        int index = 0;
        for (MdrcCardInfo mdrcCardInfo : list) {
            Integer serialNum = sns.get(index++) % MAX_VALUE;
            String szSerialNum = String.valueOf(serialNum);
            szSerialNum = StringUtils.leftPad(szSerialNum, 6, '0');

            //生成随机数字
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(random.nextInt(10));
            }

            String szRanNum = sb.toString();

            //拼装明文密码
            String clearPwd = combine(szSerialNum, szRanNum);
            mdrcCardInfo.setClearPsw(clearPwd);

            //加密密码
            String salt = com.cmcc.vrp.util.StringUtils.randomString(64);
            String encodedPwd = passwordEncoder.encode(clearPwd, salt);

            //设置加密盐和密文密码
            mdrcCardInfo.setSalt(salt);
            mdrcCardInfo.setCardPassword(encodedPwd);
        }
    }

    @Override
    protected boolean checkCardNum(String cardNum) {
        return StringUtils.isNotBlank(cardNum)
            && Pattern.compile("^\\d{27}$").matcher(cardNum).matches(); //27位数字
    }

    @Override
    protected boolean checkPassword(String password) {
        return StringUtils.isNotBlank(password)
            && Pattern.compile("^\\d{14}$").matcher(password).matches(); //14位数字
    }

    @Override
    protected int getCardNumLength() {
        return 27;
    }

    @Override
    protected int getPasswordLength() {
        return 14;
    }

    @Override
    public int getIntYear(String cardNum) {
        if (checkCardNum(cardNum)) {
            String yearStr = cardNum.substring(5, 7);
            // 长度两位的简写
            if (yearStr.length() == 4) {
                // 长度四位的年份取后两位
                yearStr = yearStr.substring(2, 4);
            }

            return NumberUtils.toInt(yearStr, 0);
        }

        return 0;
    }

    @Override
    public List<String> generatCardNums(MdrcBatchConfig mdrcBatchConfig) {
        //0. 校验
        if (mdrcBatchConfig == null) {
            LOGGER.error("卡批次信息不能为空.");
            return null;
        }

        MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcBatchConfig.getCardmakerId());
        if (mdrcCardmaker == null) {
            LOGGER.error("无效的卡商ID， 无法生成相应的卡号序列. CardMakerId = {}.", mdrcBatchConfig.getCardmakerId());
            return null;
        }

        final String busiCode = "00000"; //业务编码，暂时用不上，保留字段
        String year = mdrcBatchConfig.getThisYear();
        String provinceCode = mdrcBatchConfig.getProvinceCode();
        String cityCode = "00"; //城市编码， 保留字段，暂时用不上
        String providerCode = mdrcCardmaker.getSerialNumber();
        int batchSerialNum = mdrcBatchConfig.getSerialNumber();
        long count = mdrcBatchConfig.getAmount();

        return generateInternal(busiCode, year, provinceCode, cityCode,
            providerCode, batchSerialNum, count);
    }

    /**
     * 根据卡号和数量信息，计算终止卡号
     *
     * @param beginCardNum 起始卡号
     * @param delta        数量
     * @return 终止卡号，如果起始卡号+数量已经超过了该批次的最大卡号，返回空
     */
    @Override
    public String cal(String beginCardNum, int delta) {
        if (!checkCardNum(beginCardNum)) {
            LOGGER.error("无效的起始卡号， 起始卡号为{}.", beginCardNum);
            return null;
        }

        //卡号为最后9位
        String prefix = beginCardNum.substring(0, 18);
        long beginNum = NumberUtils.toLong(beginCardNum.substring(18), -1);
        if (beginNum == -1) {
            return null;
        }

        long endNum = beginNum + delta;
        return prefix + StringUtils.leftPad(String.valueOf(endNum), 9, "0");
    }

    /**
     * 卡序列号默认27位
     * <p>
     *
     * @param busiCode     业务编码,占5位
     * @param year         年份,占2位
     * @param provinceCode 省份编码,占2位
     * @param cityCode     地市编码,占2位
     * @param providerCode 制造商编码,占2位
     * @param batchNumber  批次号,占5位
     * @param count        生成数量，占9位
     * @return
     */
    private List<String> generateInternal(String busiCode, String year,
                                          String provinceCode, String cityCode,
                                          String providerCode, Integer batchNumber,
                                          Long count) {
        DecimalFormat df1 = new DecimalFormat("00000");//格式化生产卡的批号
        DecimalFormat df2 = new DecimalFormat("000000000");//格式化序列号
        List<String> resultData = new ArrayList<String>();
        if (busiCode == null || year == null || cityCode == null || providerCode == null || batchNumber == null || count == null) {
            return null;
        }
        if (busiCode.length() < 5) {
            busiCode = "10000";//设置默认值
        } else {
            busiCode = busiCode.substring(busiCode.length() - 5, busiCode.length());//业务编码占5位，截取最后5位
        }

        if (year.length() < 2) {
            year = String.valueOf(new DateTime().getYear()).substring(2, 4);
        } else {
            year = year.substring(year.length() - 2, year.length());//年份占2位，获取最后2位
        }

        if (provinceCode.length() < 2) {
            provinceCode = "50";//设置为默认值
        } else {
            provinceCode = provinceCode.substring(provinceCode.length() - 2, provinceCode.length());//省份编码占2位，获取最后2位
        }

        if (cityCode.length() < 2) {
            cityCode = "00";//设置为默认值
        } else {
            cityCode = cityCode.substring(cityCode.length() - 2, cityCode.length());//地市占2位，获取最后2位
        }

        if (providerCode.length() < 2) {
            providerCode = "00";//设置为默认值
        } else {
            providerCode = providerCode.substring(providerCode.length() - 2, providerCode.length());//制造商编码占2位，获取最后2位
        }

        for (int i = 1; i <= count; i++) {
            String temp = busiCode + year + provinceCode + cityCode + providerCode + df1.format(batchNumber) + df2.format(i);
            resultData.add(temp);
        }
        return resultData;
    }

    //拼装密码: 密码共14位，
    //其中第3,5,7,8,10,13位一共6位，按6位自然数递增顺序生成，
    //第2, 4,6,9,11,14位一共6位，6位均为随机生成数
    //第1，12位为年份
    private String combine(String serialNum, String randNum) {
        LOGGER.debug("顺序序列号为: " + serialNum);
        LOGGER.debug("随机序列号为: " + randNum);

        StringBuilder sb = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        String szYear = StringUtils.right(String.valueOf(calendar.get(Calendar.YEAR)), 2);

        int serialIndex = 0;
        int randIndex = 0;

        sb.append(szYear.charAt(1));//1
        sb.append(serialNum.charAt(randIndex++)); //2
        sb.append(serialNum.charAt(serialIndex++));//3
        sb.append(randNum.charAt(randIndex++));//4
        sb.append(serialNum.charAt(serialIndex++));//5
        sb.append(randNum.charAt(randIndex++));//6
        sb.append(serialNum.charAt(serialIndex++));//7
        sb.append(serialNum.charAt(serialIndex++));//8
        sb.append(randNum.charAt(randIndex++));//9
        sb.append(serialNum.charAt(serialIndex++));//10
        sb.append(randNum.charAt(randIndex++));//11
        sb.append(szYear.charAt(0));//12
        sb.append(serialNum.charAt(serialIndex++));//13
        sb.append(randNum.charAt(randIndex++));//14

        LOGGER.debug("生成的密码为: " + sb.toString());
        return sb.toString();
    }

    private List<Integer> getSerialNums(int count) {
        if (count <= 0) {
            return null;
        }

        String szOldValue = globalConfigService.get(SERIAL_NUM_KEY);
        int oldValue = NumberUtils.toInt(szOldValue);
        int newValue = oldValue + count;

        if (globalConfigService.updateValue(SERIAL_NUM_KEY, String.valueOf(newValue), szOldValue)) {
            List<Integer> nums = new ArrayList<Integer>();
            for (int i = oldValue + 1; i <= newValue; i++) {
                nums.add(i);
            }

            return nums;
        }

        LOGGER.error("获取卡密的顺序数字时出错...");
        return null;
    }
}
