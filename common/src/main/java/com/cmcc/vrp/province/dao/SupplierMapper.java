package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 供应商dao
 */
public interface SupplierMapper {
    /**
     * 根据主键ID删除供应商信息，逻辑删除
     *
     * @param id 主键ID
     * @return 删除的供应商个数
     */
    int delete(Long id);

    /**
     * 创建新的供应商记录
     *
     * @param record 供应商记录
     * @return 新增成功的供应商个数
     */
    int create(Supplier record);

    /**
     * 根据主键ID获取供应商信息
     *
     * @param id 主键ID
     * @return
     */
    Supplier get(Long id);

    /**
     * 根据主键ID更新供应商信息
     *
     * @param id      主键ID
     * @param newName 新的供应商名称
     * @param newIsp  新的供应商运营商信息
     * @return 更新成功的供应商记录数
     */
    int update(@Param("id") Long id, @Param("newName") String newName, @Param("newIsp") String newIsp);

    /**
     * 根据sync标识获取供应商列表
     *
     * @param sync sync标识
     * @return 符合条件的供应商列表
     */
    List<Supplier> getSupplierBySync(@Param("sync") int sync);
    
    /**
     * @param map
     * @return 
     */
    List<Supplier> selectByMap(Map map);

    /**
     * 根据查询条件获取供应商列表
     *
     * @param map 查询条件
     * @return 符合条件的供应商列表
     */
    List<Supplier> queryPaginationSupplier(Map<String, Object> map);

    /**
     * 根据查询条件获取满足条件的供应商个数
     *
     * @param map 查询条件
     * @return 符合条件的供应商个数
     */
    int queryPaginationSupplierCount(Map<String, Object> map);

    /**
     * 恢复供应商
     *
     * @param id 恢复供应商
     * @return 恢复成功的个数
     */
    int recoverSupplier(Long id);

    /**
     * 根据供应商fingerprint获取相应的供应商信息
     *
     * @param fingerprint 供应商的指纹信息
     * @return 符合条件的供应商信息
     */
    Supplier getByFingerPrint(@Param("fingerprint") String fingerprint);

    /**
     * 获取所有的供应商信息
     *
     * @return 所有的供应商列表
     */
    List<Supplier> getAllSuppliers();
    
    /**
     * 更新供应商信息
     * @return
     * */
    int updateByPrimaryKeySelective(Supplier record);

    /**
     * 根据名称查询
     * @return
     * */
    List<Supplier> selectByName(@Param("name") String name);

    /**
     * 根据产品id获取关联的boss产品的供应商
     * @param prdId
     * @return
     */
    List<Supplier> getSupplierByPrdId(@Param("prdId") Long prdId);

    /**
     * 根据产品id获取优先选择的供应商
     * @param productId
     * @return
     */
    List<Supplier> getPriorSupplierByPrdid(@Param("prdId") Long productId);
}