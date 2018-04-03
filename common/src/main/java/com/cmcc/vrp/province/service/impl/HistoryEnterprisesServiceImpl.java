package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.province.dao.HistoryEnterprisesMapper;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ApprovalRequestSmsService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterpriseFileService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by qinqinyan on 2016/10/12.
 */
@Service("historyEnterprisesService")
public class HistoryEnterprisesServiceImpl implements HistoryEnterprisesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryEnterprisesServiceImpl.class);
    protected String zyProvinceFlagValue = "ziying";
    @Autowired
    HistoryEnterprisesMapper mapper;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    EnterpriseFileService enterpriseFileService;
    @Autowired
    HistoryEnterpriseFileService historyEnterpriseFileService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    GiveMoneyEnterService giveMoneyEnterService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    private ApprovalRequestSmsService approvalRequestSmsService;

    @Override
    public boolean insert(HistoryEnterprises record) {
        if (record != null && record.getEntId() != null) {
            return mapper.insert(record) == 1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(HistoryEnterprises record) {
        if (record != null) {
            return mapper.insertSelective(record) == 1;
        }
        return false;
    }

    @Override
    public HistoryEnterprises selectByRequestId(Long requestId) {
        if (requestId != null) {
            return mapper.selectByRequestId(requestId);
        }
        return null;
    }

    @Override
    public boolean updateStatusByPrimaryKey(HistoryEnterprises record) {
        if (record == null) {
            return false;
        } else {
            HistoryEnterprises he = new HistoryEnterprises();
            he.setRequestId(record.getRequestId());
            he.setUpdateTime(new Date());
            he.setDeleteFlag(record.getDeleteFlag());
            return mapper.updateStatusByRequestId(he) == 1;
        }
    }

    // 初始化企业信息
    private HistoryEnterprises initHistoryEnterprises(HistoryEnterprises historyEnterprises, Long adminId,
            Long requestId, Integer stage) {
        historyEnterprises.setCreatorId(adminId);
        historyEnterprises.setCreateTime(new Date());
        historyEnterprises.setUpdateTime(new Date());
        if (stage.toString().equals("0")) {
            historyEnterprises.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
        } else {
            historyEnterprises.setDeleteFlag(EnterpriseStatus.SUBMIT_APPROVAL.getCode());
        }
        historyEnterprises.setRequestId(requestId);
        return historyEnterprises;
    }

    private HistoryEnterpriseFile initHistoryEnterpriseFile(HistoryEnterprises historyEnterprises,
            MultipartHttpServletRequest request, Long requestId) {
        HistoryEnterpriseFile hef = new HistoryEnterpriseFile();

        EnterpriseFile enterpriseFile = enterpriseFileService.selectByEntId(historyEnterprises.getEntId());

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());
            String originalFilename = multipartFile.getOriginalFilename();
            if (!StringUtils.isEmpty(originalFilename)) {
                if (multipartFile.getName().toString().equals("licenceImage")) {

                    hef.setBusinessLicence(originalFilename);

                } else if (multipartFile.getName().toString().equals("authorization")) {

                    hef.setAuthorizationCertificate(originalFilename);

                } else if (multipartFile.getName().toString().equals("identification")) {

                    hef.setIdentificationCard(originalFilename);

                } else if (multipartFile.getName().toString().equals("identificationBack")) {

                    hef.setIdentificationBack(originalFilename);

                } else if (multipartFile.getName().toString().equals("customerFile")) {

                    hef.setCustomerfileName(originalFilename);

                } else if (multipartFile.getName().toString().equals("contract")) {

                    hef.setContractName(originalFilename);

                } else if (multipartFile.getName().toString().equals("image")) {

                    hef.setImageName(originalFilename);
                }
            }
        }

        if (StringUtils.isEmpty(hef.getBusinessLicence())) {
            hef.setBusinessLicence(enterpriseFile.getBusinessLicence());
            hef.setLicenceKey(enterpriseFile.getLicenceKey());
        }

        if (StringUtils.isEmpty(hef.getAuthorizationCertificate())) {
            hef.setAuthorizationCertificate(enterpriseFile.getAuthorizationCertificate());
            hef.setAuthorizationKey(enterpriseFile.getAuthorizationKey());
        }

        if (StringUtils.isEmpty(hef.getIdentificationCard())) {
            hef.setIdentificationCard(enterpriseFile.getIdentificationCard());
            hef.setIdentificationKey(enterpriseFile.getIdentificationKey());
        }

        if (StringUtils.isEmpty(hef.getIdentificationBack())) {
            hef.setIdentificationBack(enterpriseFile.getIdentificationBack());
            hef.setIdentificationBackKey(enterpriseFile.getIdentificationBackKey());
        }

        if (StringUtils.isEmpty(hef.getContractName())) {
            hef.setContractName(enterpriseFile.getContractName());
            hef.setContractKey(enterpriseFile.getContractKey());
        }

        if (StringUtils.isEmpty(hef.getCustomerfileName())) {
            hef.setCustomerfileName(enterpriseFile.getCustomerfileName());
            hef.setCustomerfileKey(enterpriseFile.getCustomerfileKey());
        }

        if (StringUtils.isEmpty(hef.getImageName())) {
            hef.setImageName(enterpriseFile.getImageName());
            hef.setImageKey(enterpriseFile.getImageKey());
        }

        hef.setCreateTime(new Date());
        hef.setUpdateTime(new Date());
        hef.setRequestId(requestId);
        hef.setEntId(historyEnterprises.getEntId());
        return hef;
    }

    @Override
    @Transactional
    public boolean saveEdit(HistoryEnterprises historyEnterprises, Long adminId, MultipartHttpServletRequest request) {
        if (historyEnterprises == null || adminId == null) {
            return false;
        }

        try {
            // 1、插入提交审批请求
            Long requestId = approvalRequestService.submitEnterpriseChange(historyEnterprises.getEntId(), adminId);
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.Ent_Information_Change_Approval.getCode());
            if (requestId != null) {
                LOGGER.info("成功插入企业信息变更请求,请求ID-" + requestId.toString());

                // 2、插入企业变更信息
                initHistoryEnterprises(historyEnterprises, adminId, requestId, approvalProcessDefinition.getStage());
                if (!insertSelective(historyEnterprises)) {
                    LOGGER.info("插入企业变更信息失败,企业ID-" + historyEnterprises.getEntId());
                    throw new RuntimeException();
                }

                // 3、上传文件
                HistoryEnterpriseFile hef = initHistoryEnterpriseFile(historyEnterprises, request, requestId);
                uploadFiles(request, hef);

                // 4、更新historyEnterpriseFile
                if (!historyEnterpriseFileService.insertSelective(hef)) {
                    LOGGER.info("插入企业文件信息失败,请求ID-" + requestId.toString());
                    throw new RuntimeException();
                }

                // 无须审核
                if (approvalProcessDefinition.getStage().toString().equals("0")) {
                    if (!enterprisesService.updateByPrimaryKeySelective(generateEnterprise(historyEnterprises))) {
                        throw new RuntimeException();
                    }
                    if (!enterpriseFileService.update(generateEnterpriseFile(hef))) {
                        throw new RuntimeException();
                    }

                    // 自营不存在更改折扣
                    if (!getProvinceFlag().equals(zyProvinceFlagValue)) {
                        if (!giveMoneyEnterService.updateByEnterId(historyEnterprises.getGiveMoneyId(),
                                historyEnterprises.getEntId())) {
                            throw new RuntimeException();
                        }
                    }
                }else{
                    approvalRequestSmsService.sendNoticeSms(requestId);
                }
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            LOGGER.info("插入企业信息变更请求失败,企业ID-" + historyEnterprises.getEntId().toString());
            return false;
        }
    }

    public String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
    }

    private Enterprise generateEnterprise(HistoryEnterprises he) {
        Enterprise enter = new Enterprise();
        enter.setId(he.getEntId());
        enter.setName(he.getName());
        enter.setEntName(he.getEntName());
        enter.setCode(he.getCode());
        enter.setDiscount(he.getDiscount());
        enter.setPhone(he.getPhone());
        enter.setEmail(he.getEmail());
        enter.setLicenceStartTime(he.getLicenceStartTime());
        enter.setLicenceEndTime(he.getLicenceEndTime());
        enter.setStartTime(he.getStartTime());
        enter.setEndTime(he.getEndTime());
        enter.setUpdateTime(new Date());
        return enter;
    }

    private EnterpriseFile generateEnterpriseFile(HistoryEnterpriseFile hef) {
        EnterpriseFile enterpriseFile = new EnterpriseFile();

        enterpriseFile.setImageName(hef.getImageName());
        enterpriseFile.setImageKey(hef.getImageKey());

        enterpriseFile.setBusinessLicence(hef.getBusinessLicence());
        enterpriseFile.setLicenceKey(hef.getLicenceKey());

        enterpriseFile.setIdentificationCard(hef.getIdentificationCard());
        enterpriseFile.setIdentificationKey(hef.getIdentificationKey());

        enterpriseFile.setIdentificationBack(hef.getIdentificationBack());
        enterpriseFile.setIdentificationBackKey(hef.getIdentificationBackKey());

        enterpriseFile.setCustomerfileName(hef.getCustomerfileName());
        enterpriseFile.setCustomerfileKey(hef.getCustomerfileKey());

        enterpriseFile.setEntId(hef.getEntId());

        enterpriseFile.setContractName(hef.getContractName());
        enterpriseFile.setContractKey(hef.getContractKey());

        enterpriseFile.setAuthorizationCertificate(hef.getAuthorizationCertificate());
        enterpriseFile.setAuthorizationKey(hef.getAuthorizationKey());

        enterpriseFile.setUpdateTime(new Date());

        return enterpriseFile;
    }

    // 上传文件
    private void uploadFiles(MultipartHttpServletRequest request, HistoryEnterpriseFile hef) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        // 1、上传文件
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());

            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            if (!org.apache.commons.lang.StringUtils.isBlank(originalFilename)) {
                if (multipartFile.getName().equals("licenceImage")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setLicenceKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("authorization")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setAuthorizationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identification")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setIdentificationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identificationBack")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setIdentificationBackKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("customerFile")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setCustomerfileKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("contract")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setContractKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("image")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    hef.setImageKey(key);
                    fileStoreService.save(key, file);
                }
            }
        }
    }

    /**
     * @Title: selectHistoryEnterpriseByRequestId
     * @Description: TODO
     */
    @Override
    public HistoryEnterprises selectHistoryEnterpriseByRequestId(Long requestId) {
        if (requestId != null) {
            return mapper.selectHistoryEnterpriseByRequestId(requestId);
        }
        return null;
    }

    @Override
    public Boolean hasApprovalRecord(Long entId) {
        List<HistoryEnterprises> historyEnterprises = selectByEntIdAndStatus(entId,
                EnterpriseStatus.SUBMIT_APPROVAL.getCode());
        if (historyEnterprises != null && historyEnterprises.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<HistoryEnterprises> selectByEntIdAndStatus(Long entId, Integer status) {
        if (entId == null || status == null) {
            return null;
        }
        return mapper.selectByEntIdAndStatus(entId, status);
    }
}
