package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcActiveRequestConfigMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.MdrcActiveRequestConfig;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.MdrcActiveRequestConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qinqinyan on 2016/12/1.
 */
@Service("mdrcActiveRequestConfigService")
public class MdrcActiveRequestConfigServiceImpl implements MdrcActiveRequestConfigService{

    @Autowired
    MdrcActiveRequestConfigMapper mapper;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    MdrcActiveDetailService mdrcActiveDetailService;

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)==1;
        }
        return false;
    }

    @Override
    public boolean insert(MdrcActiveRequestConfig record) {
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(MdrcActiveRequestConfig record) {
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public MdrcActiveRequestConfig selectByPrimaryKey(Long id) {
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(MdrcActiveRequestConfig record) {
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)==1;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(MdrcActiveRequestConfig record) {
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean active(MdrcActiveRequestConfig record) throws RuntimeException{

        if(record!=null){
            //插入请求和批次卡关系表
            if(!insertSelective(record)){
                return false;
            }

            //激活
            MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(record.getRequestId());
            if(!mdrcCardInfoService.activeRange(record.getConfigId(), record.getStartSerial(), record.getEndSerial(),
                mdrcActiveDetail.getEntId(), mdrcActiveDetail.getProductId())){
                throw new RuntimeException();
            }

            //更新激活状态
            MdrcActiveDetail updateRecord = new MdrcActiveDetail();
            updateRecord.setRequestId(mdrcActiveDetail.getRequestId());
            updateRecord.setActiveStatus(1);
            if(!mdrcActiveDetailService.updateByPrimaryKeySelective(updateRecord)){
                throw new RuntimeException();
            }
            return true;
        }
        return false;
    }

    @Override
    public List<MdrcActiveRequestConfig> selectByRequestId(Long requestId) {
        if(requestId!=null){
            return mapper.selectByRequestId(requestId);
        }
        return null;
    }
}
