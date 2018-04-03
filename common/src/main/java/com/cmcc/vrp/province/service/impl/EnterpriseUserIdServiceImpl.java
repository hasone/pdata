package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.sichuan.model.SCOpenRequest;
import com.cmcc.vrp.boss.sichuan.model.SCOpenResponse;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.boss.sichuan.service.SCCancelService;
import com.cmcc.vrp.boss.sichuan.service.SCDelMemberService;
import com.cmcc.vrp.boss.sichuan.service.SCOpenService;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.dao.EnterpriseUserIdMapper;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseUserId;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.ManagerService;

@Service("enterpriseUserIdService")
public class EnterpriseUserIdServiceImpl implements EnterpriseUserIdService {

    @Autowired
    SCOpenService scOpenService;

    @Autowired
    EnterpriseUserIdMapper enterpriseUserIdMapper;

    @Autowired
    DiscountMapper discountMapper;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    SCAddMemberService scAddMemberService;

    @Autowired
    SCDelMemberService scDelMemberService;

    @Autowired
    SCCancelService scCancelService;
    
    @Autowired
    EntManagerService entManagerService;
    
    @Autowired
    ManagerService managerService;

    private Logger logger = LoggerFactory.getLogger(EnterpriseUserIdService.class);

    @Override
    public Boolean saveUserId(Enterprise enterprise) {
        SCOpenRequest request = new SCOpenRequest();

        Discount discount = discountMapper.selectByPrimaryKey(enterprise.getDiscount());
        Integer d = new Double(discount.getDiscount() * 100).intValue();
        request.setDiscntCode(d.toString());

        request.setUnitId(enterprise.getCode());
        String regionId = "";
        
        //企业管理员职位
        Manager manager = entManagerService.getManagerForEnter(enterprise.getId());
        //客户经理职位
        Manager fatherManager = managerService.selectByPrimaryKey(manager.getParentId());
        //根据客户经理职位找到地区
        List<District> districts = districtMapper.selectByName(fatherManager.getName());
        if(districts != null && districts.size() == 1){
            District district = districts.get(0);
            regionId = district.getCode();
        }
        
        /*District district = districtMapper.selectById(enterprise.getDistrictId());
        if (district != null) {
            if (!StringUtils.isEmpty(district.getCode())) {
                regionId = district.getCode();
            } else {
                District ds = districtMapper.selectById(district.getParentId());
                regionId = ds.getCode();
            }
        }*/
        
        request.setRegionId(regionId);

        if (getUserIdByEnterpriseCode(enterprise.getCode()) != null) {
            logger.info("集团编码:" + enterprise.getCode() + "的企业已存在产品唯一标识，不需重复开户");
            return false;
        }

        SCOpenResponse response = scOpenService.sendOpenRequest(request);

        if (response != null) {
            if (response.getResCode().equals("0000000")) {
                //插入数据库
                EnterpriseUserId enterpriseUserId = new EnterpriseUserId();
                enterpriseUserId.setCode(enterprise.getCode());
                enterpriseUserId.setUserId(response.getOutData().getUSER_ID());
                enterpriseUserId.setCreateTime(new Date());
                if (enterpriseUserIdMapper.insert(enterpriseUserId) > 0) {
                    logger.info("获取集团产品唯一标识成功，并插入数据库，集团编码:" + enterprise.getCode() + "，产品唯一标识：" + response.getOutData().getUSER_ID());
                    return true;
                }
            } else {
                logger.info("获取集团产品唯一标识失败，失败原因：" + response.getResMsg());
                return false;
            }
        }
        return false;
    }

    @Override
    public String getUserIdByEnterpriseCode(String code) {
        return enterpriseUserIdMapper.getUserIdByEnterpriseCode(code);
    }

}
