/**
 * @Title: 	XinjiangBossService.java 
 * @Package com.cmcc.xinjiang.boss.service 
 * @author:	qihang
 * @date:	2016年3月29日 下午10:59:37 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.service;

import com.cmcc.vrp.boss.xinjiang.response.GroupInfoResp;
import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.ResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;

/** 
 * @ClassName:	XinjiangBossService 
 * @Description:  新疆的boss接口服务类
 * @author:	qihang
 * @date:	2016年3月29日 下午7:59:37 
 *  
 */
public interface XinjiangBossService {
	
	/**
	 * 5.1.	集团用户信息查询
	 * 参数：
	 * group_id：集团ID
	 * 
	 * 返回：
	 * GroupInfoResp 集团的基本信息，详情请参考该类
	 */
    GroupInfoResp getGetGroupInfo(String groupId);
	
	
	/**
	 * 5.6.	集团用户产品流量池信息查询
	 * 参数：
	 * group_id：集团ID
	 * 
	 * 返回：
	 * ResourcePoolResp 集团用户产品流量池的信息，详情请参考该类
	 */
    ResourcePoolResp getResourcePoolResp(String groupId);
	
	/**
	 * 5.2.	集团用户产品流量池信息查询
	 * 参数：
	 * group_id：集团ID
	 * 
	 * 返回：
	 * ResourcePoolResp 集团用户产品流量池的信息，详情请参考该类
	 */
    NewResourcePoolResp getResourcePoolRespNew(String groupId);
	
	/**
	 * 5.4.	集团流量转赠接口
	 * 参数：
	 * group_id：集团ID
	 * user_id：集团产品USER_ID，由5.6集团用户产品流量池信息查询中返回。
	 * phone：手机号
	 * flowNum： 转赠流量值（整数）
	 * serialNum： 使用流水号（注意唯一），格式长度现在还没有限制
	 * 
	 * 返回：
	 * SendResp 转增的基本信息，详情请参考该类
	 */
    SendResp getSendResp(String groupId,String userId,String phone,String flowNum,String serialNum);
	
	
	/**
	 * 5.1.	集团流量转赠接口(新版)
	 * 参数：
	 * group_id：集团ID
	 * user_id：集团产品USER_ID，由5.6集团用户产品流量池信息查询中返回。
	 * phone：手机号
	 * flowNum： 转赠流量值（整数）
	 * serialNum： 使用流水号（注意唯一），格式长度现在还没有限制
	 * 
	 * 返回：
	 * SendResp 转增的基本信息，详情请参考该类
	 */
    SendResp getNewSendResp(String groupId,String phone,String flowNum,String serialNum);

}
