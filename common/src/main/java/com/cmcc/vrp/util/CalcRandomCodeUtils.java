package com.cmcc.vrp.util;

import com.cmcc.vrp.enums.CalcRandomCodeOperationType;

import java.util.Random;

import static com.cmcc.vrp.enums.CalcRandomCodeOperationType.Add;
import static com.cmcc.vrp.enums.CalcRandomCodeOperationType.Minus;

/**
 * 随机验证码（运算） 模型
 *
 * @author qihang
 */
public class CalcRandomCodeUtils {

    public final Random random = new Random();
    /**
     * 值1
     */
    private int num1;
    /**
     * 值2
     */
    private int num2;
    /**
     * 运算类型
     */
    private CalcRandomCodeOperationType oper;

    public CalcRandomCodeUtils(int num1, int num2, CalcRandomCodeOperationType opera) {
        this.num1 = num1;
        this.num2 = num2;
        this.oper = opera;
    }

    /**
     * 随机初始化，static方法
     */
    public static CalcRandomCodeUtils randomModel() {
        Random random = new Random();
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);

        CalcRandomCodeOperationType oper = random.nextBoolean() ? Add : Minus;
        return new CalcRandomCodeUtils(num1, num2, oper);
    }

    /**
     * 得到表达式
     */
    public String getExpression() {
        return num1 + oper.getOper() + num2 + "=";
    }

    /**
     * 得到运算结果
     */
    public int getValue() {
        switch (oper) {
            case Add:
                return num1 + num2;
            case Minus:
                return num1 - num2;
            default:
                return 0;
        }
    }
}
