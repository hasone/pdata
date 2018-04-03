package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Manager;

public interface EntManagerService {
    boolean insertEntManager(EntManager entManager);

    /**
     * 根据企业ID找到其所属的管理员节点ID
     *
     * @param enterId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    Long getManagerIdForEnter(Long enterId);

    /**
     * 根据企业ID找到其所属的管理员节点\Manager对象
     *
     * @param enterId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    Manager getManagerForEnter(Long enterId);
    
    /**
     * 根据企业Code找到其所属的管理员节点\Manager对象
     *
     * @param enterCode
     * @return
     * @date 2016年11月12日
     * @author panxin
     */
    Manager getManagerForEnterCode(String enterCode);
    
    /** 
     * @Title: selectByManagerId 
     */
    List<EntManager> selectByManagerId(Long managerId);
}
