/**
 * @ClassName: PasswordService.java
 * @Description: TODO
 * @author: sunyiwei
 * @date 2015年7月28日 - 上午9:03:41
 * @version : 1.0
 */

package com.cmcc.vrp.province.service;

import java.util.List;


/**
 * 生成密码和解析密码
 */
public interface PasswordService {
    /** 
     * 生成密码
     * @Title: generate 
     */
    String generate();

    /** 
     * 批量生成密码
     * @Title: batchGenerate 
     */
    List<String> batchGenerate(int count);

    //校验密码
    boolean isValidate(String password);

    //得到年份
    int getYear(String password) throws Exception;
}
