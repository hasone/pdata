package com.cmcc.vrp.province.service.impl;

import java.util.List;

import com.cmcc.vrp.province.dao.MdrcActiveDetailMapper;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qinqinyan on 2016/11/18.
 */
@Service("mdrcActiveDetailService")
public class MdrcActiveDetailServiceImpl implements MdrcActiveDetailService {

    @Autowired
    MdrcActiveDetailMapper mdrcActiveDetailMapper;

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if (id == null) {
            return false;
        }
        return mdrcActiveDetailMapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean insert(MdrcActiveDetail record) {
        if (record == null) {
            return false;
        }
        return mdrcActiveDetailMapper.insert(record) == 1;
    }

    @Override
    public boolean insertSelective(MdrcActiveDetail record) {
        if (record == null) {
            return false;
        }
        return mdrcActiveDetailMapper.insertSelective(record) == 1;
    }

    @Override
    public MdrcActiveDetail selectByPrimaryKey(Long id) {
        if (id != null) {
            return mdrcActiveDetailMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(MdrcActiveDetail record) {
        if (record != null) {
            return mdrcActiveDetailMapper.updateByPrimaryKeySelective(record) == 1;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(MdrcActiveDetail record) {
        if (record == null) {
            return false;
        }
        return mdrcActiveDetailMapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public MdrcActiveDetail selectByRequestId(Long requestId) {
        if (requestId != null) {
            return mdrcActiveDetailMapper.selectByRequestId(requestId);
        }
        return null;
    }

    @Override
    public boolean updateByRequestIdSelective(MdrcActiveDetail record) {
        if (record != null) {
            return mdrcActiveDetailMapper.updateByRequestIdSelective(record) == 1;
        }
        return false;
    }

    @Override
    public List<MdrcActiveDetail> selectByconfigIdAndStatus(Long configId, Integer status) {
        return mdrcActiveDetailMapper.selectByconfigIdAndStatus(configId, status);
    }
}
