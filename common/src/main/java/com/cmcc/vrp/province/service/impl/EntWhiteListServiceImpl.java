/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.EntWhiteListMapper;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.service.EntWhiteListService;

/**
 * <p>Title:EntWhiteListServiceImpl </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月17日
 */
@Service("EntWhiteListService")
public class EntWhiteListServiceImpl implements EntWhiteListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntWhiteListServiceImpl.class);
    
    @Autowired
    EntWhiteListMapper entWhiteListMapper;
    
    /**
     * @Title: isIpInEntWhiteList
     * @Description: 判断ip是否在白名单中
     */
    @Override
    public boolean isIpInEntWhiteList(String ip, Long entId) {
    	List<EntWhiteList> entWhiteList = entWhiteListMapper.selectByEntId(entId);
    	
    	if(entWhiteList != null && entWhiteList.size()>0){
            for(EntWhiteList ew:entWhiteList) {
                if (ip.equals(ew.getIpAddress())) {
                    return true;
                }
            }
    	}
    	return false;
    }

    /* 根据企业ID筛选出ip白名单
     * @see com.cmcc.vrp.province.service.EntWhiteListService#selectByEntId(java.lang.Long)
     */
    @Override
    public List<EntWhiteList> selectByEntId(Long entId) {
        return entWhiteListMapper.selectByEntId(entId);

    }
    
    /**
     * @Title: deleteByEntId
     * @Description: 根据entId逻辑删除ip白名单，表中不一定有要删除的数据
     */
    @Override
    public boolean deleteByEntId(Long entId) {
	return entWhiteListMapper.deleteByEntId(entId) >= 0;
    }
    
    @Override
    public boolean batchInsert (List<EntWhiteList> entWhiteLists) {
	return entWhiteListMapper.batchInsert(entWhiteLists) == entWhiteLists.size();
    }

    /**
     * 
     * @Title: insertIps 
     * @param ips
     * @param entId
     * @return
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    @Override
    public boolean insertIps(List<String> ips, Long entId) {
        return entWhiteListMapper.insertIps(ips, entId) == ips.size();
    }
}
