package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.CrowdfundingQueryUrlMapper;
import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月5日 上午10:11:06
*/
@Service("crowdfundingQueryUrlService")
public class CrowdfundingQueryUrlServiceImpl implements CrowdfundingQueryUrlService{

    @Autowired
    CrowdfundingQueryUrlMapper crowdfundingQueryUrlMapper;
    @Override
    public boolean insert(CrowdfundingQueryUrl record) {
        if(record==null){
            return false;
        }
        return crowdfundingQueryUrlMapper.insert(record)==1;
    }

    @Override
    public boolean delete(Long id) {
        if(id==null){
            return false;
        }
        return crowdfundingQueryUrlMapper.delete(id)==1;
    }

    @Override
    public CrowdfundingQueryUrl getByCrowdfundingActivityDetailId(Long crowdfundingActivityDetailId) {
        if(crowdfundingActivityDetailId==null){
            return null;
        }
        return crowdfundingQueryUrlMapper.getByCrowdfundingActivityDetailId(crowdfundingActivityDetailId);
    }

    @Override
    public CrowdfundingQueryUrl getById(Long id) {
        if(id==null){
            return null;
        }
        return crowdfundingQueryUrlMapper.getById(id);
    }

    @Override
    public boolean updateByCrowdfundingActivityDetailId(CrowdfundingQueryUrl record) {
        if(record==null){
            return false;
        }
        return crowdfundingQueryUrlMapper.updateByCrowdfundingActivityDetailId(record)==1;
    }

}
