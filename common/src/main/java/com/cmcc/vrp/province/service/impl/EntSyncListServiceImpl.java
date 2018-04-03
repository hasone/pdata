package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.EntSyncListMapper;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.StringUtils;
/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年11月30日 上午8:34:36
*/
@Service
public class EntSyncListServiceImpl implements EntSyncListService {
    private static final Logger logger = LoggerFactory.getLogger(EntSyncListServiceImpl.class);
	 
    @Autowired
	EntSyncListMapper entSyncListMapper;
	
    @Autowired
	EnterprisesService enterprisesService;
	
    @Override
	public boolean insert(EntSyncList entSyncList) {
    	if(entSyncList == null) {
    	    return false;
    	}
    	return entSyncListMapper.insert(entSyncList) > 0;
    }
	
    @Override
    public List<EntSyncList> getByEntProCode(String entProCode) {
    	if(StringUtils.isEmpty(entProCode)) {
    	    logger.error("无效的请求参数，entProCode={}", entProCode);
    	    return null;
    	}
		
    	return entSyncListMapper.getByEntProCode(entProCode);
    }

    @Override
	public List<EntSyncList> getByEntId(Long entId) {
		
    	if (entId == null
    			|| enterprisesService.selectById(entId) == null) {
    	    logger.error("无效的请求参数，entId={}", entId);
    	    return null;
    	}
		
    	return entSyncListMapper.getByEntId(entId);
    }

    @Override
	public EntSyncList getByEntIdAndEntProCode(Long entId, String entProCode) {
    	if (entId == null
    			|| StringUtils.isEmpty(entProCode)
    			|| enterprisesService.selectById(entId) == null) {
    	    logger.error("无效的请求参数，entId={}, entProCode={}", entId, entProCode);
    	    return null;
    	}
    	return entSyncListMapper.getByEntIdAndEntProCode(entId, entProCode);
    }

    @Override
	public int updateSelective(EntSyncList entSyncList) {
        return entSyncListMapper.updateSelective(entSyncList.getId(), entSyncList.getStatus(), entSyncList.getSyncInfo());
    }

    @Override
	public EntSyncList getById(Long id) {
    	return entSyncListMapper.getById(id);
    }

}
