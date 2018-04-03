package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.DiscountRecord;

/**
 * 
 * @ClassName: DiscountRecordMapper 
 * @Description: 折扣记录DAO
 * @author: Rowe
 * @date: 2016年11月17日 下午8:08:56
 */
public interface DiscountRecordMapper {
    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: 查询
     * @param id
     * @return
     * @return: DiscountRecord
     */
    DiscountRecord selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: deleteByPrimaryKey 
     * @Description: 删除
     * @param id
     * @return
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * @Title: batchInsert 
     * @Description: 批量生成记录
     * @param records
     * @return
     * @return: int
     */
    int batchInsert(@Param("records") List<DiscountRecord> records);

    /**
     * 
     * @Title: insert 
     * @Description: 生成记录
     * @param record
     * @return
     * @return: int
     */
    int insert(DiscountRecord record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: 生成记录
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(DiscountRecord record);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: 根据主键更新
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(DiscountRecord record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: 根据主键更新
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(DiscountRecord record);
    
    /**
     * date 
     * 得到某天所有的折扣记录和当天之前最后一条折扣记录,所有记录按照时间倒序排列
     * 
     */
    List<DiscountRecord> getOneDayDiscount(@Param("startTime")Date startTime,
            @Param("endTime")Date endTime,@Param("userid")String userid,@Param("prdCode")String prdCode);

}