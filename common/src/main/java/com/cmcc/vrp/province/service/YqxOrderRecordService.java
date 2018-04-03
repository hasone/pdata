package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.YqxOrderRecord;

/**
 * YqxOrderRecordService.java
 * @author wujiamin
 * @date 2017年5月4日
 */
public interface YqxOrderRecordService {
    
    /** 
     * @Title: insert 
     */
    public boolean insert(YqxOrderRecord yqxOrderRecord);
    
    /** 
     * @Title: selectByMap 
     */
    public List<YqxOrderRecord> selectByMap(Map map);

    /** 
     * @Title: selectBySerialNum 
     */
    YqxOrderRecord selectBySerialNum(String serialNum);

    /** 
     * @Title: updateByPrimaryKey 
     */
    Boolean updateByPrimaryKey(YqxOrderRecord record);

    /** 
     * 订购数量是否超过限额，true超过，false没超过
     * @Title: ifOverNum 
     */
    boolean ifOverNum(String mobile);

    /** 
     * @Title: create 
     */
    public boolean create(YqxOrderRecord yqxOrderRecord);

    /** 
     * @Title: countByMap 
     */
    Integer countByMap(Map<String, Object> map);

    /** 
     * 是否是云企信的限制购买时间段
     * @Title: duringAccountCheckDate 
     */
    boolean duringAccountCheckDate();
    
    /** 
     * 重庆云企信前端展示的虚拟购买数量
     * @Title: getCqNum 
     */
    public Long getVirtualCqNum();

}
