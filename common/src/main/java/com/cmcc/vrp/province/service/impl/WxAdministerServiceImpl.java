package com.cmcc.vrp.province.service.impl;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.dao.TmpaccountMapper;
import com.cmcc.vrp.province.dao.WxAdministerMapper;
import com.cmcc.vrp.province.model.ChangeMobileRecord;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.ChangeMobileRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.model.Tmpaccount;

/**
 *  微信用户服务类
 */
@Service("wxAdministerService")
public class WxAdministerServiceImpl implements WxAdministerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxAdministerServiceImpl.class);

    @Autowired
    WxAdministerMapper wxAdministerMapper;
    @Autowired
    ChangeMobileRecordService changeMobileRecordService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    TmpaccountMapper tmpaccountMapper;
    
    @Override
    public WxAdminister selectWxAdministerById(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        WxAdminister admin = wxAdministerMapper.selectByPrimaryKey(id);
        return admin;
    }
    
    @Override
    @Transactional
    public boolean updateWxAdminster(String oldMobile, String newMobile) {
        // TODO Auto-generated method stub
        LOGGER.info("原手机号:oldMobile = {}, 新手机号:newMobile = {}", oldMobile, newMobile);
        if(StringUtils.isEmpty(oldMobile) || StringUtils.isEmpty(newMobile)){
            LOGGER.info("参数异常");
            return false;
        }
        
        WxAdminister oldWxAdminister = selectByMobilePhone(oldMobile);
        if(oldWxAdminister!=null){
            WxAdminister newWxAdminister = new WxAdminister();
            newWxAdminister.setId(oldWxAdminister.getId());
            newWxAdminister.setMobilePhone(newMobile);
           
            if(!updateSelective(newWxAdminister)){
                LOGGER.info("更新新手机号失败, adminId = {}, oldMobile = {}, newMobile = {}",
                        oldWxAdminister.getId(), oldMobile, newMobile);
                return false;
            }
            
            if(!changeMobileRecordService.insertSelective(createChangeMobileRecord(oldMobile, newMobile, oldWxAdminister.getId()))){
                LOGGER.info("插入更新新手机号记录失败, adminId = {}, oldMobile = {}, newMobile = {}",
                        oldWxAdminister.getId(), oldMobile, newMobile);
                throw new RuntimeException();
            }
            return true;
        }
        return false;
    }
    
    private ChangeMobileRecord createChangeMobileRecord(String oldMobile, String newMobile, Long adminId){
        ChangeMobileRecord record = new ChangeMobileRecord();
        record.setAdminId(adminId);
        record.setOldMobile(oldMobile);
        record.setNewMobile(newMobile);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }
    
    @Override
    public WxAdminister selectByMobilePhone(String phone) {
        // TODO Auto-generated method stub
        WxAdminister administer = null;
        if (StringUtils.isValidMobile(phone)) {
            administer = wxAdministerMapper.selectByMobilePhone(phone);
        }
        return administer;
    }
    
    /**
     * @param administer
     * @return boolean
     * @throws
     * @Title:updateSelective
     * @Description:数据库的更新
     */
    @Override
    public boolean updateSelective(WxAdminister wxAdminister) {
        if (wxAdminister == null || wxAdminister.getId() == null) {
            return false;
        }
        wxAdminister.setUpdateTime(new Date());
        return wxAdministerMapper.updateByPrimaryKeySelective(wxAdminister) > 0;
    }
    
    /**
     * 微信的用户创建
     */
    @Override
    @Transactional
    public boolean insertForWx(String mobile, String openid) {
        WxAdminister admin = new WxAdminister();
        admin.setMobilePhone(mobile);
        admin.setCreatorId(0L);
        if (!insertSelective(admin)) {
            throw new RuntimeException("插入用户表失败！");
        }
        
        List<IndividualAccount> accounts = new ArrayList<IndividualAccount>();
        List<IndividualProduct> products = individualProductService.selectByDefaultValue(1);
        if (products != null && products.size() > 0) {
            for (IndividualProduct product : products) {
                IndividualAccount account = new IndividualAccount();
                account.setAdminId(admin.getId());
                account.setOwnerId(admin.getId());
                account.setCount(new BigDecimal(0));
                account.setDeleteFlag(0);
                account.setIndividualProductId(product.getId());
                account.setType(IndividualAccountType.INDIVIDUAL_BOSS.getValue());//创建的账户都是个人的
                account.setVersion(0);
                accounts.add(account);
            }

            if (!individualAccountService.batchInsert(accounts)) {
                LOGGER.error("插入individual_account失败！");
                throw new RuntimeException("插入individual_account失败！");
            }
        }
        List<Tmpaccount> tmpAccounts = tmpaccountMapper.selectByOpenid(openid);
        if(tmpAccounts!=null && tmpAccounts.size()==1 && tmpAccounts.get(0).getCount().intValue()>0){
            IndividualProduct product = individualProductService.getIndivialPointProduct();
            IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());
            if(account!=null){
                if(!individualAccountService.changeAccount(account, tmpAccounts.get(0).getCount(), SerialNumGenerator.buildSerialNum(), 
                        (int)AccountRecordType.INCOME.getValue(), "历史流量币", -1, 0)){
                    LOGGER.error("插入历史流量币失败！");
                    throw new RuntimeException("插入历史流量币失败！");
                }
            }
        }
        return true;
    }
    
    /**
     * @param administer
     * @return boolean
     * @throws
     * @Title:insertSelective
     * @Description:数据库的插入操作
     */
    public boolean insertSelective(WxAdminister wxAdminister) {
        if (wxAdminister == null) {
            return false;
        }
        wxAdminister.setCreateTime(new Date());
        wxAdminister.setUpdateTime(new Date());
        wxAdminister.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return wxAdministerMapper.insertSelective(wxAdminister) > 0;
    }
    
    
}
