package com.cmcc.vrp.province.mdrc.service;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:38:34
*/
public interface MdrcCardmakerService {

    /**
     * @return
     */
    List<MdrcCardmaker> selectAllCardmaker();

    /**
     * 根据id获取未删除的记录
     * @param id
     * edit by qinqinyan
     */
    MdrcCardmaker selectByPrimaryKey(Long id);
    
    /**
     * 根据id获取记录，用于显示
     * @param id
     * @author qinqinyan
     * */
    MdrcCardmaker selectByPrimaryKeyForshow(Long id);

    /**
     * 新建制卡商信息
     * <p>
     *
     * @param mdrcCardmaker
     * @return
     */
    boolean insert(MdrcCardmaker mdrcCardmaker);

    /**
     * 查询符合条件的记录数
     * <p>
     *
     * @param queryObject
     * @return
     */
    int count(QueryObject queryObject);

    /**
     * 查询符合条件的记录
     * <p>
     *
     * @param queryObject
     * @return
     */
    List<MdrcCardmaker> list(QueryObject queryObject);

    /**
     * 更新制卡商信息
     * <p>
     *
     * @param mdrcCardmaker
     * @return
     */
    boolean update(MdrcCardmaker mdrcCardmaker);

    /**
     * 获取新的序列号
     * <p>
     *
     * @return
     */
    int getNewSerialNumber();

    /**
     * 按名称插叙已存在记录数
     * <p>
     *
     * @param name
     * @return
     */
    int countByName(String name);

    /**
     * 根据记录标识查询
     * <p>
     *
     * @param map
     * @return
     */
    MdrcCardmaker selectWithKeys(Long id);

    /**
     * 根据记录标识和创建者标识查询
     * <p>
     *
     * @param id
     * @param creatorId
     * @return
     */
    MdrcCardmaker selectWithKeys(Long id, Long creatorId);

    /**
     * @param authCode：制卡专员权限编码
     * @return
     * @Title: selectUnboundCardmaker
     * @Description: 根据权限编码查询未绑定制卡商的制卡专员
     * @return: List<Administer>
     */
    List<Administer> selectUnboundCardmaker(String authCode);

    /**
     * 删除制卡商(物理删除)
     * <p>
     *
     * @param id
     * @return
     */
    boolean deleteById(Long id);
    
    /**
     * 逻辑删除
     * @param id
     * @author qinqinyan
     * */
    boolean deleteCardmaker(Long id);


    /**
     * 根据制卡专员ID查询
     */
    MdrcCardmaker selectByOperatorId(Long operatorId);

    /**
     * 唯一性校验
     *
     * @param m
     * @return
     */
    boolean checkUnique(MdrcCardmaker m);

}
