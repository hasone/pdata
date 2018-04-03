package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 广东营销卡卡号卡密规则
 * <p>
 * 1. 卡号： 16位：专用卡区分1位、省份代码2位、批次3位、年份2位、预留2位、批次内序号6位
 * 2. 密码：8位
 * <p>
 * Created by sunyiwei on 2016/11/29.
 */
public class GdCardNumAndPwdServiceImpl extends JdbcBasedCardNumAndPwdServiceImpl {
    @Override
    protected boolean checkCardNum(String cardNum) {
        return StringUtils.isNotBlank(cardNum)
            && Pattern.compile("^\\d{16}$").matcher(cardNum).matches(); //16位数字卡号
    }

    @Override
    protected boolean checkPassword(String password) {
        return StringUtils.isNotBlank(password)
            && Pattern.compile("^\\d{8}$").matcher(password).matches(); //8位数字随机密码
    }

    @Override
    protected int getCardNumLength() {
        return 16;
    }

    @Override
    protected int getPasswordLength() {
        return 8;
    }

    @Override
    public int getIntYear(String cardNum) {
        if (!checkCardNum(cardNum)) {
            return 0;
        }

        return NumberUtils.toInt(cardNum.substring(6, 8), 0);
    }

    @Override
    public List<String> generatCardNums(MdrcBatchConfig mdrcBatchConfig) {
        //0. 校验
        if (mdrcBatchConfig == null
            || mdrcBatchConfig.getSerialNumber() == null
            || mdrcBatchConfig.getAmount() <= 0) {
            LOGGER.error("卡批次信息不能为空， 或卡批次信息不正确. MdrcBatchConfig = {}.", mdrcBatchConfig == null ? "空" : new Gson().toJson(mdrcBatchConfig));
            return null;
        }

        //专用卡区分1位、省份代码2位、批次3位、年份2位、预留2位、批次内序号6位
        final String type = "0"; //专用卡标识，保留
        final String provindeCode = "44"; //广东省省份编码， 参考http://platform.eeeeee.org/info/country/
        final String batchNum = StringUtils.leftPad(String.valueOf(mdrcBatchConfig.getSerialNumber()), 3, "0");
        final String preserved = "00";
        final String year = String.valueOf(new DateTime().getYear()).substring(2, 4);//年份占2位，获取最后2位

        final int count = mdrcBatchConfig.getAmount().intValue();
        List<String> cardNums = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            cardNums.add(type + provindeCode + batchNum + year + preserved + StringUtils.leftPad(String.valueOf(i), 6, "0"));
        }

        return cardNums;
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
            LOGGER.error("无效的起始卡号， CardNum = {}.", beginCardNum);
            return null;
        }

        //批次内序号6位
        String prefix = beginCardNum.substring(0, 10);
        String num = beginCardNum.substring(10);

        long beginNum = NumberUtils.toLong(num, -1);
        long endNum = beginNum + delta;

        return prefix + StringUtils.leftPad(String.valueOf(endNum), 6, "0");
    }
}
