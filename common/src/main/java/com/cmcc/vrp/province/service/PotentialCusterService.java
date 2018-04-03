package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Enterprise;

/**
 * 潜在客户服务类
 * <p>
 * Created by sunyiwei on 2016/3/28.
 */
public interface PotentialCusterService {


    /**
     * 创建潜在用户
     *
     * @param enterprise
     * @param cmPhone
     * @param emPhone
     * @param emName
     * @param currentUserId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    public boolean savePotentialEnterprise(Enterprise enterprise, String cmPhone,
                                           String emPhone, String emName, Long currentUserId);

    /**
     * 更新潜在用户
     *
     * @param enterprise
     * @param cmPhone
     * @param emPhone
     * @param emName
     * @param currentUserId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    boolean saveEditPotential(Enterprise enterprise, String cmPhone,
                              String emPhone, String emName, Long currentUserId);
}
