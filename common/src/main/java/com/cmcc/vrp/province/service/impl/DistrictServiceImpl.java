package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leelyn on 2016/6/1.
 */
@Service("districtService")
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public List<District> queryByParentId(Long parentId) {
        return parentId == null ? null : districtMapper.selectDisByParentId(parentId);
    }

    @Override
    public District queryBySonId(Long sonId) {
        return sonId == null ? null : districtMapper.selectDisBySonId(sonId);
    }

    @Override
    public District queryById(Long id) {
        return id == null ? null : districtMapper.selectById(id);
    }

    @Override
    public String selectFullDistrictNameById(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return districtMapper.selectFullDistrictNameById(id);
    }
    
    @Override
    public List<Long> selectNodeById(Long id) {
        // TODO Auto-generated method stub
        if (id != null) {
            return districtMapper.selectNodeById(id);
        }
        return null;
    }

    @Override
    public List<District> selectChildByName(String name) {
        List<District> districts = new ArrayList<District>();
        if (!StringUtils.isEmpty(name)) {
            districts = districtMapper.selectChildByName(name);
        }
        return districts;
    }

    @Override
    public List<District> selectByName(String name) {
        return districtMapper.selectByName(name);
    }

    @Override
    public List<District> selectByParentId(Long parentId) {
        return districtMapper.selectByParentId(parentId);
    }
}
