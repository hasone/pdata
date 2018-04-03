package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @ClassName: SupplierProductMapMapper 
 * @Description: TODO
 */
public interface SupplierProductMapMapper {
    /**
     * 
     * @Title: delete 
     * @Description: TODO
     * @param id
     * @return
     * @return: int
     */
    int delete(Long id);

    /**
     * 
     * @Title: create 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int create(SupplierProductMap record);

    /**
     * 
     * @Title: get 
     * @Description: TODO
     * @param id
     * @return
     * @return: SupplierProductMap
     */
    SupplierProductMap get(Long id);

    /**
     * 
     * @Title: batchInsert 
     * @Description: TODO
     * @param maps
     * @return
     * @return: int
     */
    int batchInsert(@Param("supplierProductMaps") List<SupplierProductMap> maps);

    /**
     * 
     * @Title: deleteByPlftPid 
     * @Description: TODO
     * @param plftPid
     * @return
     * @return: int
     */
    int deleteByPlftPid(Long plftPid);

    /**
     * 
     * @Title: deleteByPlftPidAndSplPid 
     * @Description: TODO
     * @param plftPid
     * @param splPid
     * @return
     * @return: int
     */
    int deleteByPlftPidAndSplPid(@Param("plftPid") Long plftPid, @Param("splPid") Long splPid);

    /**
     * 
     * @Title: deleteBySplPid 
     * @Description: TODO
     * @param splPid
     * @return
     * @return: int
     */
    int deleteBySplPid(Long splPid);

    /**
     * 
     * @Title: deleteBySupplierId 
     * @Description: TODO
     * @param supplierId
     * @return
     * @return: int
     */
    int deleteBySupplierId(Long supplierId);

    /**
     * 更据平台产品id查询关联的所有供应商产品
     * @Title: getByPltfPid 
     * @Description: TODO
     * @param pltfPid
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> getByPltfPid(Long pltfPid);
    
    /**
     * 更据平台产品id查询上架状态为1的供应商产品
     * @Title: getOnshelfByPltfPid 
     * @Description: TODO
     * @param pltfPid
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> getOnshelfByPltfPid(Long pltfPid);

    /**
     * 
     * @Title: getBySplPid 
     * @Description: TODO
     * @param splPid
     * @return
     * @return: List<Product>
     */
    List<Product> getBySplPid(Long splPid);

    /**
     * 
     * @Title: getBySpplierId 
     * @Description: TODO
     * @param supplierId
     * @return
     * @return: List<Product>
     */
    List<Product> getBySpplierId(Long supplierId);
    
    /**
     * 
     * @Title: batchUpdate 
     * @Description: TODO
     * @param list
     * @return
     * @return: int
     */
    int batchUpdate(@Param("list") List<SupplierProductMap> list);
    
    /**
     * 
     * @Title: selectBySplPidWithOutDeleteFlag 
     * @Description: TODO
     * @param splPid
     * @return
     * @return: List<SupplierProductMap>
     */
    List<SupplierProductMap> selectBySplPidWithOutDeleteFlag(@Param("splPid")Long splPid);
    
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
     * 更新产品优先选择的供应商
     * @param prdId
     * @param supplierId
     * @return
     */
    int updatePriorSupplier(@Param("prdId") Long prdId, @Param("supplierId") Long supplierId);

    
    /**
     * 清除所有优先选择的供应商关系
     * @param prdId
     * @return
     */
    int clearPriorSupplier(@Param("prdId") Long prdId);

    SupplierProductMap getBypltfpidAndSplpid(@Param("prdId") Long prdId, @Param("splId") Long splId);

    /** 
    * @Title: clearPriorSupplierBySplId 
    * @Description:
    * @param splId
    * @return 
    * @return int
    * @throws 
    */
    int clearPriorSupplierBySplId(@Param("splId") Long splId);

    /** 
    * @Title: clearPriorSupplierBySupplierId 
    * @Description:
    * @param supplierId
    * @return 
    * @return int
    * @throws 
    */
    int clearPriorSupplierBySupplierId(@Param("supplierId") Long supplierId);
}