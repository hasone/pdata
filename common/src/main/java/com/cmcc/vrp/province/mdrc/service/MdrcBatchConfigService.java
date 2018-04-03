package com.cmcc.vrp.province.mdrc.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.util.QueryObject;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:37:51
*/
public interface MdrcBatchConfigService {

    /**
     * 默认excel文件名称
     */
    public static final String EXCEL_FILE_NAME = "yingxiaokashujuwenjian.xlsx";

    /**
     * 默认txt文件名称
     */
    public static final String TXT_FILE_NAME = "yingxiaokajilushuju.txt";

    /**
     * 加密后的文件名称
     */
    public static final String ENCRYPTED_FILE_NAME = "营销卡数据文件";

    /**
     * 默认卡密码为6位数字
     */
    public static final int CARD_PASSWORD_SIZE = 14;

    /**
     * 默认excel文件密码为6位数字
     */

    public static final int EXCEL_PASSWORD_SIZE = 6;
    /**
     * 默认秘钥长度为32位
     */
    public static final int CARD_SECRET_SIZE = 32;

    /**
     * @param record
     * @return
     */
    Long insertSelective(MdrcBatchConfig record);

    /**
     * @param id
     * @return
     */
    MdrcBatchConfig selectByPrimaryKey(Long id);

    /**
     * @param id
     * @return
     */
    MdrcBatchConfig select(Long id);

    /**
     * @return
     */
    List<MdrcBatchConfig> selectAllConfig();

    /**
     * @param queryObject
     * @return
     */
    int queryCounts(QueryObject queryObject);

    // 联合查询：配置表、制卡商表、模板表
    /**
     * @param queryObject
     * @return
     */
    List<MdrcBatchConfig> queryPagination(QueryObject queryObject);

    /**
     * @param id
     * @return
     */
    MdrcBatchConfig selectModuleByPrimaryKey(Long id);

    // 根据年份和省份编码查询
    /**
     * @param year
     * @param provinceCode
     * @return
     */
    List<MdrcBatchConfig> selectByYearAndProvinceCode(String year, String provinceCode);

    // 更新记录
    /**
     * @param m
     * @return
     */
    boolean update(MdrcBatchConfig m);

    // 下载数据
    /**
     * @param request
     * @param response
     * @param configId
     * @param managerId
     * @param fileName
     * @return
     * @throws IOException
     */
    boolean downloadFile(HttpServletRequest request, HttpServletResponse response, long configId, long managerId,
            String fileName) throws IOException;

    //下载数据，并压缩文件
    /**
     * @param request
     * @param response
     * @param configId
     * @param managerId
     * @param fileName
     * @param templateId
     * @return
     * @throws IOException
     */
    boolean downloadFile(HttpServletRequest request, HttpServletResponse response, long configId, long managerId,
            String fileName, long templateId) throws IOException;

    // 根据创建者ID和状态查询
    /**
     * @param creatorId
     * @param status
     * @return
     */
    List<MdrcBatchConfig> selectByCreatorIdAndStatus(Long creatorId, Long status);

    /**
     * 查询与制卡商关联的记录
     * <p>
     *
     * @param queryObject
     * @return
     */
    List<MdrcBatchConfig> selectByCardmaker(QueryObject queryObject);

    /**
     * 查询与制卡商关联的记录条数
     * <p>
     *
     * @param queryObject
     * @return
     */
    int countByCardmaker(QueryObject queryObject);

    /**
     * 获取当前规则的所有数据文件
     * <p>
     *
     * @param configId
     * @param operatorId
     * @return
     */
    //List<File> listFile(Long configId, Long operatorId);
    boolean listFile(Long configId, Long operatorId);

    /**
     * 准备待下载的文件
     * @param configId
     * @author qinqinyan
     * @date 2017/08/08
     * */
    List<File> prepareFilesForDownload(Long configId);

    /**
     * 生成营销卡数据
     * <p>
     *
     * @param configName
     * @param cardmakerId
     * @param amount
     */
    boolean create(String configName, long cardmakerId, long productId, Date startTime, Date deadline, long amount,
            long creatorId);

    /**
     * 向制卡商发送短信通知卡数据可下载
     * <p>
     *
     * @param configId
     * @return
     */
    boolean notifyCardmaker(long configId);

    /**
     * 向客户经理下发短信通知卡数据已被下载
     * <p>
     *
     * @return
     */
    boolean notifyManager(long config);

    /**
     * 文件下载
     *
     * @param configId
     * @param response
     * @return
     */
    boolean downloadTxtFile(long configId, HttpServletResponse response);

    /**
     * 唯一性校验
     *
     * @param m
     * @return
     */
    boolean checkUnique(MdrcBatchConfig m);

    /**
     * 分页查找所有卡批次
     * @param map
     * @return
     */
    List<MdrcBatchConfig> selectAllConfigByPagination(QueryObject queryObject);

    /**
    * @Title: countAllConfigByPagination
    * @Description: TODO
    */
    int countAllConfigByPagination(QueryObject queryObject);

    /**
     * 下载数据(采购)
     * <p>
     * 
     * @param configId
     * @return
     */
    List<File> listPurchaseFile(long configId);

    /**
    * @Title: downloadPurchaseFile
    * @Description: TODO
    */
    boolean downloadPurchaseFile(HttpServletRequest request, HttpServletResponse response, long configId,
            String fileName, long templateId) throws IOException;

    /**
     * 提交审核
     * @param approvalRequest
     * @param mdrcCardmakeDetail
     * @author qinqinyan
     * 
     * edit by qinqinyan on 2017/08/01 for v1.12.1
     * */
    boolean submitMdrcCardmakeApproval(ApprovalRequest approvalRequest, MdrcCardmakeDetail mdrcCardmakeDetail,
            MdrcBatchConfigInfo mdrcBatchConfigInfo);

    /**
     * 提交编辑被驳回的制卡审核信息
     * @author qinqinyan
     * @date 2017/08/13
     * */
    boolean submitEditMdrcCardmakeApproval(ApprovalRequest approvalRequest, MdrcCardmakeDetail mdrcCardmakeDetail,
            MdrcBatchConfigInfo mdrcBatchConfigInfo);

    /**
     * 根据map查询
     * @param map
     * @return
     * */
    List<MdrcBatchConfig> selectByMap(Map map);

    /**
     * 根据批次号查询
     * 
     * @param serialNum
     * @param year 
     * @return
     */
    MdrcBatchConfig selectBySerialNum(String serialNum, String year);

    /**
     * 下载失败处理
     * @param configId
     * @author qinqinyan
     * @date 2017/08/10
     * */
    boolean handleDownloadFail(Long configId);

    /**
     * 
     * @Title: selectByEntIdAndStatus 
     * @Description: TODO
     * @param entId
     * @param status
     * @return
     * @return: List<MdrcBatchConfig>
     */
    List<MdrcBatchConfig> selectByEntIdAndStatus(Long entId, MdrcBatchConfigStatus status);

    /**
     * 
     * @Title: getConfigDetailsByIdAndStatus 
     * @Description: TODO
     * @param configId
     * @param status
     * @return
     * @return: MdrcBatchConfig
     */
    MdrcBatchConfig getConfigDetailsByIdAndStatus(Long configId, Integer status);

    /**
     * 
     * @Title: getConfigDetailsById 
     * @Description: TODO
     * @param configId
     * @return
     * @return: MdrcBatchConfig
     */
    MdrcBatchConfig getConfigDetailsById(Long configId);
    
    /**
     * 
     * @Title: isOverAuth 
     * @Description: 校验当前用户是否有权限查看营销卡规则信息
     * @param currentUserId
     * @param configId
     * @return
     * @return: boolean
     */
    boolean isOverAuth(Long currentUserId, Long configId);
}
