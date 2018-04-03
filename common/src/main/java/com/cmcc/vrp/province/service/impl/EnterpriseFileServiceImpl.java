/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EnterpriseFileMapper;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author JamieWu
 */
@Service("enterprisesFileService")
public class EnterpriseFileServiceImpl implements EnterpriseFileService {

    @Autowired
    private EnterpriseFileMapper enterpriseFileMapper;

    @Override
    public int insert(EnterpriseFile enterpriseFile) {
        enterpriseFile.setUpdateTime(new Date());
        enterpriseFile.setCreateTime(new Date());
        return enterpriseFileMapper.insert(enterpriseFile);
    }

    @Override
    public EnterpriseFile selectByEntId(Long entId) {
        return enterpriseFileMapper.selectByEntId(entId);
    }

    @Override
    public boolean update(EnterpriseFile enterpriseFile) {
        if (enterpriseFileMapper.update(enterpriseFile) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEnterpriseVerifyFiles(Long entId) {
        return enterpriseFileMapper.deleteEnterpriseVerifyFiles(entId);
    }

}
