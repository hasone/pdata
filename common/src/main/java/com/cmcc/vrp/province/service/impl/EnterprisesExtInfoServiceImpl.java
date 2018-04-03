package com.cmcc.vrp.province.service.impl;

import java.util.List;

import com.cmcc.vrp.enums.CrowdFundingJoinType;
import com.cmcc.vrp.province.dao.EnterprisesExtInfoMapper;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 企业扩展信息服务实现
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
@Service
public class EnterprisesExtInfoServiceImpl implements EnterprisesExtInfoService {
    private static final Logger logger = LoggerFactory.getLogger(EnterprisesExtInfoServiceImpl.class);

    @Autowired
    EnterprisesExtInfoMapper enterprisesExtInfoMapper;

    /**
     * 根据企业ID获取企业扩展信息
     *
     * @param entId 企业ID
     * @return
     */
    @Override
    public EnterprisesExtInfo get(Long entId) {
        return entId == null ? null : enterprisesExtInfoMapper.get(entId);
    }

    /**
     * 删除企业的扩展信息
     *
     * @param entId 企业ID
     * @return
     */
    @Override
    public boolean delete(Long entId) {
        return entId != null && enterprisesExtInfoMapper.delete(entId) == 1;
    }

    /**
     * 插入新的企业扩展信息
     *
     * @param enterprisesExtInfo 新的企业扩展信息
     * @return
     */
    @Override
    public boolean insert(EnterprisesExtInfo enterprisesExtInfo) {
        return validate(enterprisesExtInfo) && enterprisesExtInfoMapper.insertSelective(enterprisesExtInfo) == 1;
    }

    /**
     * 更新企业扩展信息
     *
     * @param enterprisesExtInfo 更新企业扩展信息，根据企业ID
     * @return
     */
    @Override
    @Transactional
    public boolean update(EnterprisesExtInfo enterprisesExtInfo) {
        if(validate(enterprisesExtInfo)){
            if(!(enterprisesExtInfoMapper.updateByEntId(enterprisesExtInfo)>=0)){
                logger.info("更新企业附加信息失败：企业id:"+enterprisesExtInfo.getEnterId());
                return false;
            }

            if(enterprisesExtInfo.getJoinType()!=null
                && enterprisesExtInfo.getJoinType().toString().equals(CrowdFundingJoinType.Small_Enterprise.getCode().toString())){

                EnterprisesExtInfo originEnterExtInfo = get(enterprisesExtInfo.getEnterId());
                if(!StringUtils.isEmpty(originEnterExtInfo.getCallbackUrl())){
                    if(!(enterprisesExtInfoMapper.setCallbackUrlNullByEntId(enterprisesExtInfo.getEnterId())>=0)){
                        logger.info("更新企业附加信息url为空失败：企业id:"+enterprisesExtInfo.getEnterId());
                        throw new RuntimeException();
                    }
                }

            }
            return true;
        }
        return false;
    }

    /**
     * 校验企业扩展信息
     *
     * @param eei 企业扩展信息
     * @return
     */
    private boolean validate(EnterprisesExtInfo eei) {
        return eei != null
            && eei.getEnterId() != null
            && eei.getDeleteFlag() != null;
    }

    @Override
    public List<EnterprisesExtInfo> selectByEcCodeAndEcPrdCode(String ecCode, String ecPrdCode) {
        return enterprisesExtInfoMapper.selectByEcCodeAndEcPrdCode(ecCode, ecPrdCode);
    }
}
