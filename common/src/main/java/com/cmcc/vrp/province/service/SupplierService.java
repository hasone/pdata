package com.cmcc.vrp.province.service;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;
import java.util.Map;

/**
 * 供应商服务
 * <p>
 * Created by sunyiwei on 2016/6/13.
 */
public interface SupplierService {
    /**
     * 新建供应商
     *
     * @param supplier 供应商对象
     * @return 新增成功返回true, 否则false
     */
    boolean create(Supplier supplier);

    /**
     * 逻辑删除供应商， 会自动删除该供应商提供的产品
     *
     * @param supplierId 供应商ID
     * @return 删除成功返回true, 否则false
     */
    boolean delete(Long supplierId);

    /**
     * 更新供应商名称
     *
     * @param supplierId 供应商ID
     * @param newName    新的供应商名称，可以为空
     * @param newType    新的供应商类型，可以为空
     * @return 更新成功返回true, 否则false
     */
    boolean update(Long supplierId, String newName, IspType newType);

    /**
     * 根据供应商ID查询供应商信息
     *
     * @param supplierId 供应商ID
     * @return 供应商信息
     */
    Supplier get(Long supplierId);

    /**
     * 根据标识找出供应商：可手动增加产品的供应商及自动BOSS同步产品的供应商
     *
     * @param sync
     * @return
     * @date 2016年6月14日
     * @author wujiamin
     */
    List<Supplier> getSupplierBySync(int sync);
    
    
    /**
     * 根据条件获取供应商
     * @param map
     * @author qinqinyan
     * */
    List<Supplier> selectByMap(Map map);


    /**
     * 根据条件查询供应商信息
     *
     * @param queryObject 查询条件
     * @return 供应商信息
     */
    List<Supplier> queryPaginationSupplier(QueryObject queryObject);

    /**
     * 根据条件查询供应商数量
     *
     * @param queryObject 查询条件
     * @return 供应商数量
     */
    int queryPaginationSupplierCount(QueryObject queryObject);

    /**
     * 根据id暂停供应商
     *
     * @param id 供应商id
     * @return 暂停成功返回true, 否则false
     */
    boolean suspendSupplier(Long id);

    /**
     * 根据id开通供应商
     *
     * @param id 供应商id
     * @return 开通成功返回true, 否则false
     */
    boolean recoverSupplier(Long id);

    /**
     * @param fingerPrint
     * @return
     * @Title: getByFingerPrint
     * @Description: 根据指纹查询
     * @return: Supplier
     */
    Supplier getByFingerPrint(String fingerPrint);

    /**
     * @return
     * @Title: getAllSuppliers
     * @Description: 获取所有未删除的供应商
     * @return: List<Supplier>
     */
    List<Supplier> getAllSuppliers();
    
    /**
     * 更新供应商信息
     * @return supplier
     * @author qinqinyan
     * */
    Boolean updateByPrimaryKeySelective(Supplier supplier);

    /**
     * 根据名称查找
     * @param name
     * @author qinqinyan
     * */
    List<Supplier> selectByName(String name);
    
    /**
     * 设置供应商全量限额
     * @author qinqinyan
     * */
    boolean setSupplierLimit(Supplier supplier, Long adminId);

    /**
     * 根据产品id获取关联的boss产品的供应商
     * @param prdId
     * @return
     */
    List<Supplier> getSupplierByPrdId(Long prdId);

    /**
     * 根据产品id获取优先选择的供应商
     * @param productId
     * @return
     */
    Supplier getPriorSupplierByPrdid(Long productId);
}
