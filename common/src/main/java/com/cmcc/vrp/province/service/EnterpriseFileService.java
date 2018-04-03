/**
 *
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.EnterpriseFile;

/**
 * @author JamieWu
 */
public interface EnterpriseFileService {

    int insert(EnterpriseFile enterpriseFile);

    EnterpriseFile selectByEntId(Long entId);

    boolean update(EnterpriseFile enterpriseFile);

    boolean deleteEnterpriseVerifyFiles(Long entId);

}
