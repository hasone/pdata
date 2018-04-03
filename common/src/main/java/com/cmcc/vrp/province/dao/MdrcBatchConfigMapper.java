package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcBatchConfig;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @Title:MdrcBatchConfigMapper
 * @Description:
 * */
public interface MdrcBatchConfigMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(MdrcBatchConfig record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    Long insertSelective(MdrcBatchConfig record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    MdrcBatchConfig selectByPrimaryKey(Long id);

    /**
     * @Title:
     * @Description:
     * */
    MdrcBatchConfig select(Long id);

    /**
     * @Title:select
     * @Description:
     * */
    List<MdrcBatchConfig> selectAllConfig();

    /**
     * @Title:selectModuleByPrimaryKey
     * @Description:
     * */
    MdrcBatchConfig selectModuleByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(MdrcBatchConfig record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(MdrcBatchConfig record);

    /**
     * 查询符合条件的记录，制造商、客户经理、模板
     * @Title:queryPagination
     * @Description:
     * */
    List<MdrcBatchConfig> queryPagination(Map<String, Object> map);

    /**
     * 分页数量
     * @Title:queryCounts
     * @Description:
     * */
    int queryCounts(Map<String, Object> map);

    /**
     * 根据年份和省份编码查询
     * @Title:selectByYearAndProvinceCode
     * @Description:
     * */
    List<MdrcBatchConfig> selectByYearAndProvinceCode(Map<String, Object> map);

    /**
     * 根据创建者ID和状态查询
     * @Title:selectByCreatorIdAndStatus
     * @Description:
     * */
    List<MdrcBatchConfig> selectByCreatorIdAndStatus(Map<String, Object> map);

    /**
     * @Title:selectByCardmaker
     * @Description:
     * */
    List<MdrcBatchConfig> selectByCardmaker(Map<String, Object> map);

    /**
     * @Title:countByCardmaker
     * @Description:
     * */
    int countByCardmaker(Map<String, Object> map);

    /**
     * 唯一性校验
     * @Title:checkUnique
     * @Description:
     * */
    int checkUnique(MdrcBatchConfig m);
    
    
    /**
    * @Title: selectAllConfigByPagination
    * @Description: 分页查找所有卡批次
    */ 
    List<MdrcBatchConfig> selectAllConfigByPagination(Map<String, Object> map);
    
    /**
    * @Title: countAllConfigByPagination
    * @Description: 分页查找所有卡批次
    */ 
    int countAllConfigByPagination(Map<String, Object> map);

    /**
     * @Title:
     * @Description:
     * */
    List<MdrcBatchConfig> selectByMap(Map map);
	
    /**
     * @param serialNum
     * @param year 
     * @return
     */
    MdrcBatchConfig selectBySerialNum(@Param("serialNum") String serialNum, @Param("year") String year);
    
    /**
     * 
     * @Title: selectByEntIdAndStatus 
     * @Description: TODO
     * @param entId
     * @param status
     * @return
     * @return: List<MdrcBatchConfig>
     */
    List<MdrcBatchConfig> selectByEntIdAndStatus(@Param("entId") Long entId, @Param("status") Integer status);
    
    /**
     * 
     * @Title: getConfigDetailsByIdAndStatus 
     * @Description: TODO
     * @param configId
     * @param year
     * @param status
     * @return
     * @return: MdrcBatchConfig
     */
    MdrcBatchConfig getConfigDetailsByIdAndStatus(@Param("configId") Long configId,@Param("year") String year, @Param("status") Integer status);
    
    /**
     * 
     * @Title: getConfigDetailsById 
     * @Description: TODO
     * @param configId
     * @param year
     * @return
     * @return: MdrcBatchConfig
     */
    MdrcBatchConfig getConfigDetailsById(@Param("configId") Long configId, @Param("year") String year);
}