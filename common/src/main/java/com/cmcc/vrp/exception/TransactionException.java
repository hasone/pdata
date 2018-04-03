/**
 * @Title: TransactionException.java
 * @Package com.cmcc.vrp.exception
 * @author: sunyiwei
 * @date: 2015年3月18日 下午5:17:43
 * @version V1.0
 */
package com.cmcc.vrp.exception;

/**
 * @ClassName: TransactionException
 * @Description: 事务失败异常
 * @author: sunyiwei
 * @date: 2015年3月18日 下午5:17:43
 *
 */
public class TransactionException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TransactionException() {
        super();
    }

    public TransactionException(String message) {
        super(message);
    }
}
