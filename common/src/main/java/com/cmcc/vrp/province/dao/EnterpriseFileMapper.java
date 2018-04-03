/**
 *
 */
package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EnterpriseFile;

/**
 * 企业开户时上传的文件信息
 *
 * @author JamieWu
 */
public interface EnterpriseFileMapper {

    /**
     * @param entId
     * @return
     */
    public EnterpriseFile selectByEntId(Long entId);

    /**
     * @param enterpriseFile
     * @return
     */
    public int insert(EnterpriseFile enterpriseFile);

    /**
     * @param enterpriseFile
     * @return
     */
    public int update(EnterpriseFile enterpriseFile);

    /**
     * @param entId
     * @return
     */
    public boolean deleteEnterpriseVerifyFiles(Long entId);
}
