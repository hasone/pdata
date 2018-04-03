package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;

import java.util.List;

/**
 * 供应商产品与平台产品的对应关系服务
 * <p>
 * Created by sunyiwei on 2016/6/13.
 */
public interface SupplierProductMapService {
    /**
     * 创建新的供应商产品与平台产品的对应关系
     *
     * @param supplierProductMap 对应关系对象
     * @return 关联关系对应成功返回true, 否则false
     */
    boolean create(SupplierProductMap supplierProductMap);

    /**
     * 创建新的关联关系
     *
     * @param pltfPid 平台产品ID
     * @param splPid  供应商产品ID
     * @return 创建成功返回true, 否则false
     */
    boolean create(Long pltfPid, Long splPid);

    /**
     * 更新对应关系，原来的对应关系会被删除
     *
     * @param pltfPid 平台产品ID
     * @param splPids 供应商产品ID列表
     * @return 更新成功返回true, 否则false
     */
    boolean updateByPltfPid(Long pltfPid, List<Long> splPids);

    /**
     * 更新对应关系，原来的对应关系会被删除
     *
     * @param splPid  供应商产品ID
     * @param pltPids 平台产品ID列表
     * @return 更新成功返回true, 否则false
     */
    boolean updateBySplPid(Long splPid, List<Long> pltPids);

    /**
     * 删除指定平台产品与所有供应商产品的关联关系
     *
     * @param plftPid 平台产品ID
     * @return 删除成功返回true, 否则false
     */
    boolean deleteByPlftPid(Long plftPid);

    /**
     * 删除指定的关联关系
     *
     * @param plftPid 平台产品ID
     * @param splPid  供应商产品ID
     * @return 删除成功返回true, 否则false
     */
    boolean delete(Long plftPid, Long splPid);

    /**
     * 删除指定供应商产品与所有平台产品的关联关系
     *
     * @param splPid 供应商产品ID
     * @return 删除成功返回true, 否则false
     */
    boolean deleteBySplPid(Long splPid);

    /**
     * 删除指定供应商相关的所有产品的关联关系
     *
     * @param supplierId 供应商ID
     * @return 删除成功返回true, 否则false
     */
    boolean deleteBySupplierId(Long supplierId);

    /**
     * 根据平台产品ID查询关联的供应商产品信息
     *
     * @param pltfPid 平台产品ID
     * @return 关联的供应商产品信息列表
     */
    List<SupplierProduct> getByPltfPid(Long pltfPid);
    
    /**
     * 根据平台产品ID查询关联的上架供应商产品信息
     *
     * @param pltfPid 平台产品ID
     * @return 关联的供应商产品信息列表
     */
    List<SupplierProduct> getOnshelfByPltfPid(Long pltfPid);

    /**
     * 根据供应商产品ID查询关联的平台产品信息
     *
     * @param splPid 供应商产品ID
     * @return 关联的所有平台产品信息列表
     */
    List<Product> getBySplPid(Long splPid);

    /**
     * 
     * @Title: batchInsertMap 
     * @Description: TODO
     * @param records
     * @return
     * @return: boolean
     */
    boolean batchInsertMap(List<SupplierProductMap> records);
    
    /**
     * 
     * @Title: batchUpdate 
     * @Description: 批量更新
     * @param list
     * @return
     * @return: boolean
     */
    boolean batchUpdate(List<SupplierProductMap> list);
    
    /**
     * 
     * @Title: selectBySplPid 
     * @Description: 更加供应商产品ID查询
     * @param splPid
     * @return
     * @return: List<SupplierProductMap>
     */
    List<SupplierProductMap> selectBySplPidWithOutDeleteFlag(Long splPid);
 
    /**
     * 
     * @Title: getBySplPidWithoutDel 
     * @Description: TODO
     * @param splPid
     * @return
     * @return: List<Product>
     */
    List<Product> getBySplPidWithoutDel(Long splPid);

    /**
     * 平台产品优先选择供应商supplierId
     * @param prdId
     * @param supplierId
     */
    boolean updatePriorSupplier(Long prdId, Long supplierId);
    /**
     * 清除优先选择供应商关联关系
     * @param prdId
     * @return
     */
    boolean clearPriorSupplier(Long prdId);
    
    SupplierProductMap getBypltfpidAndSplpid(Long prdId, Long splId);

    /** 
    * @Title: clearPriorSupplierBySplId 
    * @Description:根据供应商产品id请除渠道优先选择关系
    * @param id
    * @return 
    * @return boolean
    * @throws 
    */
    int clearPriorSupplierBySplId(Long splId);

    /** 
    * @Title: clearPriorSupplierBySupplierId 
    * @Description:根据供应商id清除渠道优先选择关系
    * @param supplierId
    * @return 
    * @return int
    * @throws 
    */
    int clearPriorSupplierBySupplierId(Long supplierId);
}
