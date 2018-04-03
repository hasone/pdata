package com.cmcc.vrp.wx.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.WxInviteQrcodeMapper;
import com.cmcc.vrp.wx.WxInviteQrcodeService;
import com.cmcc.vrp.wx.model.WxInviteQrcode;

@Service
public class WxInviteQrcodeServiceImpl implements WxInviteQrcodeService{

    @Autowired
    WxInviteQrcodeMapper mapper;
    
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean insert(WxInviteQrcode record) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean insertSelective(WxInviteQrcode record) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public WxInviteQrcode selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(WxInviteQrcode record) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(WxInviteQrcode record) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public WxInviteQrcode selectByAdminId(Long adminId) {
        if(adminId == null){
            return null;
        }
        return mapper.selectByAdminId(adminId);
    }
    
    @Override
    public int insertOrUpdateSelective(WxInviteQrcode record){
        return mapper.insertOrUpdateSelective(record);
    }

}
