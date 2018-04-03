/**
 * @Title: UserInfoService.java
 * @Package com.cmcc.vrp.province.service
 * @author: sunyiwei
 * @date: 2015年6月9日 下午4:39:04
 * @version V1.0
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.UserInfo;

/**
 * @ClassName: UserInfoService
 * @Description: 转盘用户对象的服务
 * @author: sunyiwei
 * @date: 2015年6月9日 下午4:39:04
 *
 */
public interface UserInfoService {
    /*
     * 由用户ID获取对象
     */
    /** 
     * @Title: get 
    */
    UserInfo get(Long id);

    /*
     * 由用户手机号获取对象
     */
    /** 
     * @Title: get 
    */
    UserInfo get(String mobile);

    /*
     * 插入用户
     */
    /** 
     * @Title: insert 
    */
    boolean insert(UserInfo userInfo);

    /*
     * 根据ID删除用户
     */
    /** 
     * @Title: delete 
    */
    boolean delete(Long id);

    /*
     * 由用户手机号删除用户
     */
    /** 
     * @Title: delete 
    */
    boolean delete(String mobile);
}
