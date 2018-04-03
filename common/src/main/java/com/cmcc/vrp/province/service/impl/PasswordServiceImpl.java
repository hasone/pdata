/**
 * @ClassName: PasswordServiceImpl.java
 * @Description: TODO
 * @author: sunyiwei
 * @date 2015年7月28日 - 上午9:27:40
 * @version : 1.0
 */

package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PasswordService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * 卡密数据生成与解析
 */
@Service("pwdService")
public class PasswordServiceImpl implements PasswordService {
    private static final Logger LOGGER = Logger.getLogger(PasswordServiceImpl.class);

    //序列号的Key
    private final String SERIAL_NUM_KEY = "serialNumKey";

    //密码序列号的最大值
    private final int MAX_VALUE = 1000000;

    @Autowired
    private GlobalConfigService globalConfigService;

    /*public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
        PasswordService service = (PasswordService) ctx.getBean("pwdService");

        List<String> pwds = service.batchGenerate(10);
        for (String pwd : pwds) {
            System.out.println(pwd);
            System.out.println(service.getYear(pwd));
        }

    }*/

    @Override
    public String generate() {
        //1. 得到状态值
        Integer currentValue = getSerialNum();

        //2. 生成密码
        return currentValue == null ? null : genRawPwd(++currentValue);
    }

    @Override
    public boolean isValidate(String password) {
        if (StringUtils.isBlank(password) || password.length() != 14) {
            return false;
        }

        return true;
    }

    /**
     * @see com.cmcc.mdrc.service.PasswordService#getYear(java.lang.String)
     */
    @Override
    public int getYear(String password) throws Exception {
        if (isValidate(password)) {
            return NumberUtils.toInt(String.valueOf(password.charAt(11)) + password.charAt(0));
        }

        throw new Exception("无效的卡密数据.");
    }

    //得到顺序数字
    private Integer getSerialNum() {
        String oldValue = globalConfigService.get(SERIAL_NUM_KEY);

        int newValue = NumberUtils.toInt(oldValue) + 1;
        if (globalConfigService.updateValue(SERIAL_NUM_KEY, String.valueOf(newValue), oldValue)) {
            return newValue;
        }

        LOGGER.error("获取卡密的顺序数字时出错...");
        return null;
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

    private String genRawPwd(int serialNum) {
        //生成顺序数字
        serialNum = serialNum % MAX_VALUE;
        String szSerialNum = String.valueOf(serialNum);
        szSerialNum = StringUtils.leftPad(szSerialNum, 6, '0');

        //生成随机数字
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        String szRanNum = sb.toString();

        //拼装
        return combine(szSerialNum, szRanNum);
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

    /**
     * @see com.cmcc.mdrc.service.PasswordService#batchGenerate(int)
     */
    @Override
    public List<String> batchGenerate(int count) {
        List<Integer> nums = getSerialNums(count);
        if (nums == null || nums.size() == 0) {
            return null;
        }

        List<String> pwds = new ArrayList<String>();
        for (Integer num : nums) {
            pwds.add(genRawPwd(num));
        }

        return pwds;
    }


}
