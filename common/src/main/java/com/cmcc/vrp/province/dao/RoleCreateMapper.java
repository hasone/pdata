/**
 *
 */
package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.RoleCreate;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:RoleCreateMapper </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月15日
 */
public interface RoleCreateMapper {

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(RoleCreate roleCreate);

    /**
     * @Title:selectRoleIdsCreateByRoleId
     * @Description:
     * */
    List<Long> selectRoleIdsCreateByRoleId(Long roleId);

    /**
     * @Title:select
     * @Description:
     * */
    RoleCreate select(Map<String, Object> param);

    /**
     * @Title:deleteRoleCreateByRoleId
     * @Description:
     * */
    int deleteRoleCreateByRoleId(RoleCreate roleCreate);

    /**
     * @Title:getCreateRolesByRoleId
     * @Description:
     * */
    List<Role> getCreateRolesByRoleId(Long roleId);

}
