/**
 * @Title: SelfCheckException.java
 * @Package com.cmcc.flow.common.exception
 * @Description: TODO
 * @author: sunyiwei
 * @date: 2015-2-5 上午10:56:30
 * @version V1.0
 */
package com.cmcc.vrp.exception;

/**
 * @ClassName: SelfCheckException
 * @Description: 在javaBean对自身各字段进行有效性检查时，如果不通过，则抛出这个异常
 * @author: sunyiwei
 * @date: 2015-2-5 上午10:56:30
 *
 */
public class SelfCheckException extends Exception {
    public SelfCheckException() {

    }

    public SelfCheckException(String msg) {
        super(msg);
    }
}
