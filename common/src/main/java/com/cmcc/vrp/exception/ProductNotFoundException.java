/**
 * @Title: ProductNotFoundException.java
 * @Package com.cmcc.vrp.exception
 * @author: qihang
 * @date: 2015年8月27日 下午3:17:26
 * @version V1.0
 */
package com.cmcc.vrp.exception;

/**
 * @ClassName: ProductNotFoundException
 * @Description: TODO
 * @author: qihang
 * @date: 2015年8月27日 下午3:17:26
 *
 */
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {

    }

    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
