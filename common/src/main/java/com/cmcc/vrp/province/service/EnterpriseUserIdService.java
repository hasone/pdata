/**
 *
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Enterprise;

/**
 * @author JamieWu
 *         企业集团产品唯一标识
 */
public interface EnterpriseUserIdService {
    Boolean saveUserId(Enterprise enterprise);

    String getUserIdByEnterpriseCode(String code);
}
