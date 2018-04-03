package com.cmcc.vrp.boss.shangdong.boss.service;

/**
 * 
* <p>Description: 客户经理操作类</p>
* @author panxin
* @date 2016年11月12日 上午11:01:16
 */
public interface SdManagerService {
    /**
     * 创建客户经理
     * @param userToken token值
     * @return administer表的id指
     */
    String createManager(String userToken);

    /**
     * 修改客户经理
     * @param userToken token值
     * @return  是否修改成功
     */
    boolean updateManager(String userToken);
}
