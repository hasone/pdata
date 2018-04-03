package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.ShOrderListMapper;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * @author lgk8023
 *
 */
@Service
public class ShOrderListServiceImpl implements ShOrderListService{

    @Autowired
    ShOrderListMapper shOrderListMapper;
    @Override
    public boolean insert(ShOrderList shOrderList) {
        if (shOrderList == null) {
            return false;
        }
        if (shOrderListMapper.getByMainBillId(shOrderList.getMainBillId()) != null) {
            return false;
        }
        return shOrderListMapper.insert(shOrderList) == 1;
    }
    @Override
    public List<ShOrderList> getByEnterId(Long enterId) {
        if (enterId == null) {
            return null;
        }
        return shOrderListMapper.getByEnterId(enterId);
    }
    @Override
    public ShOrderList getByMainBillId(String mainBillId) {
        if (StringUtils.isEmpty(mainBillId)) {
            return null;
        }

        return shOrderListMapper.getByMainBillId(mainBillId);
    }
    @Override
    public ShOrderList getById(Long id) {
        return shOrderListMapper.getById(id);
    }
    @Override
    public boolean updateAlertSelective(ShOrderList shOrderList) {
        return shOrderListMapper.updateAlertSelective(shOrderList) > 0;
    }
    @Override
    public int showForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return shOrderListMapper.showForPageResultCount(queryObject.toMap());
    }
    @Override
    public List<ShOrderList> showForPageResultList(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return shOrderListMapper.showForPageResultList(queryObject.toMap());
    }
    @Override
    public boolean updateCount(ShOrderList shOrderList) {
        return shOrderListMapper.updateCount(shOrderList) > 0;
    }
    @Override
    public List<ShOrderList> getOrderListByMap(Map map) {
        return shOrderListMapper.getOrderListByMap(map);
    }

}
