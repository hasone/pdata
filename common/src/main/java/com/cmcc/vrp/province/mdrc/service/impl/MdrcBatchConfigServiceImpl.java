package com.cmcc.vrp.province.mdrc.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.enums.MdrcTemplateType;
import com.cmcc.vrp.enums.NoticeMsgStatus;
import com.cmcc.vrp.province.dao.MdrcBatchConfigMapper;
import com.cmcc.vrp.province.dao.MdrcCardInfoMapper;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.quartz.jobs.BatchConfigExpireJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.PasswordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.Encrypter;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.PasswordEncoder;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author lgk8023
 * @date 2017年1月22日 下午2:39:24
 */
@Service("mdrcBatchConfigService")
public class MdrcBatchConfigServiceImpl implements MdrcBatchConfigService {
    private static final Logger logger = LoggerFactory.getLogger(MdrcBatchConfigServiceImpl.class);

    /**
     * 压缩文件夹名称
     */
    // public static final String ZIP_FILE_NAME = "yingxiaokashujuwenjian";
    // //包括图片和卡数据
    public static final String ZIP_FILE_NAME = "营销卡数据文件"; // 包括图片和卡数据

    /**
     * 压缩文件夹名称
     */
    public static final String DOWN_ZIP_FILE_NAME = "yingxiaokashujuwenjian.zip"; // 包括图片和卡数据

    private static final Logger LOGGER = LoggerFactory.getLogger(MdrcBatchConfigServiceImpl.class);

    private static String ZIP_ENCODEING = "GBK";

    @Autowired
    MdrcBatchConfigMapper mdrcBatchConfigMapper;
    @Autowired
    MdrcCardInfoMapper mdrcCardInfoMapper;
    @Autowired
    MdrcBatchConfigTransactional configTransactional;
    @Autowired
    MdrcCardmakerService mdrcCardmakerService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    AdministerService administerService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    CardNumAndPwdService mdrcCardNumAndPwdService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    ProductService productService;
    @Autowired
    MdrcBatchConfigInfoService mdrcBatchConfigInfoService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService;
    @Autowired
    S3Service s3Service;
    @Autowired
    ManagerService managerService;

    @Override
    public Long insertSelective(MdrcBatchConfig record) {
        return mdrcBatchConfigMapper.insertSelective(record);
    }

    @Override
    public MdrcBatchConfig selectByPrimaryKey(Long id) {
        return mdrcBatchConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public MdrcBatchConfig select(Long id) {
        // TODO Auto-generated method stub
        if (id != null) {
            return mdrcBatchConfigMapper.select(id);
        }
        return null;
    }

    @Override
    public List<MdrcBatchConfig> selectAllConfig() {
        // TODO Auto-generated method stub
        return mdrcBatchConfigMapper.selectAllConfig();
    }

    /**
     * @Title: queryCounts
     * @Description: TODO
     */
    @Transactional(readOnly = true)
    public int queryCounts(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return mdrcBatchConfigMapper.queryCounts(queryObject.toMap());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService#queryPagination(com.cmcc.vrp.util.QueryObject)
     */
    @Transactional(readOnly = true)
    public List<MdrcBatchConfig> queryPagination(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return mdrcBatchConfigMapper.queryPagination(queryObject.toMap());
    }

    @Override
    public MdrcBatchConfig selectModuleByPrimaryKey(Long id) {
        return mdrcBatchConfigMapper.selectModuleByPrimaryKey(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MdrcBatchConfig> selectByYearAndProvinceCode(String year, String provinceCode) {

        if (year == null || year.trim().equals("")) {
            return null;
        } else if (provinceCode == null || provinceCode.trim().equals("")) {
            return null;
        }

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("year", year);
        queryMap.put("provinceCode", provinceCode);
        return mdrcBatchConfigMapper.selectByYearAndProvinceCode(queryMap);
    }

    @Override
    public boolean update(MdrcBatchConfig m) {
        if (m == null) {
            return false;
        }
        return mdrcBatchConfigMapper.updateByPrimaryKeySelective(m) > 0;
    }

    @Override
    public boolean downloadFile(HttpServletRequest request, HttpServletResponse response, long configId,
            long operatorId, String fileName) throws IOException {

        // 根据configId, operatorId查询记录
        MdrcBatchConfig config = getByIdAndOperator(configId, operatorId);

        if (config == null) {
            return false;
        }

        // 为了实现只下载一次，需要对下载状态进行校验:未下载状态才可以下载
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigMapper.selectByPrimaryKey(configId);
        if (!MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode().equals(mdrcBatchConfig.getStatus())) {
            return false;
        }

        // 输出文件流
        String path = getDataFilePath() + File.separator + configId + File.separator + fileName;

        LOGGER.info("reading file from " + path);

        // 检查文件是否有效
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return false;
        }

        // 传输数据
        flushData(file, response);

        // 如果下载成功则更新状态
        // 2 更改状态
        String downloadIp = getIp(request);// 获取下载IP
        mdrcBatchConfig.setDownloadIp(downloadIp);// 设置下载IP
        mdrcBatchConfig.setDownloadTime(new Date());// 设置下载时间

        MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
        record.setConfigId(mdrcBatchConfig.getId());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setPreStatus(mdrcBatchConfig.getStatus());

        mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.DOWNLOADED.getCode());// 更新状态为已下载
        mdrcBatchConfigMapper.updateByPrimaryKeySelective(mdrcBatchConfig);// 更新数据库记录

        record.setNowStatus(mdrcBatchConfig.getStatus());
        record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        if (!mdrcBatchConfigStatusRecordService.insertSelective(record)) {
            logger.info("生成状态变更记录失败：preStatus:{} --> nowStatus:{}", record.getPreStatus(), record.getNowStatus());
        }
        return true;
    }

    @Override
    public boolean downloadFile(HttpServletRequest request, HttpServletResponse response, long configId,
            long operatorId, String key, long templateId) throws IOException {

        String fileName = key + ".zip";
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigMapper.selectByPrimaryKey(configId);

        if (fileStoreService.exist(key)) {
            logger.info("文件存在 key = {}", key);
            s3Service.downloadFromS3(response, key, fileName, request);
        } else {
            logger.info("文件不存在 key = {}", key);
        }

        // 如果下载成功则更新状态
        // 2 更改状态
        String downloadIp = getIp(request);// 获取下载IP
        mdrcBatchConfig.setDownloadIp(downloadIp);// 设置下载IP
        mdrcBatchConfig.setDownloadTime(new Date());// 设置下载时间

        MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
        record.setConfigId(mdrcBatchConfig.getId());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setPreStatus(mdrcBatchConfig.getStatus());
        record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());

        mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.DOWNLOADED.getCode());// 更新状态为已下载
        mdrcBatchConfigMapper.updateByPrimaryKeySelective(mdrcBatchConfig);// 更新数据库记录

        record.setNowStatus(mdrcBatchConfig.getStatus());
        if (!mdrcBatchConfigStatusRecordService.insertSelective(record)) {
            logger.info("生成状态变更记录失败：preStatus:{} --> nowStatus:{}", record.getPreStatus(), record.getNowStatus());
        }

        // FileUtils.deleteQuietly(new File(getZipFilePath()));
        return true;
    }

    @Override
    public List<MdrcBatchConfig> selectByCreatorIdAndStatus(Long creatorId, Long status) {
        if (creatorId == null || status == null) {
            return null;
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("creatorId", creatorId);
        queryMap.put("status", status);
        return mdrcBatchConfigMapper.selectByCreatorIdAndStatus(queryMap);
    }

    @Override
    public List<MdrcBatchConfig> selectByCardmaker(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return mdrcBatchConfigMapper.selectByCardmaker(queryObject.toMap());
    }

    @Override
    public int countByCardmaker(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return mdrcBatchConfigMapper.countByCardmaker(queryObject.toMap());
    }

    @Override
    public boolean listFile(Long configId, Long operatorId) { // public
                                                              // List<File>
                                                              // listFile(Long
                                                              // configId, Long
                                                              // operatorId)
        logger.info("configId = {}", configId);
        // 根据configId, operatorId查询记录
        MdrcBatchConfig config = getByIdAndOperator(configId, operatorId);

        if (config == null) {
            logger.info("未查找 configId = {} 该批次卡记录数据。", configId);
            return false;
        }
        // 查找
        MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService.selectByConfigId(configId);
        if (requestConfigMap == null) {
            logger.info("未查找 configId = {} 该批次卡记录数据对应的申请记录", configId);
            return false;
        }

        Enterprise enterprise = enterprisesService.selectById(config.getEnterpriseId());
        if (enterprise == null) {
            logger.info("未查找 configId = {} 所关联的企业信息", configId);
            return false;
        }

        MdrcCardmakeDetail orgMdrcCardmakeDetail = mdrcCardmakeDetailService
                .selectByRequestId(requestConfigMap.getRequestId());
        if (orgMdrcCardmakeDetail == null) {
            logger.info("未查找到营销卡数据申请详情信息 configId = {}, requestId = {}", configId, requestConfigMap.getRequestId());
            return false;
        }

        boolean result = false;
        List<File> files = null;
        // 设置文件存放路径
        String fileFolder = getDataFilePath() + File.separator + config.getId();
        File folder = new File(fileFolder);

        try {
            if (MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode().equals(config.getStatus())
                    && (!folder.exists() || (folder.exists() && readFiles(fileFolder).isEmpty()))) {
                LOGGER.info("开始创建数据文件...");
                if (folder.exists()) {
                    // 移除文件夹下已存在的文件
                    FileUtils.cleanDirectory(new File(fileFolder));
                } else {
                    folder.mkdirs();
                }

                // 生成相关文件
                doGenFiles(config, fileFolder);
            }

            // 读取已生成的文件
            // files = readFiles(fileFolder);

            String fileName = enterprise.getName() + "_" + config.getSerialNumber() + "_"
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(config.getCreateTime());
            // 压缩文件包
            if (createZIPfile(fileFolder, fileFolder, fileName, config)) {
                files = readZIPFiles(fileFolder);
            }

            logger.info("将文件上传至S3");
            if (files != null && files.size() > 0) {
                File file = files.get(0);
                if (fileStoreService.save(fileName, file)) {
                    logger.info("上传zip文件到S3成功。key = {}", fileName);

                    MdrcCardmakeDetail mdrcCardmakeDetail = new MdrcCardmakeDetail();
                    mdrcCardmakeDetail.setFileName(fileName);
                    mdrcCardmakeDetail
                            .setCardmakeStatus(Integer.valueOf(Constants.MAKE_CARD_STATUS.GENERATE_FILES.getResult()));
                    mdrcCardmakeDetail.setRequestId(orgMdrcCardmakeDetail.getRequestId());
                    if (!mdrcCardmakeDetailService.updateByPrimaryKeySelective(mdrcCardmakeDetail)) {
                        logger.info("更新已准备好数据提供下载标志位失败。待下载文件名称 fileName = {}, requestId = {}", fileName,
                                orgMdrcCardmakeDetail.getRequestId());
                    }
                    result = true;
                } else {
                    logger.info("上传zip文件到S3失败。key = {}", fileName);
                }
            }

            // 暂时注释掉删除文件做测试
            String path = getDataFilePath() + File.separator + configId;
            LOGGER.info("删除路径下临时文件 path = {}", path);
            FileUtils.deleteQuietly(new File(path));

        } catch (Exception e) {
            LOGGER.error("准备待下载文件失败,失败原因: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public List<File> prepareFilesForDownload(Long configId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public synchronized boolean create(String configName, long cardmakerId, long templateId, Date startTime,
            Date deadline, long amount, long creatorId) {
        // 查询制卡商
        MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(cardmakerId);

        if (mdrcCardmaker == null || deadline == null || startTime == null) {
            return false;
        }
        String provinceCode = getProvinceCode();

        // 获得当前年份
        DateTime dateTime = new DateTime();
        String year = String.valueOf(dateTime.getYear());

        MdrcBatchConfig m = new MdrcBatchConfig();
        m.setManagerId(creatorId);
        m.setConfigName(configName);
        m.setCardmakerId(cardmakerId);
        m.setTemplateId(templateId);// 设置模板
        m.setAmount(amount);
        m.setProvinceCode(provinceCode);// 省份编码
        m.setThisYear(year);
        m.setNotiFlag(NoticeMsgStatus.NO.getCode());// 设置默认值，未下发通知短信

        // 根据年份和省份编码查询配置规则
        List<MdrcBatchConfig> list = selectByYearAndProvinceCode(year, provinceCode);
        MdrcBatchConfig mbc = null;
        if (list != null && list.size() != 0) {
            mbc = list.get(0);// 获取批次号最大的配置规则
        }

        // 如果年份和省份编码均相同，则批次号在原有基础上进行累加
        if (mbc != null && year.equals(mbc.getThisYear()) && provinceCode.equals(mbc.getProvinceCode())) {
            m.setSerialNumber(mbc.getSerialNumber() + 1);
        } else {
            m.setSerialNumber(1);// 批次号，重新从1开始
        }
        m.setCreateTime(new Date());// 设置创建时间
        m.setCreatorId(creatorId);// 设置创建者ID
        m.setStatus(MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode());// 设置规则状态：未下载

        // 创建记录
        insertSelective(m);

        // 插入更新记录
        MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
        record.setConfigId(m.getId());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setPreStatus(m.getStatus());
        record.setNowStatus(m.getStatus());
        record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        mdrcBatchConfigStatusRecordService.insertSelective(record);

        // 批量插入卡
        mdrcCardInfoService.batchInsert(dateTime.getYearOfCentury(),
                buildCardInfos(m, startTime, deadline, mdrcCardmaker.getSerialNumber()));

        // 创建过期时的定时任务
        LOGGER.info("创建卡批次过期时的定时任务...");
        scheduleService.createScheduleJob(BatchConfigExpireJob.class, "BatchConfigExpireTask",
                String.valueOf(m.getId()), getQuartzId(m), deadline);

        LOGGER.info("发送短信通知采购人员");

        List<Administer> administers = administerService.queryAllUsersByAuthName("ROLE_MDRC_DATADL_CAIGOU");
        String content = m.getId() + "批次的流量卡数据已生成，请及时下载！";
        for (Administer admin : administers) {
            SmsPojo sms = new SmsPojo(admin.getMobilePhone(), content, null, null, null);
            LOGGER.info("手机号:" + sms.getMobile() + "短信内容:" + sms.getContent());
            taskProducer.produceDeliverNoticeSmsMsg(sms);
        }
        return true;
    }

    @Override
    public boolean notifyCardmaker(long configId) {

        MdrcBatchConfig mdrcBatchConfig = selectByPrimaryKey(configId);

        return notifyCardmaker(mdrcBatchConfig);
    }

    @Override
    public boolean notifyManager(long configId) {

        MdrcBatchConfig mdrcBatchConfig = selectByPrimaryKey(configId);

        return notifyManager(mdrcBatchConfig);
    }

    @Override
    public boolean downloadTxtFile(long configId, HttpServletResponse response) {
        MdrcBatchConfig config = mdrcBatchConfigMapper.selectByPrimaryKey(configId);
        if (config == null) {
            return false;
        }

        // 查询卡记录
        List<MdrcCardInfo> list = mdrcCardInfoService.listByConfig(config);
        if (list == null || list.size() <= 0) {
            return false;
        }

        OutputStream os = null;
        try {
            String encoded = URLEncoder.encode(TXT_FILE_NAME, "utf-8");
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + encoded);
            os = response.getOutputStream();
            String head = "SN|SIZE" + "\r\n";
            os.write(head.getBytes());
            for (MdrcCardInfo m : list) {
                // 记录格式：卡序列号；卡密码；产品名称
                String record = m.getCardNumber() + "|" + m.getProductSize() + "\r\n";
                byte[] content = record.getBytes();
                os.write(content);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkUnique(MdrcBatchConfig m) {
        if (m == null) {
            return true;
        }
        return mdrcBatchConfigMapper.checkUnique(m) <= 0;
    }

    @Override
    public List<MdrcBatchConfig> selectAllConfigByPagination(QueryObject queryObject) {
        if (queryObject != null) {
            return mdrcBatchConfigMapper.selectAllConfigByPagination(queryObject.toMap());
        }
        return null;
    }

    @Override
    public int countAllConfigByPagination(QueryObject queryObject) {
        if (queryObject != null) {
            return mdrcBatchConfigMapper.countAllConfigByPagination(queryObject.toMap());
        }
        return 0;
    }

    @Override
    public List<File> listPurchaseFile(long configId) {
        // 根据configId, operatorId查询记录
        MdrcBatchConfig config = mdrcBatchConfigMapper.selectByPrimaryKey(configId);

        if (config == null) {
            return null;
        }

        List<File> files = null;
        // 设置文件存放路径
        String fileFolder = getPurchaseDataFilePath() + File.separator + config.getId();
        File folder = new File(fileFolder);

        try {
            if (!folder.exists() || (folder.exists() && readFiles(fileFolder).isEmpty())) {
                LOGGER.info("开始创建数据文件...");
                if (folder.exists()) {
                    // 移除文件夹下已存在的文件
                    FileUtils.cleanDirectory(new File(fileFolder));
                } else {
                    folder.mkdirs();
                }

                // 生成相关文件
                doGenPurchaseFiles(config, fileFolder);
            }

            // 读取已生成的文件
            files = readFiles(fileFolder);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return files;
    }

    @Override
    public boolean downloadPurchaseFile(HttpServletRequest request, HttpServletResponse response, long configId,
            String fileName, long templateId) throws IOException {

        MdrcBatchConfig config = mdrcBatchConfigMapper.selectByPrimaryKey(configId);

        if (config == null) {
            return false;
        }

        // 输出文件流
        String path = getPurchaseDataFilePath() + File.separator + configId + File.separator + fileName;
        LOGGER.info("reading file from " + path);

        // 检查文件是否有效
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return false;
        }

        flushData(file, response);

        FileUtils.deleteQuietly(new File(getZipFilePath()));
        return true;
    }

    /**
     * 传输文件数据
     * <p>
     */
    private void flushData(File file, HttpServletResponse response) throws IOException {

        // 取得文件名。
        String filename = file.getName();
        String encoded = URLEncoder.encode(filename, "utf-8");
        // 清空response
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + encoded);
        response.addHeader("Content-Length", "" + file.length());
        // 以流的形式下载文件。
        byte[] fileData = FileUtils.readFileToByteArray(file);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(fileData);
        outputStream.flush();
    }

    /**
     * 获取下载者IP
     * <p>
     *
     * @author luozuwu
     */
    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 根据configId, operatorId查询记录
     * <p>
     */
    private MdrcBatchConfig getByIdAndOperator(Long configId, Long operatorId) {
        // 查询对应的记录
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("configId", configId);
        if (operatorId != null) {
            parameters.put("operatorId", operatorId);
        }
        List<MdrcBatchConfig> configList = mdrcBatchConfigMapper.selectByCardmaker(parameters);

        // 若记录无效则返回
        if (configList == null || configList.size() != 1 || configList.get(0) == null) {
            return null;
        }
        return configList.get(0);
    }

    /**
     * 根据指定的营销卡规则目录，读取目录下的文件列表
     * <p>
     */
    private List<File> readFiles(String fileFolder) throws IOException {
        LOGGER.info("Begin to read files...");

        File[] filesArr = null;
        File folder = new File(fileFolder);
        if (folder.exists() && folder.isDirectory() && (filesArr = folder.listFiles()) != null) {
            return Arrays.asList(filesArr);
        }

        throw new IOException("指定营销卡规则文件目录不存在, " + fileFolder);

    }

    /**
     * 读取压缩包文件
     *
     * @date 2016年8月2日
     * @author wujiamin
     */
    private List<File> readZIPFiles(String fileFolder) throws IOException {
        LOGGER.info("Begin to read ZIP files...");

        File folder = new File(fileFolder);
        File[] fileArr = null;

        if (folder.exists() && folder.isDirectory() && (fileArr = folder.listFiles()) != null) {
            List<File> files = Arrays.asList(fileArr);
            for (File file : files) {
                if (file.getName().contains(".zip")) {
                    List<File> fileList = new LinkedList<File>();
                    fileList.add(file);
                    return fileList;
                }
            }
        }

        throw new IOException("指定营销卡压缩文件不存在, " + fileFolder);

    }

    /**
     * 通用方法：创建ZIP文件
     *
     * @date 2016年8月2日
     * @author wujiamin
     */
    private boolean saveZIPfile(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
            LOGGER.info("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + File.separator + fileName + ".zip");
                if (zipFile.exists()) {
                    LOGGER.info(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.删除打包文件");
                    if (FileUtils.deleteQuietly(zipFile)) {
                        LOGGER.info(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.删除成功");
                    }
                }
                
                
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    LOGGER.info("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                } else {
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    zos.setEncoding("GBK");
                    
                    //设置压缩方式,其实默认就是这个
                    zos.setMethod(ZipOutputStream.DEFLATED); 
                    //设置压缩比率，最高，但是感觉作用不大
                    zos.setLevel(Deflater.BEST_COMPRESSION);
                    
                    LOGGER.info("ZipOutputStream.DEFAULT_COMPRESSION = "+ZipOutputStream.DEFLATED);
                    LOGGER.info("Deflater.BEST_COMPRESSION = " + Deflater.BEST_COMPRESSION);
                    
                    byte[] bufs = new byte[1024 * 10];
                    for (int i = 0; i < sourceFiles.length; i++) {
                        // 创建ZIP实体，并添加进压缩包
                        LOGGER.info("ZIP实体文件名:" + sourceFiles[i].getName());
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        // 读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(sourceFiles[i]);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                        fis.close();
                    }

                    flag = true;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                // 关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                    if (null != fis) {
                        fis.close();
                    }
                    if (null != fos) {
                        fos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }

    /**
     * 保存模板的图片到本地
     *
     * @date 2016年8月2日
     * @author wujiamin
     */
    private void saveImage(MdrcBatchConfig config, MdrcTemplate template) throws IOException {
        // 将卡封面，背面的图片存入指定路径

        String frontImageName = null;
        String rearImageName = null;

        if (template != null) {
            if (!StringUtils.isEmpty(template.getFrontImage())) {
                BufferedImage imageFront = build(template.getFrontImage());
                // frontImageName = getDataFilePath() + File.separator +
                // config.getId() + File.separator + template.getFrontImage();
                String[] types = template.getFrontImageName().split("\\.");
                if (types.length == 2) {
                    frontImageName = getDataFilePath() + File.separator + config.getId() + File.separator + types[0]
                            + "-正面." + types[1];
                    ImageIO.write(imageFront, types[1], new File(frontImageName));
                }
            }

            if (!StringUtils.isEmpty(template.getRearImage())) {
                BufferedImage rearImage = build(template.getRearImage());
                // rearImageName = getDataFilePath() + File.separator +
                // config.getId() + File.separator + template.getRearImage();
                // rearImageName = getDataFilePath() + File.separator +
                // config.getId() + File.separator +
                // template.getRearImageName();
                String[] types = template.getRearImageName().split("\\.");
                if (types.length == 2) {
                    rearImageName = getDataFilePath() + File.separator + config.getId() + File.separator + types[0]
                            + "-反面." + types[1];
                    ImageIO.write(rearImage, types[1], new File(rearImageName));
                }
            }
        }
    }

    private BufferedImage build(String key) {
        try {
            ImageInputStream inputStream = ImageIO.createImageInputStream(fileStoreService.get(key));
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            LOGGER.error("读取文件流信息时出错, Key= {}, 错误信息为{}, 错误堆栈为{}", key, e.getMessage(), e.getStackTrace());
        }

        return null;
    }

    /**
     * 创建压缩包文件
     *
     * @date 2016年8月2日
     * @author wujiamin
     */
    private boolean createZIPfile(String sourceFilePath, String zipFilePath, String fileName, MdrcBatchConfig config) {
        boolean flag = true;

        MdrcTemplate template = mdrcTemplateService.selectByPrimaryKey(config.getTemplateId());
        if (template == null) {
            return false;
        }

        try {
            saveImage(config, template);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!saveZIPfile(sourceFilePath, zipFilePath, fileName)) {
            LOGGER.info(zipFilePath + "目录下创建" + fileName + ".zip" + "打包文件,失败！");
            flag = false;
        }

        // 删除图片文件
        String[] typesFront = template.getFrontImageName().split("\\.");
        if (typesFront.length == 2 && !StringUtils.isEmpty(template.getFrontImage())
                && !FileUtils.deleteQuietly(new File(getDataFilePath() + File.separator + config.getId()
                        + File.separator + typesFront[0] + "-正面." + typesFront[1]))) {
            LOGGER.info("删除" + template.getFrontImage() + "失败");
            flag = false;
        }

        String[] typesRear = template.getRearImageName().split("\\.");
        if (typesFront.length == 2 && !StringUtils.isEmpty(template.getRearImage())
                && !FileUtils.deleteQuietly(new File(getDataFilePath() + File.separator + config.getId()
                        + File.separator + typesRear[0] + "-反面." + typesRear[1]))) {
            LOGGER.info("删除" + template.getRearImage() + "失败");
            flag = false;
        }
        return flag;
    }

    /**
     * 生成文件
     * <p>
     */
    private void doGenFiles(MdrcBatchConfig config, String fileFolder) throws Exception {

        // 查询卡记录
        List<MdrcCardInfo> list = mdrcCardInfoService.listByConfig(config);

        if (list == null) {
            return;
        }
        // 为卡信息设置年份，指定卡信息存储的表
        String year = config.getThisYear();
        // 截取年份后两位数字
        String y = year.substring(year.length() - 2, year.length());
        for (MdrcCardInfo m : list) {
            m.setYear(Integer.parseInt(y));
        }

        LOGGER.info("开始生成卡记录数据文件, configId=" + config.getId() + ", name=" + config.getConfigName());

        // 在内存中为每一条记录生成密码, 加密过的
        mdrcCardNumAndPwdService.generatePasswords(list);

        LOGGER.info("已生成卡记录密码, configId=" + config.getId() + ", name=" + config.getConfigName());

        // txt文件路径
        String txtFilePath = fileFolder + File.separator + TXT_FILE_NAME;

        // 加密后的文件路径
        String suffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String encryptedFile = fileFolder + File.separator + ENCRYPTED_FILE_NAME + "_" + suffix + ".data";

        LOGGER.info("输出txt文件, txtFile=" + txtFilePath);

        // 生成Excel文件，并写到指定路径
        // toExcel(excelFile, list, config.getExcelPassword());

        // 生成TXT文件
        toTxtFile(txtFilePath, list);

        // 取得制卡商信息
        MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectWithKeys(config.getCardmakerId());

        LOGGER.info("加密txt文件, excelFile=" + txtFilePath);

        // 加密文件

        encryptFile(txtFilePath, mdrcCardmaker, encryptedFile);

        // 完成加密后，删除txt文件
        FileUtils.deleteQuietly(new File(txtFilePath));
        LOGGER.info("Original file has been deleted...");

        // 设置状态
        // config.setStatus(MdrcBatchConfigStatus.PASSWORD_SUCCESS.getCode());

        // 批量更新记录
        configTransactional.updateRecord(list, config);
    }

    private void toTxtFile(String filePath, List<MdrcCardInfo> list) {
        // 打开一个随机访问文件流，按读写方式
        if (filePath == null || list == null) {
            return;
        }
        OutputStream os = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            String head = "SN|PASSWORD|DEADLINE" + "\r\n";
            os.write(head.getBytes());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (MdrcCardInfo m : list) {
                // 记录格式：卡序列号；卡密码；产品名称
                /*
                 * String record = m.getCardNumber() + "|" + m.getCardPassword()
                 * + "|" + m.getProductSize() + "\r\n";
                 */
                // 修改了pdata-497 记录格式变更为：卡序列号；卡密码；
                String record = m.getCardNumber() + "|" + m.getClearPsw() + "|" + formatter.format(m.getDeadline())
                        + "\r\n";
                byte[] content = record.getBytes();
                os.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加密excel文件
     */
    private void encryptFile(String excelFile, MdrcCardmaker mdrcCardmaker, String encryptedFile) throws Exception {

        // 读取文件到字节数组
        byte[] data = FileUtils.readFileToByteArray(new File(excelFile));
        byte[] encrypted = Encrypter.encrypt(data, mdrcCardmaker.getOperatorName(), mdrcCardmaker.getOperatorMobile(),
                mdrcCardmaker.getPrivateKey());

        LOGGER.info("orignal data size " + data.length + ", encrypted size " + encrypted.length);

        FileUtils.writeByteArrayToFile(new File(encryptedFile), encrypted);
        LOGGER.info("Write encrypted data to file...");
    }

    private List<MdrcCardInfo> buildCardInfos(MdrcBatchConfig config, Date startTime, Date deadline,
            String cardMakerSerialNum) {
        long count = config.getAmount();

        // 根据卡号卡密服务生成相应的卡号
        List<String> cardNumberList = mdrcCardNumAndPwdService.generatCardNums(config);
        if (cardNumberList == null || cardNumberList.size() != count) {
            return null;
        }

        List<MdrcCardInfo> cardInfos = new ArrayList<MdrcCardInfo>((int) count);
        for (int i = 0; i < count; i++) {
            MdrcCardInfo cardInfo = new MdrcCardInfo();

            String cardNum = cardNumberList.get(i);
            cardInfo.setCardNumber(cardNum);// 设置卡序列号
            cardInfo.setConfigId(config.getId());// 设置规则ID
            cardInfo.setStatus(MdrcCardStatus.NEW.getCode());// 设置卡状态，1：新制卡
            cardInfo.setCreateTime(new Date());// 创建时间
            cardInfo.setStartTime(startTime);// 生效日期
            cardInfo.setDeadline(deadline);// 失效日期
            cardInfo.setOpStatus(MdrcCardStatus.NORMAL.getCode());

            cardInfos.add(cardInfo);
        }

        return cardInfos;
    }

    private String getQuartzId(MdrcBatchConfig mbc) {
        return mbc.getId() + mbc.getProvinceCode() + mbc.getThisYear() + "_" + System.currentTimeMillis();
    }

    private boolean notifyCardmaker(MdrcBatchConfig config) {
        // 校验营销卡配置状态
        // 校验营销卡配置状态 notiFlag:0:未下发短信通知；1已下发短信通知
        if (config != null /* && config.getStatus().equals(1) */
                && config.getNotiFlag().toString().equals("0")) {
            MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(config.getCardmakerId());
            if (mdrcCardmaker == null) {
                LOGGER.info("制卡商已经被删除，下发短信通知失败。制卡商Id:" + config.getCardmakerId());
                return false;
            }

            String mobile = mdrcCardmaker.getOperatorMobile();
            String content = "您已收到一条新的制卡需求，请登入平台进行操作。";
            LOGGER.info("send message to mobile \"" + mobile + "\", content \"" + content + "\"");

            // 发送短信
            if (sendMsgService.sendVerifyCode(mobile, content)) {
                config.setNotiFlag(1);
                config.setNotiTime(new Date());
                update(config);

                return true;
            }
        }
        return false;
    }

    private boolean notifyManager(MdrcBatchConfig config) {
        if (config != null) {
            // 获取制卡专员信息
            MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(config.getCardmakerId());
            String cardMakerMobile = mdrcCardmaker.getOperatorMobile();
            // 获取客户经理信息
            Administer administer = administerService.selectAdministerById(config.getCreatorId());
            String mobile = administer.getMobilePhone();
            String content = "【营销卡平台】您生成的卡数据已被制卡商【" + cardMakerMobile + "】下载！";
            LOGGER.info("send message to mobile \"" + mobile + "\", content \"" + content + "\"");
            // 发送短信
            if (sendMsgService.sendVerifyCode(mobile, content)) {
                config.setNotiFlag(1);
                config.setNotiTime(new Date());
                update(config);
                return true;
            }
        }
        return false;
    }

    public String getDataFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_DATA_FILE_PATH.getKey());
    }

    public String getPurchaseDataFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_PURCHASE_DATA_FILE_PATH.getKey());
    }

    public String getTemplateFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey());
    }

    public String getZipFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_ZIP_FILE_PATH.getKey());
    }

    public String getProvinceCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_PROVINCE_CODE.getKey());
    }

    private String getCurrentTime() {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        dateStr = dateStr.replace("-", "");
        dateStr = dateStr.replace(":", "");
        dateStr = dateStr.replace(" ", "");
        // System.out.println(dateStr);
        return dateStr;
    }

    /**
     * 压缩文件
     * <p>
     */

    private void zipFile(File origFile, File destFile) throws IOException {

        InputStream input = null;
        ZipOutputStream zipOut = null;

        try {
            zipOut = new ZipOutputStream(new FileOutputStream(destFile));
            zipOut.setEncoding(ZIP_ENCODEING);

            if (origFile.isDirectory()) {
                File[] files = origFile.listFiles();// 查看文件夹下是否有文件
                if (files == null) {
                    return;
                }

                for (File file : files) {
                    input = new FileInputStream(file);
                    zipOut.putNextEntry(new ZipEntry(origFile.getName() + File.separator + file.getName()));
                    int temp = 0;
                    byte[] buffer = new byte[1024];
                    while ((temp = input.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, temp);
                    }
                    input.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }

            if (zipOut != null) {
                zipOut.close();
            }
        }
    }

    /**
     * 生成文件
     * <p>
     */
    private void doGenPurchaseFiles(MdrcBatchConfig config, String fileFolder) throws Exception {

        // 查询卡记录
        List<MdrcCardInfo> list = mdrcCardInfoService.listByConfig(config);

        if (list == null) {
            return;
        }
        // 为卡信息设置年份，指定卡信息存储的表
        String year = config.getThisYear();
        // 截取年份后两位数字
        String y = year.substring(year.length() - 2, year.length());
        for (MdrcCardInfo m : list) {
            m.setYear(Integer.parseInt(y));
        }

        LOGGER.info("开始生成卡记录数据文件, configId=" + config.getId() + ", name=" + config.getConfigName());

        // txt文件路径
        String txtFilePath = fileFolder + File.separator + TXT_FILE_NAME;

        LOGGER.info("输出txt文件, txtFile=" + txtFilePath);

        // 生成TXT文件
        toTxtPurchaseFile(txtFilePath, list);

    }

    private void toTxtPurchaseFile(String filePath, List<MdrcCardInfo> list) {
        // 打开一个随机访问文件流，按读写方式
        if (filePath == null || list == null) {
            return;
        }
        OutputStream os = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            String head = "CID|SN|SIZE|DEADLINE" + "\r\n";
            os.write(head.getBytes());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (MdrcCardInfo m : list) {
                // 记录格式：卡序列号；卡密码；产品名称
                /*
                 * String record = m.getCardNumber() + "|" + m.getCardPassword()
                 * + "|" + m.getProductSize() + "\r\n";
                 */
                // 修改了pdata-497 记录格式变更为：卡序列号；卡密码；
                String record = m.getConfigId() + "|" + m.getCardNumber() + "|"
                        + Integer.parseInt(m.getProductSize()) / 1024 + "|" + formatter.format(m.getDeadline())
                        + "\r\n";
                byte[] content = record.getBytes();
                os.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * @Override public boolean downloadPurchaseFile(HttpServletRequest request,
     * HttpServletResponse response, long configId, String fileName, long
     * templateId) throws IOException {
     * 
     * MdrcBatchConfig config =
     * mdrcBatchConfigMapper.selectByPrimaryKey(configId);
     * 
     * if (config == null) { return false; }
     * 
     * // 输出文件流 String path = getPurchaseDataFilePath() + File.separator +
     * configId + File.separator + fileName; LOGGER.info("reading file from " +
     * path);
     * 
     * // 检查文件是否有效 File file = new File(path); if (!file.exists() ||
     * !file.isFile()) { return false; }
     * 
     * flushData(file, response);
     * 
     * FileUtils.deleteQuietly(new File(getZipFilePath())); return true; }
     */

    @Override
    public boolean submitMdrcCardmakeApproval(ApprovalRequest approvalRequest, MdrcCardmakeDetail mdrcCardmakeDetail,
            MdrcBatchConfigInfo mdrcBatchConfigInfo) {

        Product product = productService.get(mdrcCardmakeDetail.getProductId());
        MdrcTemplate mdrcTemplate = new MdrcTemplate();
        // 准备要插入的数据
        if (mdrcCardmakeDetail.getTemplateId() == null) {
            mdrcTemplate.setName("自定义模板");
            mdrcTemplate.setFrontImage(mdrcBatchConfigInfo.getTemplateFrontKey());
            mdrcTemplate.setRearImage(mdrcBatchConfigInfo.getTemplateBackKey());
            mdrcTemplate.setProductId(mdrcCardmakeDetail.getProductId());
            mdrcTemplate.setCreateTime(new Date());
            mdrcTemplate.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            mdrcTemplate.setCreatorId(approvalRequest.getCreatorId());
            mdrcTemplate.setResourcesCount(1);
            mdrcTemplate.setProductSize(product.getProductSize().toString());
            mdrcTemplate.setType(1);

            mdrcTemplate.setFrontImageName(mdrcBatchConfigInfo.getTemplateFrontName());
            mdrcTemplate.setRearImageName(mdrcBatchConfigInfo.getTemplateBackName());
        }

        mdrcBatchConfigInfo.setCreateTime(new Date());
        mdrcBatchConfigInfo.setUpdateTime(new Date());
        mdrcBatchConfigInfo.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        // 计算制卡成本
        // mdrcCardmakeDetail.setCost(getMakecardCost(mdrcCardmakeDetail.getAmount()));

        try {
            if (insertMdrcCardmakeApproval(approvalRequest, mdrcTemplate, mdrcCardmakeDetail, mdrcBatchConfigInfo)) {
                return true;
            } else {
                LOGGER.info("插入数据失败");
                return false;
            }
        } catch (RuntimeException e) {
            LOGGER.info("插入数据失败，失败原因：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean submitEditMdrcCardmakeApproval(ApprovalRequest approvalRequest,
            MdrcCardmakeDetail mdrcCardmakeDetail, MdrcBatchConfigInfo mdrcBatchConfigInfo) {
        // TODO Auto-generated method stub

        MdrcTemplate newMdrcTemplate = new MdrcTemplate();
        MdrcCardmakeDetail orgMdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(approvalRequest.getId());
        MdrcTemplate orgMdrcTemplate = mdrcTemplateService.selectByPrimaryKey(orgMdrcCardmakeDetail.getTemplateId());
        Product product = productService.get(mdrcCardmakeDetail.getProductId());

        if (mdrcCardmakeDetail.getTemplateId() == null) {
            if (orgMdrcTemplate.getType().toString().equals(MdrcTemplateType.COMMON_TEMPLATE.getCode().toString())) {
                // 原使用的是模板库的模板
                newMdrcTemplate.setName("自定义模板");
                newMdrcTemplate.setFrontImage(mdrcBatchConfigInfo.getTemplateFrontKey());
                newMdrcTemplate.setRearImage(mdrcBatchConfigInfo.getTemplateBackKey());
                newMdrcTemplate.setProductId(mdrcCardmakeDetail.getProductId());
                newMdrcTemplate.setCreateTime(new Date());
                newMdrcTemplate.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                newMdrcTemplate.setCreatorId(approvalRequest.getCreatorId());
                newMdrcTemplate.setResourcesCount(1);
                newMdrcTemplate.setProductSize(product.getProductSize().toString());
                newMdrcTemplate.setType(1);

                newMdrcTemplate.setFrontImageName(mdrcBatchConfigInfo.getTemplateFrontName());
                newMdrcTemplate.setRearImageName(mdrcBatchConfigInfo.getTemplateBackName());
            } else {
                // 原使用的是就是自定义模板
                newMdrcTemplate.setId(orgMdrcTemplate.getId());
                newMdrcTemplate.setProductSize(product.getProductSize().toString());
                newMdrcTemplate.setProductId(mdrcCardmakeDetail.getProductId());
            }
        } else {
            newMdrcTemplate = null;
        }

        mdrcBatchConfigInfo.setUpdateTime(new Date());

        // 计算制卡成本
        // mdrcCardmakeDetail.setCost(getMakecardCost(mdrcCardmakeDetail.getAmount()));

        try {
            if (updateMdrcCardmakeApproval(approvalRequest, newMdrcTemplate, mdrcCardmakeDetail, mdrcBatchConfigInfo)) {
                LOGGER.info("更新数据成功.");
                return true;
            } else {
                LOGGER.info("更新数据失败.");
                return false;
            }

        } catch (RuntimeException e) {
            LOGGER.info("更新数据失败，失败原因：{}", e.getMessage());
            return false;
        }
    }

    @Transactional
    private boolean updateMdrcCardmakeApproval(ApprovalRequest approvalRequest, MdrcTemplate mdrcTemplate,
            MdrcCardmakeDetail mdrcCardmakeDetail, MdrcBatchConfigInfo mdrcBatchConfigInfo) {
        if (!approvalRequestService.updateByPrimaryKeySelective(approvalRequest)) {
            LOGGER.info("更新请求制卡申请失败。approvalRequest = {}", JSON.toJSONString(approvalRequest));
            return false;
        }

        if (mdrcTemplate != null) {
            if (mdrcTemplate.getId() != null) {
                if (!mdrcTemplateService.updateByIdSeletive(mdrcTemplate)) {
                    LOGGER.info("更新自定义模板信息失败。mdrcTemplate = {}", JSON.toJSONString(mdrcTemplate));
                    throw new RuntimeException();
                }
            } else {
                if (!mdrcTemplateService.insertSelective(mdrcTemplate)) {
                    LOGGER.info("插入自定义模板失败");
                    throw new RuntimeException();
                }
                mdrcCardmakeDetail.setTemplateId(mdrcTemplate.getId());
            }
        }

        if (!mdrcCardmakeDetailService.updateByPrimaryKeySelective(mdrcCardmakeDetail)) {
            LOGGER.info("更新制卡信息失败。mdrcCardmakeDetail = {}", JSON.toJSONString(mdrcCardmakeDetail));
            throw new RuntimeException();
        }

        if (!mdrcBatchConfigInfoService.updateByPrimaryKeySelective(mdrcBatchConfigInfo)) {
            LOGGER.info("更新制卡扩展信息失败。MdrcBatchConfigInfo = {}", JSON.toJSONString(mdrcBatchConfigInfo));
            throw new RuntimeException();
        }

        if (approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
            ApprovalRecord approvalRecord = createApprovalRecord(approvalRequest);
            if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    @Transactional
    private boolean insertMdrcCardmakeApproval(ApprovalRequest approvalRequest, MdrcTemplate mdrcTemplate,
            MdrcCardmakeDetail mdrcCardmakeDetail, MdrcBatchConfigInfo mdrcBatchConfigInfo) {
        // 1、插入审批记录
        if (!approvalRequestService.insert(approvalRequest)) {
            return false;
        }

        // 2、插入营销卡扩展信息
        if (!mdrcBatchConfigInfoService.insertSelective(mdrcBatchConfigInfo)) {
            LOGGER.info("插入营销卡扩展信息失败");
            throw new RuntimeException();
        }

        // 3、插入自定义卡模板
        if (mdrcTemplate != null && mdrcTemplate.getType() != null
                && mdrcTemplate.getType().intValue() == MdrcTemplateType.INDIVIDUATION_TEMPLATE.getCode().intValue()) {
            if (!mdrcTemplateService.insertSelective(mdrcTemplate)) {
                LOGGER.info("插入自定义模板失败");
                throw new RuntimeException();
            }
            mdrcCardmakeDetail.setTemplateId(mdrcTemplate.getId());
        }

        mdrcCardmakeDetail.setRequestId(approvalRequest.getId());
        mdrcCardmakeDetail.setConfigInfoId(mdrcBatchConfigInfo.getId());
        mdrcCardmakeDetail.setDeleteFlag((long) Constants.DELETE_FLAG.UNDELETED.getValue());
        mdrcCardmakeDetail.setCardmakeStatus(0);
        if (!mdrcCardmakeDetailService.insertSelective(mdrcCardmakeDetail)) {
            LOGGER.info("插入卡数据详情失败");
            throw new RuntimeException();
        }

        if (approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
            ApprovalRecord approvalRecord = createApprovalRecord(approvalRequest);
            if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    private ApprovalRecord createApprovalRecord(ApprovalRequest approvalRequest) {
        ApprovalRecord record = new ApprovalRecord();
        record.setRequestId(approvalRequest.getId());
        record.setCreatorId(approvalRequest.getCreatorId());
        record.setDescription(approvalRequestService.getCurrentStatus(approvalRequest));
        record.setCreateTime(new Date());
        // record.setUpdateTime(new Date());
        record.setIsNew(1);
        record.setDeleteFlag(0);
        return record;
    }

    @Override
    public List<MdrcBatchConfig> selectByMap(Map map) {
        return mdrcBatchConfigMapper.selectByMap(map);
    }

    @Override
    public MdrcBatchConfig selectBySerialNum(String serialNum, String year) {
        return mdrcBatchConfigMapper.selectBySerialNum(serialNum, year);
    }

    @Override
    @Transactional
    public boolean handleDownloadFail(Long configId) {
        // TODO Auto-generated method stub
        if (configId != null) {
            MdrcBatchConfig mdrcBatchConfig = new MdrcBatchConfig();
            mdrcBatchConfig.setId(configId);

            // 插入变更记录
            MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
            record.setConfigId(mdrcBatchConfig.getId());
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setPreStatus(mdrcBatchConfig.getStatus());

            mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.USELESS.getCode());
            if (!update(mdrcBatchConfig)) {
                logger.info("将该卡批次config = {}置失效.", configId);
                return false;
            }

            if (!mdrcCardInfoService.changeStatusByConfigId(MdrcCardStatus.USELESS.getCode(), configId)) {
                logger.info("将该卡批次config = {}置失效.", configId);
                throw new RuntimeException();
            }

            record.setNowStatus(mdrcBatchConfig.getStatus());
            record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            if (!mdrcBatchConfigStatusRecordService.insertSelective(record)) {
                logger.info("生成批次状态变更记录失败preStatus:{} --> nowStatus:{}.", record.getPreStatus(), record.getNowStatus());
                throw new RuntimeException();
            }
            return true;
        }
        return false;
    }

    @Override
    public List<MdrcBatchConfig> selectByEntIdAndStatus(Long entId, MdrcBatchConfigStatus status) {
        return mdrcBatchConfigMapper.selectByEntIdAndStatus(entId, status.getCode());
    }

    @Override
    public MdrcBatchConfig getConfigDetailsByIdAndStatus(Long configId, Integer status) {
        MdrcBatchConfig config = selectByPrimaryKey(configId);
        if (config != null) {
            String year = getYear(config.getThisYear());
            if (StringUtils.isNotBlank(year)) {
                return mdrcBatchConfigMapper.getConfigDetailsByIdAndStatus(configId, year, status);
            }
        }
        return new MdrcBatchConfig();
    }

    @Override
    public MdrcBatchConfig getConfigDetailsById(Long configId) {
        MdrcBatchConfig config = selectByPrimaryKey(configId);
        if (config != null) {
            String year = getYear(config.getThisYear());
            if (StringUtils.isNotBlank(year)) {
                return mdrcBatchConfigMapper.getConfigDetailsById(configId, year);
            }
        }
        return new MdrcBatchConfig();
    }

    // 获取卡信息表后缀,约定：约定长度大于2位，并取后两位为卡信息表后缀
    private String getYear(String year) {
        if (StringUtils.isBlank(year) || year.length() < 2) {
            logger.info("配置规则中年份信息错误，约定长度大于2位，并取后两位为卡信息表后缀，实际年份：{}。" + year);
            return null;
        } else {
            return year.substring(year.length() - 2, year.length());
        }
    }

    @Override
    public boolean isOverAuth(Long currentUserId, Long configId) {
        if(currentUserId == null || configId == null){
            logger.info("参数为空。");
            return true;
        }
        
        Manager sonManager = managerService.getManagerByAdminId(currentUserId);
        if(sonManager == null){
            logger.info("用户节点不存在。currentUserId = " + currentUserId);
            return true;
        }
        
        //省级管理员、市级管理员、客户经理、企业管理员需要校验子父节点关系,其他节点不需要校验
        if(!managerService.isProOrCityOrMangerOrEnt(sonManager.getRoleId())){
            return false;
        }
        
        MdrcBatchConfig config = mdrcBatchConfigMapper.selectByPrimaryKey(configId);
        if(config == null || config.getCreatorId() == null){
            logger.info("规则不存在。configId = " + configId);
            return true;
        }
        
        //该规则是当前用户节点的父节点用户创建时，不越权
        Manager fatherManager = managerService.getManagerByAdminId(config.getCreatorId());
        if (fatherManager != null
                && (managerService.isParentManage(sonManager.getId(), fatherManager.getId()) || managerService
                        .isParentManage(fatherManager.getId(), sonManager.getId()))) {
            return false;
        } 
        return true;  
    }
}
