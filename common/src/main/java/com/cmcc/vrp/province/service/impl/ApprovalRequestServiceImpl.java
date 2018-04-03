package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.InterfaceApprovalStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.enums.ProductChangeOperation;
import com.cmcc.vrp.province.dao.ApprovalRequestMapper;
import com.cmcc.vrp.province.dao.EntProductMapper;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductChangeDetail;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.SdAccApprovalRequest;
import com.cmcc.vrp.province.service.AccountChangeDetailService;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityApprovalDetailService;
import com.cmcc.vrp.province.service.AdminChangeDetailService;
import com.cmcc.vrp.province.service.AdminChangeOperatorService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EcApprovalDetailService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.ProductChangeDetailService;
import com.cmcc.vrp.province.service.ProductChangeOperatorService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinqinyan
 */
@Service("approvalRequestService")
public class ApprovalRequestServiceImpl implements ApprovalRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalRequestServiceImpl.class);
    protected String zyProvinceFlagValue = "ziying";
    @Autowired
    ApprovalRequestMapper mapper;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    EntProductMapper entProductMapper;
    @Autowired
    AccountChangeDetailService accountChangeDetailService;
    @Autowired
    AccountChangeOperatorService accountChangeOperatorService;
    @Autowired
    AccountService accountService;
    @Autowired
    AdministerService administerService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    @Autowired
    RoleService roleService;
    @Autowired
    ProductChangeDetailService productChangeDetailService;
    @Autowired
    ProductChangeOperatorService productChangeOperatorService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityApprovalDetailService activityApprovalDetailService;
    @Autowired
    HistoryEnterprisesService historyEnterprisesService;
    @Autowired
    EnterpriseFileService enterpriseFileService;
    @Autowired
    GiveMoneyEnterService giveMoneyEnterService;
    @Autowired
    EcApprovalDetailService ecApprovalDetailService;
    @Autowired
    EntCallbackAddrService entCallbackAddrService;
    @Autowired
    EntWhiteListService entWhiteListService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    MdrcActiveDetailService mdrcActiveDetailService;
    @Autowired
    ProductService productService;
    @Autowired
    AdminChangeOperatorService adminChangeOperatorService;
    @Autowired
    AdminChangeDetailService adminChangeDetailService;
    @Autowired
    ProductTemplateEnterpriseMapService productTemplateEnterpriseMapService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    AccountRecordService accountRecordService;

    @Override
    public boolean insert(ApprovalRequest record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        return mapper.insertSelective(record) == 1;
    }

    @Override
    public ApprovalRequest selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(ApprovalRequest record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        return mapper.updateApprovalRequest(record) == 1;
    }

    @Override
    public List<ApprovalRequest> getApprovalRequestList(QueryObject queryObject, List<Enterprise> enterprises,
            Long processId, List<Integer> preconditions, boolean isMakecardRole) {
        // TODO Auto-generated method stub
        if (processId == null) {
            return null;
        }

        Map map = queryObject.toMap();
        map.put("processId", processId);
        //        map.put("preconditions", preconditions);

        if (map.get("approvalType") != null) {
            String approvalType = map.get("approvalType").toString();

            if (!isMakecardRole && !approvalType.equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
                //不是制卡专员，并且不是制卡审核，企业列表为空，则直接返回
                if (enterprises == null || enterprises.size() < 1) {
                    return null;
                } else {
                    map.put("enterprises", enterprises);
                }
            }

            if (approvalType.equals(ApprovalType.Activity_Approval.getCode().toString())) {
                return mapper.getActivityApprovalRequestsByMap(map);
            } else if (approvalType.equals(ApprovalType.Admin_Change_Approval.getCode().toString())) {
                return mapper.getActivityApprovalRequestsByMap(map);
            } else if (approvalType.equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
                if (isMakecardRole) {
                    map.remove("creators");
                } else {
                    if ((map.get("creators") == null)) {
                        return null;
                    }
                }
                return mapper.getApprovalRequestForMdrcCardmake(map);
            } else {
                return mapper.getApprovalRequestForMdrcActive(map);
            }
        }

        if (enterprises == null || enterprises.size() < 1) {
            return null;
        } else {
            map.put("enterprises", enterprises);
        }
        return mapper.getApprovalRequestListByMap(map);
    }

    @Override
    public Long countApprovalRequest(QueryObject queryObject, List<Enterprise> enterprises, Long processId,
            List<Integer> preconditions, boolean isMakecardRole) {
        // TODO Auto-generated method stub
        if (processId == null || preconditions == null || preconditions.size() < 1) {
            return 0L;
        }
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();

        map.put("processId", processId);
        //        map.put("preconditions", preconditions);

        if (map.get("approvalType") != null) {
            String approvalType = map.get("approvalType").toString();

            if (!isMakecardRole && !approvalType.equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
                //不是制卡专员，并且不是制卡审核，企业列表为空，则直接返回
                if (enterprises == null || enterprises.size() < 1) {
                    return 0L;
                } else {
                    map.put("enterprises", enterprises);
                }
            }

            if (approvalType.equals(ApprovalType.Activity_Approval.getCode().toString())) {
                return mapper.countActivityApprovalRequestsByMap(map);
            } else if (approvalType.equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
                if (isMakecardRole) {
                    map.remove("creators");
                } else {
                    if ((map.get("creators") == null)) {
                        return 0L;
                    }
                }
                return mapper.countApprovalRequestForMdrcCardmake(map);
            } else {
                return mapper.countApprovalRequestForMdrcActive(map);
            }
        }
        if (enterprises == null || enterprises.size() < 1) {
            return 0L;
        } else {
            map.put("enterprises", enterprises);
        }
        return mapper.countApprovalRequestByMap(map);
    }

    /**
     * edit by qinqinyan on 2017/08/08
     * 在审批完成后直接生成卡数据
     * 生成卡数据这个任务则是直接扔到队列里面实现。
     * */
    @Override
    @Transactional
    public boolean updateLastRecordAndInsertNewRecord(ApprovalRecord updateApprovalRecord,
            ApprovalRequest updateApprovalRequest, ApprovalRecord newApprovalRecord) {
        //1、更新审批记录
        if (updateApprovalRecord != null) {
            if (!approvalRecordService.updateApprovalRecord(updateApprovalRecord)) {
                return false;
            }
        }
        //2、更新审批请求记录
        if (updateApprovalRequest != null) {
            if (!updateByPrimaryKeySelective(updateApprovalRequest)) {
                throw new RuntimeException();
            }
        }
        //3、插入新的审批记录
        if (newApprovalRecord != null) {
            if (!approvalRecordService.insertApprovalRecord(newApprovalRecord)) {
                throw new RuntimeException();
            }
        }

        //审批通过立即处理, luozuwu v12.1 add 2017/08/11
        if (!doAfterApprovaled(updateApprovalRequest.getId())) {
            throw new RuntimeException();
        }

        return true;
    }

    @Override
    @Transactional
    public boolean approvalForActivity(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord, ActivityApprovalDetail activityApprovalDetail) {
        //1、审批操作
        if (!updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest, newApprovalRecord)) {
            //LOGGER.info("更新或者插入审核记录失败!活动-"+activityApprovalDetail.getActivityId());
            return false;
        }

        //活动审核
        if (activityApprovalDetail != null) {
            Integer activityStatus = ActivityStatus.FINISH_APPROVAL.getCode();
            if (activityApprovalDetail.getApprovalStatus().equals("0")) {
                //驳回
                activityStatus = ActivityStatus.REJECT.getCode();
            }
            if (!activitiesService.changeStatus(activityApprovalDetail.getActivityId(), activityStatus)) {
                throw new RuntimeException();
            }
            return true;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean approvalForProductChange(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord, List<ProductChangeDetail> productChangeDetails) {
        //1、审批操作
        if (!updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest, newApprovalRecord)) {
            return false;
        }

        //如果是产品变更：更新产品信息
        if (productChangeDetails != null && productChangeDetails.size() > 0) {
            try {
                if (changeProductForEnterprise(updateApprovalRequest, productChangeDetails)) {
                    return true;
                }
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage());
                throw new RuntimeException();
            }

            /*List<EntProduct> addEntProductList = new ArrayList<EntProduct>();
            List<EntProduct> deleteEntProductList = new ArrayList<EntProduct>();
            List<EntProduct> discountEntProductList = new ArrayList<EntProduct>();

            for (ProductChangeDetail pco : productChangeDetails) {
                if (pco.getOperate().intValue() == ProductChangeOperation.ADD.getValue()) {
                    addEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                } else if (pco.getOperate().intValue() == ProductChangeOperation.DELETE.getValue()) {
                    deleteEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                } else if (pco.getOperate().intValue() == ProductChangeOperation.CHANGE_DISCOUNT.getValue()) {
                    discountEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                }
            }

            if (!entProductService.updateEnterProductByRecords(updateApprovalRequest.getEntId(), addEntProductList,
                    deleteEntProductList, discountEntProductList)) {
                LOGGER.info("更新产品账户失败.add: " + JSONArray.toJSONString(addEntProductList) + " ; delete: "
                        + JSONArray.toJSONString(deleteEntProductList) + " ; discount: "
                        + JSONArray.toJSONString(discountEntProductList));
                throw new RuntimeException();
            }
            
            ProductChangeDetail productChangeDetail = productChangeDetails.get(0);
            if(productChangeDetail.getOldProductTemplateId()!=null){
                ProductTemplateEnterpriseMap oldMap = new ProductTemplateEnterpriseMap();
                oldMap.setEnterpriseId(updateApprovalRequest.getEntId());
                oldMap.setProductTemplateId(productChangeDetail.getOldProductTemplateId());
                oldMap.setUpdateTime(new Date());
                oldMap.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());
                if(!productTemplateEnterpriseMapService.updateByPrimaryKeySelective(oldMap)){
                    LOGGER.info("删除模板与企业关联关系失败，企业id = {}, 产品模板id = {}", 
                            updateApprovalRequest.getEntId(), productChangeDetail.getOldProductTemplateId());
                    throw new RuntimeException();
                }
            }
            
            if(productChangeDetail.getNewProductTemplateId()!=null){
                ProductTemplateEnterpriseMap newMap = new ProductTemplateEnterpriseMap();
                newMap.setProductTemplateId(productChangeDetail.getNewProductTemplateId());
                newMap.setEnterpriseId(updateApprovalRequest.getEntId());
                newMap.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                newMap.setCreateTime(new Date());
                newMap.setUpdateTime(new Date());
                if(!productTemplateEnterpriseMapService.insertSelective(newMap)){
                    LOGGER.info("插入模板与企业关联关系失败，企业id = {}, 产品模板id = {}", 
                            updateApprovalRequest.getEntId(), productChangeDetail.getNewProductTemplateId());
                    throw new RuntimeException();
                }
            }*/
        }
        return true;
    }

    @Transactional
    private boolean changeProductForEnterprise(ApprovalRequest updateApprovalRequest,
            List<ProductChangeDetail> productChangeDetails) {
        if (productChangeDetails != null && productChangeDetails.size() > 0) {
            List<EntProduct> addEntProductList = new ArrayList<EntProduct>();
            List<EntProduct> deleteEntProductList = new ArrayList<EntProduct>();
            List<EntProduct> discountEntProductList = new ArrayList<EntProduct>();

            for (ProductChangeDetail pco : productChangeDetails) {
                if (pco.getOperate().intValue() == ProductChangeOperation.ADD.getValue()) {
                    addEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                } else if (pco.getOperate().intValue() == ProductChangeOperation.DELETE.getValue()) {
                    deleteEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                } else if (pco.getOperate().intValue() == ProductChangeOperation.CHANGE_DISCOUNT.getValue()) {
                    discountEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                }
            }

            if (!entProductService.updateEnterProductByRecords(updateApprovalRequest.getEntId(), addEntProductList,
                    deleteEntProductList, discountEntProductList)) {
                LOGGER.info("更新产品账户失败.add: " + JSONArray.toJSONString(addEntProductList) + " ; delete: "
                        + JSONArray.toJSONString(deleteEntProductList) + " ; discount: "
                        + JSONArray.toJSONString(discountEntProductList));
                throw new RuntimeException();
            }

            ProductChangeDetail productChangeDetail = productChangeDetails.get(0);
            if (productChangeDetail.getOldProductTemplateId() != null) {
                if (!productTemplateEnterpriseMapService.deleteByEntId(updateApprovalRequest.getEntId())) {
                    LOGGER.info("删除模板与企业关联关系失败，企业id = {}, 产品模板id = {}", updateApprovalRequest.getEntId(),
                            productChangeDetail.getOldProductTemplateId());
                    throw new RuntimeException();
                }
            }

            if (productChangeDetail.getNewProductTemplateId() != null) {
                ProductTemplateEnterpriseMap newMap = new ProductTemplateEnterpriseMap();
                newMap.setProductTemplateId(productChangeDetail.getNewProductTemplateId());
                newMap.setEnterpriseId(updateApprovalRequest.getEntId());
                newMap.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                newMap.setCreateTime(new Date());
                newMap.setUpdateTime(new Date());
                if (!productTemplateEnterpriseMapService.insertSelective(newMap)) {
                    LOGGER.info("插入模板与企业关联关系失败，企业id = {}, 产品模板id = {}", updateApprovalRequest.getEntId(),
                            productChangeDetail.getNewProductTemplateId());
                    throw new RuntimeException();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean submitWithoutApprovalForProductChange(ApprovalRequest approvalRequest,
            List<ProductChangeDetail> productChangeDetails) {

        if (approvalRequest == null || productChangeDetails == null || productChangeDetails.size() < 1) {
            LOGGER.info("提交审批请求的参数异常");
            return false;
        }

        if (!insert(approvalRequest)) {
            LOGGER.info("提交审批请求失败");
            return false;
        }

        /**
         * 产品变更
         * */
        for (ProductChangeDetail item : productChangeDetails) {
            item.setRequestId(approvalRequest.getId());
        }
        if (!(productChangeDetailService.batchInsert(productChangeDetails) && productChangeOperatorService
                .deleteProductChangeRecordByEntId(approvalRequest.getEntId()))) {
            throw new RuntimeException();
        } else {
            try {
                if (changeProductForEnterprise(approvalRequest, productChangeDetails)) {
                    return true;
                }
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage());
                throw new RuntimeException();
            }

            /*List<EntProduct> addEntProductList = getProductList(ProductChangeOperation.ADD.getValue(),
                    approvalRequest.getEntId(), productChangeDetails);
            List<EntProduct> deleteEntProductList = getProductList(ProductChangeOperation.DELETE.getValue(),
                    approvalRequest.getEntId(), productChangeDetails);
            List<EntProduct> discountEntProductList = getProductList(
                    ProductChangeOperation.CHANGE_DISCOUNT.getValue(), approvalRequest.getEntId(),
                    productChangeDetails);

            if (!entProductService.updateEnterProductByRecords(approvalRequest.getEntId(), addEntProductList,
                    deleteEntProductList, discountEntProductList)) {
                LOGGER.info("更新产品账户失败.add: " + JSONArray.toJSONString(addEntProductList) + " ; delete: "
                        + JSONArray.toJSONString(deleteEntProductList) + " ; discount: "
                        + JSONArray.toJSONString(discountEntProductList));
                throw new RuntimeException();
            }*/
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateLastRecordAndInsertNewRecord(ApprovalRecord updateApprovalRecord,
            ApprovalRequest updateApprovalRequest, ApprovalRecord newApprovalRecord, Enterprise enterprise,
            List<ProductChangeDetail> productChangeDetails, AccountChangeDetail accountChangeDetail,
            ActivityApprovalDetail activityApprovalDetail, HistoryEnterprises historyEnterprises,
            EnterpriseFile enterpriseFile, EntCallbackAddr entCallbackAddr, List<EntWhiteList> entWhiteLists,
            AdminChangeDetail adminChangeDetail) {

        //更新审批记录
        if (updateApprovalRecord != null) {
            if (!approvalRecordService.updateApprovalRecord(updateApprovalRecord)) {
                return false;
            }
        }
        //更新审批请求记录
        if (updateApprovalRequest != null) {
            if (!updateByPrimaryKeySelective(updateApprovalRequest)) {
                throw new RuntimeException();
            }
        }
        //插入新的审批记录
        if (newApprovalRecord != null) {
            if (!approvalRecordService.insertApprovalRecord(newApprovalRecord)) {
                throw new RuntimeException();
            }
        }
        //若果是企业开户审批：审批流程正常结束后更新企业信息
        if (enterprise != null) {
            if (!enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                throw new RuntimeException();
            }
        }

        //若是账户审核：
        if (accountChangeDetail != null) {
            LOGGER.info("企业的充值请求审批通过，开始充值. EndId = {}, PrdId = {}, Count = {}, SerialNum = {}.",
                    accountChangeDetail.getEntId(), accountChangeDetail.getProductId(), accountChangeDetail.getCount(),
                    accountChangeDetail.getSerialNum());
            Product product = productService.get(accountChangeDetail.getProductId());
            //单位换算
            Double size = accountChangeDetail.getCount();
            if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().intValue()) {//流量池产品，MB转KB
                size = SizeUnits.MB.toKB(size) * 1.0;
            } else {//资金产品、话单产品，元转分
                size = size * 100;
            }
            Account account = accountService.get(accountChangeDetail.getEntId(), accountChangeDetail.getProductId(),
                    AccountType.ENTERPRISE.getValue());
            Integer approvalType = accountChangeDetail.getChangeType();
            if (ApprovalType.Account_Change_Approval.getCode() == approvalType) {
                if (!accountService.addCount(accountChangeDetail.getEntId(), accountChangeDetail.getProductId(),
                        AccountType.ENTERPRISE, size, accountChangeDetail.getSerialNum(), "充值请求审批通过，增加余额")) {
                    LOGGER.info("企业帐户余额增加失败. EntId = {}, PrdId = {}, Count = {}, SerialNum = {}.",
                            accountChangeDetail.getEntId(), accountChangeDetail.getProductId(),
                            accountChangeDetail.getCount(), accountChangeDetail.getSerialNum());
                    throw new RuntimeException();
                }
                accountService.recoverAlert(accountChangeDetail.getEntId(), accountChangeDetail.getProductId());
            } else if (ApprovalType.Account_Min_Change_Approval.getCode() == approvalType) {
                if (accountService.updateMinCount(account.getId(), size)
                        && accountRecordService.create(buildAccountRecord(account.getEnterId(), account.getOwnerId(),
                                account.getId(), size, AccountRecordType.RESET, accountChangeDetail.getSerialNum(),
                                "审批通过，最小额度更新"))) {
                    LOGGER.info("更新账户最小额度成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                            account.getOwnerId(), account.getProductId(), size, accountChangeDetail.getSerialNum(),
                            "审批通过，最小额度更新");
                    return true;
                } else {
                    throw new RuntimeException();
                }

            } else if (ApprovalType.Account_Alert_Change_Approval.getCode() == approvalType) {
                if (accountService.updateAlertCount(account.getId(), size)
                        && accountRecordService.create(buildAccountRecord(account.getEnterId(), account.getOwnerId(),
                                account.getId(), size, AccountRecordType.RESET, accountChangeDetail.getSerialNum(),
                                "审批通过，预警值更新"))) {
                    LOGGER.info("更新账户最小额度成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                            account.getOwnerId(), account.getProductId(), size, accountChangeDetail.getSerialNum(),
                            "审批通过，预警值更新");
                    return true;
                } else {
                    throw new RuntimeException();
                }
            } else if (ApprovalType.Account_Stop_Change_Approval.getCode() == approvalType) {
                if (accountService.updateStopCount(account.getId(), size)
                        && accountRecordService.create(buildAccountRecord(account.getEnterId(), account.getOwnerId(),
                                account.getId(), size, AccountRecordType.RESET, accountChangeDetail.getSerialNum(),
                                "审批通过，暂停值更新"))) {
                    LOGGER.info("更新账户最小额度成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                            account.getOwnerId(), account.getProductId(), size, accountChangeDetail.getSerialNum(),
                            "审批通过，暂停值更新");
                    return true;
                } else {
                    throw new RuntimeException();
                }
            }
        }

        //如果是产品变更：更新产品信息
        if (productChangeDetails != null) {
            List<EntProduct> addEntProductList = new ArrayList<EntProduct>();
            List<EntProduct> deleteEntProductList = new ArrayList<EntProduct>();
            List<EntProduct> discountEntProductList = new ArrayList<EntProduct>();

            for (ProductChangeDetail pco : productChangeDetails) {
                if (pco.getOperate().intValue() == ProductChangeOperation.ADD.getValue()) {
                    addEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                } else if (pco.getOperate().intValue() == ProductChangeOperation.DELETE.getValue()) {
                    deleteEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                } else if (pco.getOperate().intValue() == ProductChangeOperation.CHANGE_DISCOUNT.getValue()) {
                    discountEntProductList.add(createEntProduct(pco, updateApprovalRequest.getEntId()));
                }
            }

            //   try {
            if (!entProductService.updateEnterProductByRecords(updateApprovalRequest.getEntId(), addEntProductList,
                    deleteEntProductList, discountEntProductList)) {
                LOGGER.info("更新产品账户失败.add: " + JSONArray.toJSONString(addEntProductList) + " ; delete: "
                        + JSONArray.toJSONString(deleteEntProductList) + " ; discount: "
                        + JSONArray.toJSONString(discountEntProductList));
                throw new RuntimeException();
            }
        }

        //若是信息变更审核
        if (historyEnterprises != null && enterpriseFile != null) {
            if (historyEnterprises.getDeleteFlag().equals(EnterpriseStatus.NORMAL.getCode())) {
                if (!enterprisesService.updateByPrimaryKeySelective(generateEnterprise(historyEnterprises))) {
                    throw new RuntimeException();
                }
                if (!enterpriseFileService.update(enterpriseFile)) {
                    throw new RuntimeException();
                }

                //自营不存在更改折扣
                if (!getProvinceFlag().equals(zyProvinceFlagValue)) {
                    if (!giveMoneyEnterService.updateByEnterId(historyEnterprises.getGiveMoneyId(),
                            historyEnterprises.getEntId())) {
                        throw new RuntimeException();
                    }
                }
                if (!historyEnterprisesService.updateStatusByPrimaryKey(historyEnterprises)) {
                    throw new RuntimeException();
                }
            } else if (historyEnterprises.getDeleteFlag().equals(EnterpriseStatus.REJECTED.getCode())) {
                if (!historyEnterprisesService.updateStatusByPrimaryKey(historyEnterprises)) {
                    throw new RuntimeException();
                }
            }
        }

        //若是企业EC信息变更
        if (entCallbackAddr != null || entWhiteLists != null) {
            if (!(entWhiteLists != null && entWhiteLists.size() > 0
                    && entWhiteListService.deleteByEntId(entWhiteLists.get(0).getEntId()) && entWhiteListService
                        .batchInsert(entWhiteLists))) {
                throw new RuntimeException("ip白名单修改失败");
            } else if (entCallbackAddr != null) {
                //如果回掉地址不为空,删除原回掉地址并插入新回掉地址
                if (!StringUtils.isEmpty(entCallbackAddr.getCallbackAddr())) {
                    if (!(entCallbackAddrService.delete(entCallbackAddr.getEntId()) && entCallbackAddrService
                            .insert(entCallbackAddr))) {
                        throw new RuntimeException("回调地址修改失败");
                    }
                } else {//回掉地址为空,删除原回掉地址
                    if (!entCallbackAddrService.delete(entCallbackAddr.getEntId())) {
                        throw new RuntimeException("新回调地址为空，原回调地址删除失败");
                    }
                }
            }

            //为初始化企业创建appkey
            Enterprise ent = enterprisesService.selectById(enterprise.getId());
            if (ent.getAppKey() == null) {
                if (!enterprisesService.createAppkey(enterprise.getId())) {
                    throw new RuntimeException("企业appkey创建失败");
                }
            }
        }

        //若是用户审批变更
        if (adminChangeDetail != null) {
            Administer admin = new Administer();
            admin.setId(adminChangeDetail.getAdminId());
            admin.setUserName(adminChangeDetail.getDestName());
            admin.setMobilePhone(adminChangeDetail.getDestPhone());
            if (!administerService.updateSelective(admin)) {
                throw new RuntimeException("用户变更失败");
            }
        }

        return true;
    }

    public String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
    }

    /**
     * 无审批流程
     */
    @Override
    @Transactional
    public boolean submitWithoutApproval(ApprovalRequest approvalRequest, Enterprise enterprise,
            AccountChangeDetail accountChangeDetail, List<ProductChangeDetail> productChangeDetails,
            AdminChangeDetail adminChangeDetail) {

        if (approvalRequest == null
                || (enterprise == null && accountChangeDetail == null && adminChangeDetail == null && (productChangeDetails == null || productChangeDetails
                        .size() < 1))) {
            return false;
        }

        if (!insert(approvalRequest)) {
            LOGGER.info("提交审批请求失败");
            return false;
        }

        /**
         * 企业开户
         * */
        if (enterprise != null) {
            if (!enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                LOGGER.info("企业开户失败. EndId = {}", enterprise.getId());
                throw new RuntimeException();
            }
        }

        /**
         * 账户变更
         * */
        if (accountChangeDetail != null) {
            accountChangeDetail.setRequestId(approvalRequest.getId());
            if (!(accountChangeDetailService.insert(accountChangeDetail) && accountChangeOperatorService
                    .deleteBySerialNum(accountChangeDetail.getSerialNum()))) {
                throw new RuntimeException();
            } else {
                LOGGER.info("企业的充值请求审批通过，开始充值. EndId = {}, PrdId = {}, Count = {}, SerialNum = {}.",
                        approvalRequest.getEntId(), accountChangeDetail.getProductId(), accountChangeDetail.getCount(),
                        accountChangeDetail.getSerialNum());
                Product product = productService.get(accountChangeDetail.getProductId());
                //单位换算
                Double size = accountChangeDetail.getCount();
                if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().intValue()) {//流量池产品，MB转KB
                    size = SizeUnits.MB.toKB(size) * 1.0;
                } else {//资金产品、话单产品，元转分
                    size = size * 100;
                }

                Account account = accountService.get(approvalRequest.getEntId(), accountChangeDetail.getProductId(),
                        AccountType.ENTERPRISE.getValue());

                Integer approvalType = accountChangeDetail.getChangeType();
                if (ApprovalType.Account_Change_Approval.getCode().equals(approvalType)) {
                    if (!accountService.addCount(approvalRequest.getEntId(), accountChangeDetail.getProductId(),
                            AccountType.ENTERPRISE, size, accountChangeDetail.getSerialNum(), "充值请求无须审批，增加余额")) {
                        LOGGER.info("企业帐户余额增加失败. EntId = {}, PrdId = {}, Count = {}, SerialNum = {}.",
                                approvalRequest.getEntId(), accountChangeDetail.getProductId(),
                                accountChangeDetail.getCount(), accountChangeDetail.getSerialNum());
                        throw new RuntimeException();
                    }
                    accountService.recoverAlert(approvalRequest.getEntId(), accountChangeDetail.getProductId());
                } else if (ApprovalType.Account_Min_Change_Approval.getCode().equals(approvalType)) {
                    if (accountService.updateMinCount(account.getId(), size)
                            && accountRecordService.create(buildAccountRecord(account.getEnterId(),
                                    account.getOwnerId(), account.getId(), size, AccountRecordType.RESET,
                                    accountChangeDetail.getSerialNum(), "无需审批，最小额度更新"))) {
                        LOGGER.info("更新账户最小额度成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                                account.getOwnerId(), account.getProductId(), size, accountChangeDetail.getSerialNum(),
                                "无需审批，最小额度更新");
                        return true;
                    } else {
                        throw new RuntimeException();
                    }

                } else if (ApprovalType.Account_Alert_Change_Approval.getCode().equals(approvalType)) {
                    if (accountService.updateAlertCount(account.getId(), size)
                            && accountRecordService.create(buildAccountRecord(account.getEnterId(),
                                    account.getOwnerId(), account.getId(), size, AccountRecordType.RESET,
                                    accountChangeDetail.getSerialNum(), "无需审批，预警值更新"))) {
                        LOGGER.info("更新账户最小额度成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                                account.getOwnerId(), account.getProductId(), size, accountChangeDetail.getSerialNum(),
                                "无需审批，预警值更新");
                        return true;
                    } else {
                        throw new RuntimeException();
                    }
                } else if (ApprovalType.Account_Stop_Change_Approval.getCode().equals(approvalType)) {
                    if (accountService.updateStopCount(account.getId(), size)
                            && accountRecordService.create(buildAccountRecord(account.getEnterId(),
                                    account.getOwnerId(), account.getId(), size, AccountRecordType.RESET,
                                    accountChangeDetail.getSerialNum(), "无需审批，暂停值更新"))) {
                        LOGGER.info("更新账户最小额度成功, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                                account.getOwnerId(), account.getProductId(), size, accountChangeDetail.getSerialNum(),
                                "无需审批，暂停值更新");
                        return true;
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    throw new RuntimeException("未知的账户变更类型：changeType = " + approvalType);
                }
            }
        }

        /**
         * 产品变更
         * */
        if (productChangeDetails != null && productChangeDetails.size() > 0) {
            for (ProductChangeDetail item : productChangeDetails) {
                item.setRequestId(approvalRequest.getId());
            }
            if (!(productChangeDetailService.batchInsert(productChangeDetails) && productChangeOperatorService
                    .deleteProductChangeRecordByEntId(approvalRequest.getEntId()))) {
                throw new RuntimeException();
            } else {

                List<EntProduct> addEntProductList = getProductList(ProductChangeOperation.ADD.getValue(),
                        approvalRequest.getEntId(), productChangeDetails);
                List<EntProduct> deleteEntProductList = getProductList(ProductChangeOperation.DELETE.getValue(),
                        approvalRequest.getEntId(), productChangeDetails);
                List<EntProduct> discountEntProductList = getProductList(
                        ProductChangeOperation.CHANGE_DISCOUNT.getValue(), approvalRequest.getEntId(),
                        productChangeDetails);

                if (!entProductService.updateEnterProductByRecords(approvalRequest.getEntId(), addEntProductList,
                        deleteEntProductList, discountEntProductList)) {
                    LOGGER.info("更新产品账户失败.add: " + JSONArray.toJSONString(addEntProductList) + " ; delete: "
                            + JSONArray.toJSONString(deleteEntProductList) + " ; discount: "
                            + JSONArray.toJSONString(discountEntProductList));
                    throw new RuntimeException();
                }

            }
        }

        /**
         * 用户变更
         */
        if (adminChangeDetail != null) {
            adminChangeDetail.setRequestId(approvalRequest.getId());
            Administer admin = new Administer();
            admin.setId(adminChangeDetail.getAdminId());
            admin.setUserName(adminChangeDetail.getDestName());
            admin.setMobilePhone(adminChangeDetail.getDestPhone());

            if (!(adminChangeDetailService.insert(adminChangeDetail)
                    && adminChangeOperatorService.deleteByAdminId(adminChangeDetail.getAdminId()) && administerService
                        .updateSelective(admin))) {
                throw new RuntimeException();
            }
        }

        return true;
    }

    private List<EntProduct> getProductList(int type, Long entId, List<ProductChangeDetail> productChangeDetails) {
        List<EntProduct> list = new ArrayList<EntProduct>();
        for (ProductChangeDetail pco : productChangeDetails) {
            if (pco.getOperate().intValue() == type) {
                list.add(createEntProduct(pco, entId));
            }
        }
        return list;
    }

    private EntProduct createEntProduct(ProductChangeDetail record, Long entId) {
        EntProduct entProduct = new EntProduct();
        entProduct.setProductId(record.getProductId());
        entProduct.setEnterprizeId(entId);
        entProduct.setDiscount(record.getDiscount());
        entProduct.setCreateTime(new Date());
        entProduct.setUpdateTime(new Date());
        if (record.getOperate().intValue() == ProductChangeOperation.DELETE.getValue()) {
            entProduct.setDeleteFlag(1);
        } else {
            entProduct.setDeleteFlag(0);
        }
        return entProduct;
    }

    @Override
    @Transactional
    public Long submitEnterpriseChange(Long entId, Long creatorId) {
        if (entId != null && creatorId != null) {
            ApprovalRequest approvalRequest = initApprovalRequest(entId, creatorId);
            ApprovalRecord approvalRecord = initApprovalRecord(approvalRequest.getProcessId(), creatorId);

            if (!insert(approvalRequest)) {
                return null;
            }
            if (approvalRecord != null) {
                approvalRecord.setRequestId(approvalRequest.getId());
                if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
                    throw new RuntimeException();
                }
            }
            return approvalRequest.getId();
        }
        return null;
    }

    private ApprovalRequest initApprovalRequest(Long entId, Long creatorId) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Ent_Information_Change_Approval.getCode());
        if (approvalProcessDefinition != null && approvalProcessDefinition.getId() != null) {
            approvalRequest.setProcessId(approvalProcessDefinition.getId());
            if(approvalProcessDefinition.getStage().intValue() == 0){
                //不需要审核，则直接审核通过
                approvalRequest.setResult(1);
            }else{
                approvalRequest.setResult(0);
            }
        }
        approvalRequest.setEntId(entId);
        approvalRequest.setCreatorId(creatorId);
        approvalRequest.setStatus(0);
        approvalRequest.setCreateTime(new Date());
        approvalRequest.setUpdateTime(new Date());
        approvalRequest.setDeleteFlag(0);
        return approvalRequest;
    }

    private ApprovalRecord initApprovalRecord(Long processId, Long creatorId) {
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Ent_Information_Change_Approval.getCode());
        if (approvalProcessDefinition.getStage().toString().equals("0")) {
            return null;
        }
        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setCreatorId(creatorId);

        ApprovalDetailDefinition approvalDetailDefinition = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, 0);
        if (approvalDetailDefinition != null && approvalDetailDefinition.getRoleId() != null) {
            Role role = roleService.getRoleById(approvalDetailDefinition.getRoleId());
            if (role != null) {
                approvalRecord.setDescription("待" + role.getName() + "审核");
            }
        }
        approvalRecord.setCreateTime(new Date());
        //approvalRecord.setUpdateTime(new Date());
        approvalRecord.setIsNew(1);
        approvalRecord.setDeleteFlag(0);
        return approvalRecord;
    }

    /**
     * 提交审批申请，
     */
    @Override
    @Transactional
    public boolean submitApproval(ApprovalRequest approvalRequest, ApprovalRecord approvalRecord,
            Enterprise enterprise, AccountChangeDetail accountChangeDetail,
            List<ProductChangeDetail> productChangeDetails, ActivityApprovalDetail activityApprovalDetail,
            AdminChangeDetail adminChangeDetail) {
        // TODO Auto-generated method stub
        if (!insert(approvalRequest)) {
            return false;
        }
        approvalRecord.setRequestId(approvalRequest.getId());
        if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
            throw new RuntimeException();
        }

        /**
         * 企业开户
         * */
        if (enterprise != null) {
            if (!enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                throw new RuntimeException();
            }
        }

        /**
         * 账户变更
         * */
        if (accountChangeDetail != null) {
            accountChangeDetail.setRequestId(approvalRequest.getId());
            if (!(accountChangeDetailService.insert(accountChangeDetail) && accountChangeOperatorService
                    .deleteBySerialNum(accountChangeDetail.getSerialNum()))) {
                throw new RuntimeException();
            }
        }

        /**
         * 产品变更
         * */
        if (productChangeDetails != null && productChangeDetails.size() > 0) {
            for (ProductChangeDetail item : productChangeDetails) {
                item.setRequestId(approvalRequest.getId());
            }
            if (!(productChangeDetailService.batchInsert(productChangeDetails) && productChangeOperatorService
                    .deleteProductChangeRecordByEntId(approvalRequest.getEntId()))) {
                throw new RuntimeException();
            }
        }

        /**
         * 营销活动
         * */
        if (activityApprovalDetail != null) {
            activityApprovalDetail.setRequestId(approvalRequest.getId());

            if (!(activityApprovalDetailService.insert(activityApprovalDetail) && activitiesService.changeStatus(
                    activityApprovalDetail.getActivityId(), ActivityStatus.SUBMIT_APPROVAL.getCode()))) {
                throw new RuntimeException();
            }
        }

        /**
         * 用户变更
         */
        if (adminChangeDetail != null) {
            adminChangeDetail.setRequestId(approvalRequest.getId());

            if (!(adminChangeDetailService.insert(adminChangeDetail) && adminChangeOperatorService
                    .deleteByAdminId(adminChangeDetail.getAdminId()))) {
                throw new RuntimeException();
            }

        }
        return true;
    }

    @Override
    public List<ApprovalRequest> selectByEntIdAndProcessId(Long entId, Long processId) {
        // TODO Auto-generated method stub
        if (entId == null || processId == null) {
            return null;
        }
        return mapper.selectByEntIdAndProcessId(entId, processId);
    }

    @Override
    public List<ApprovalRequest> queryApprovalRequestsForAccountChange(QueryObject queryObject, Long adminId,
            Integer approvalType) {
        // TODO Auto-generated method stub
        if (adminId != null && approvalType != null) {
            if (queryObject == null) {
                queryObject = new QueryObject();
            }
            Map map = queryObject.toMap();
            if (map.get("endTime") != null) {
                map.put("endTime", map.get("endTime").toString() + " 23:59:59");
            }

            //查找相应企业
            Administer administer = administerService.selectAdministerById(adminId);
            if (administer != null) {
                List<Enterprise> enterprises = enterprisesService.getAllEnterpriseListByAdminId(administer);
                if (enterprises != null && enterprises.size() > 0) {
                    //查找相应的流程
                    ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                            .selectByType(approvalType);

                    map.put("enterprises", enterprises);
                    map.put("processId", approvalProcessDefinition.getId());

                    return mapper.queryApprovalRequestsForAccountChange(map);
                }
            }
        }
        return null;
    }

    @Override
    public List<ApprovalRequest> queryRecordForAccountChange(QueryObject queryObject, Long adminId, Integer approvalType) {
        // TODO Auto-generated method stub
        if (adminId != null && approvalType != null) {
            if (queryObject == null) {
                queryObject = new QueryObject();
            }
            Map map = queryObject.toMap();
            if (map.get("endTime") != null) {
                map.put("endTime", map.get("endTime").toString() + " 23:59:59");
            }

            //查找相应企业
            Administer administer = administerService.selectAdministerById(adminId);
            if (administer != null) {
                List<Enterprise> enterprises = enterprisesService.getAllEnterpriseListByAdminId(administer);
                if (enterprises != null && enterprises.size() > 0) {
                    //查找相应的流程
                    ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                            .selectByType(approvalType);

                    map.put("enterprises", enterprises);
                    map.put("processId", approvalProcessDefinition.getId());

                    return mapper.queryRecordForAccountChange(map);
                }
            }
        }
        return null;
    }

    @Override
    public List<SdAccApprovalRequest> querySdAccountChangeRecord(QueryObject queryObject, Long adminId,
            Integer approvalType) {
        if (adminId != null && approvalType != null) {
            if (queryObject == null) {
                queryObject = new QueryObject();
            }
            Map<String, Object> map = queryObject.toMap();
            if (map.get("endTime") != null) {
                map.put("endTime", map.get("endTime").toString() + " 23:59:59");
            }

            //查找相应企业
            Administer administer = administerService.selectAdministerById(adminId);
            if (administer != null) {
                List<Enterprise> enterprises = enterprisesService.getAllEnterpriseListByAdminId(administer);
                if (enterprises != null && enterprises.size() > 0) {
                    //查找相应的流程
                    ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                            .selectByType(approvalType);

                    map.put("enterprises", enterprises);
                    map.put("processId", approvalProcessDefinition.getId());

                    return mapper.exportSDAccountChangeRecords(map);
                }
            }
        }
        return new ArrayList<SdAccApprovalRequest>();
    }

    @Override
    public Long countApprovalRequestsForAccountChange(QueryObject queryObject, Long adminId, Integer approvalType) {
        // TODO Auto-generated method stub
        if (adminId != null && approvalType != null) {
            if (queryObject == null) {
                queryObject = new QueryObject();
            }
            Map map = queryObject.toMap();
            if (map.get("endTime") != null) {
                map.put("endTime", map.get("endTime").toString() + " 23:59:59");
            }
            //查找相应企业
            Administer administer = administerService.selectAdministerById(adminId);
            if (administer != null) {
                List<Enterprise> enterprises = enterprisesService.getAllEnterpriseListByAdminId(administer);

                if (enterprises != null && enterprises.size() > 0) {
                    //查找相应的流程
                    ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                            .selectByType(approvalType);
                    map.put("enterprises", enterprises);
                    map.put("processId", approvalProcessDefinition.getId());

                    return mapper.countApprovalRequestsForAccountChange(map);
                }
            }
        }
        return 0L;
    }

    @Override
    public String getCurrentStatus(ApprovalRequest approvalRequest) {
        // TODO Auto-generated method stub
        if (approvalRequest == null || approvalRequest.getId() == null) {
            return "不需要审批";
        }
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .getApprovalProcessById(approvalRequest.getProcessId());
        if (approvalProcessDefinition.getStage().toString().equals("0")) {
            return "审批结束";
        }

        ApprovalDetailDefinition approvalDetail = approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(
                approvalRequest.getProcessId(), approvalRequest.getStatus());

        Role role = null;
        if (approvalDetail == null) {
            //完成审批
            ApprovalDetailDefinition lastApprovalDetail = approvalDetailDefinitionService
                    .getLastApprovalDetailByProccessId(approvalRequest.getProcessId());
            if ((lastApprovalDetail.getApprovalCode().intValue() + lastApprovalDetail.getPrecondition().intValue()) == approvalRequest
                    .getStatus().intValue()) {
                return "审批结束";
            } else {
                return "不需要审批";
            }
        } else {
            if (!approvalRequest.getDeleteFlag().toString().equals("1")) {
                role = roleService.getRoleById(approvalDetail.getRoleId());
                return "待" + role.getName() + "审核";
            } else {
                return "已驳回";
            }
        }
    }

    @Override
    public List<ApprovalRequest> selectByActivityId(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        return mapper.selectByActivityId(activityId);
    }

    @Override
    public List<ApprovalRequest> queryForEntChange(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();
        List<ApprovalProcessDefinition> approvalProcessList = approvalProcessDefinitionService
                .selectApprovalProcessesByType(ApprovalType.Ent_Information_Change_Approval.getCode());
        if (approvalProcessList != null && approvalProcessList.size() > 0) {
            map.put("approvalProcessList", approvalProcessList);
            return mapper.queryForEntChange(map);
        }
        return null;
    }

    @Override
    public Long countForEntChange(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();
        List<ApprovalProcessDefinition> approvalProcessList = approvalProcessDefinitionService
                .selectApprovalProcessesByType(ApprovalType.Ent_Information_Change_Approval.getCode());
        if (approvalProcessList != null && approvalProcessList.size() > 0) {
            map.put("approvalProcessList", approvalProcessList);
            return mapper.countForEntChange(map);
        }
        return 0L;
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

    /**
     * 提交EC信息审核
     * @Title: submitEcApproval 
     * @param interfaceFlag
     * @param approvalRequest
     * @param approvalRecord
     * @param ecApprovalDetail
     * @return
     * @Author: wujiamin
     * @date 2016年10月21日
     */
    @Override
    @Transactional
    public boolean submitEcApproval(Integer interfaceFlag, ApprovalRequest approvalRequest,
            ApprovalRecord approvalRecord, EcApprovalDetail ecApprovalDetail) {
        if (!insert(approvalRequest)) {
            return false;
        }

        if (approvalRecord != null) {
            approvalRecord.setRequestId(approvalRequest.getId());
            if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
                throw new RuntimeException("insertApprovalRecord失败");
            }
        }

        Enterprise enterprise = new Enterprise();
        enterprise.setId(approvalRequest.getEntId());
        if (interfaceFlag == InterfaceStatus.OPEN.getCode() || interfaceFlag == InterfaceStatus.CLOSE.getCode()) {
            enterprise.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVING.getCode());
        } else {
            enterprise.setInterfaceFlag(InterfaceStatus.APPROVING.getCode());
            enterprise.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVING.getCode());
        }

        if (!enterprisesService.updateByPrimaryKeySelective(enterprise)) {
            throw new RuntimeException("update企业interface=4失败");
        }

        if (ecApprovalDetail != null) {
            ecApprovalDetail.setRequestId(approvalRequest.getId());
            if (!ecApprovalDetailService.insert(ecApprovalDetail)) {
                throw new RuntimeException("ecApprovalDetail插入失败");
            }
        }

        return true;
    }

    @Override
    @Transactional
    public boolean submitEcWithoutApproval(Integer interfaceFlag, ApprovalRequest approvalRequest,
            ApprovalRecord approvalRecord, EcApprovalDetail ecApprovalDetail, List<String> ips) {
        approvalRequest.setResult(ApprovalRequestStatus.APPROVED.getCode());
        if (!submitEcApproval(interfaceFlag, approvalRequest, approvalRecord, ecApprovalDetail)) {
            return false;
        }
        if (!entWhiteListService.deleteByEntId(ecApprovalDetail.getEntId())
                || !entWhiteListService.insertIps(ips, ecApprovalDetail.getEntId())) {
            throw new RuntimeException("ip白名单更新失败！entId=" + ecApprovalDetail.getEntId());
        }
        if (ecApprovalDetail.getCallbackUrl() != null) {
            EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
            entCallbackAddr.setCallbackAddr(ecApprovalDetail.getCallbackUrl());
            entCallbackAddr.setEntId(ecApprovalDetail.getEntId());
            entCallbackAddr.setCreateTime(new Date());
            entCallbackAddr.setUpdateTime(new Date());
            entCallbackAddr.setDeleteFlag(0);
            if (!entCallbackAddrService.delete(ecApprovalDetail.getEntId())
                    || !entCallbackAddrService.insert(entCallbackAddr)) {
                throw new RuntimeException("回调地址更新失败！entId=" + ecApprovalDetail.getEntId());
            }
        }

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(ecApprovalDetail.getEntId());
        Enterprise ent = new Enterprise();
        ent.setId(ecApprovalDetail.getEntId());

        if (enterprise.getInterfaceFlag().equals(InterfaceStatus.APPROVING.getCode())) {
            if (!enterprisesService.createAppkey(ecApprovalDetail.getEntId())) {
                throw new RuntimeException("创建appkey及过期定时任务失败！entId=" + ecApprovalDetail.getEntId());
            }

            if (EnterpriseStatus.NORMAL.getCode().equals(enterprise.getDeleteFlag())) {//企业状态正常情况下，EC审核通过后，EC未开启状态，其他默认为关闭
                ent.setInterfaceFlag(InterfaceStatus.OPEN.getCode());
            } else {
                ent.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());
            }
            ent.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());
        } else {
            ent.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());
        }
        if (!enterprisesService.updateByPrimaryKeySelective(ent)) {
            throw new RuntimeException("企业信息更新失败！entId=" + ecApprovalDetail.getEntId());
        }

        return true;
    }

    /**
    * @Title: queryApprovalRequestsForEcChange
    * @Description: 
    */
    @Override
    public List<ApprovalRequest> queryApprovalRequestsForEcChange(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();

        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Ec_Approval.getCode());

        map.put("processId", approvalProcessDefinition.getId());

        return mapper.queryApprovalRequestsForEcChange(map);
    }

    /**
    * @Title: countApprovalRequestsForEcChange
    * @Description: 
    */
    @Override
    public Long countApprovalRequestsForEcChange(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Map map = queryObject.toMap();

        //查找相应的流程	
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Ec_Approval.getCode());
        map.put("processId", approvalProcessDefinition.getId());

        return mapper.countApprovalRequestsForEcChange(map);
    }

    /**
     * 根据审批类型和企业ID获取记录，并安装创建时间的倒序排序
     * @Title: selectByEntIdAndProcessType 
     * @param entId
     * @param type
     * @return
     * @Author: wujiamin
     * @date 2016年10月25日
     */
    @Override
    public List<ApprovalRequest> selectByEntIdAndProcessType(Long entId, Integer type) {
        if (entId == null || type == null) {
            return null;
        }
        Map map = new HashMap();
        map.put("entId", entId);
        map.put("type", type);
        return mapper.selectByEntIdAndProcessType(map);
    }

    /**
     * 活动提交审批请求
     * @param approvalRequest
     * @param approvalRecord
     * @param activityApprovalDetail
     * @author qinqinyan
     * */
    @Override
    @Transactional
    public boolean submitApprovalForActivity(ApprovalRequest approvalRequest, ApprovalRecord approvalRecord,
            ActivityApprovalDetail activityApprovalDetail) {
        if (!insert(approvalRequest)) {
            return false;
        }

        approvalRecord.setRequestId(approvalRequest.getId());
        if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
            throw new RuntimeException();
        }

        activityApprovalDetail.setRequestId(approvalRequest.getId());
        if (!(activityApprovalDetailService.insert(activityApprovalDetail) && activitiesService.changeStatus(
                activityApprovalDetail.getActivityId(), ActivityStatus.SUBMIT_APPROVAL.getCode()))) {
            throw new RuntimeException();
        }
        return true;
    }

    /**
     * 获取营销模板激活审批请求列表
     * @param queryObject
     * @author qinqinyan
     * */
    @Override
    public List<ApprovalRequest> getApprovalRequestForMdrcActive(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        return mapper.getApprovalRequestForMdrcActive(queryObject.toMap());
    }

    /**
     * 获取营销模板制卡审批请求列表
     * @param queryObject
     * @author qinqinyan
     * */
    @Override
    public List<ApprovalRequest> getApprovalRequestForMdrcCardmake(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        return mapper.getApprovalRequestForMdrcCardmake(queryObject.toMap());
    }

    /**
     * 计算营销模板激活审批请求个数
     * @param queryObject
     * @author qinqinyan
     * */
    @Override
    public Long countApprovalRequestForMdrcActive(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        return mapper.countApprovalRequestForMdrcActive(queryObject.toMap());
    }

    /**
     * 计算营销模板制卡审批请求个数
     * @param queryObject
     * @author qinqinyan
     * */
    @Override
    public Long countApprovalRequestForMdrcCardmake(QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        return mapper.countApprovalRequestForMdrcCardmake(queryObject.toMap());
    }

    /**
     * 提交营销卡激活审批申请
     * @param approvalRequest
     * @param mdrcActiveDetail
     * @return
     * @author qinqinyan
     * */
    @Override
    @Transactional
    public boolean submitApprovalForMdrcActive(ApprovalRequest approvalRequest, MdrcActiveDetail mdrcActiveDetail)
            throws RuntimeException {
        if (!insert(approvalRequest)) {
            return false;
        }

        mdrcActiveDetail.setRequestId(approvalRequest.getId());
        mdrcActiveDetail.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        if (!mdrcActiveDetailService.insertSelective(mdrcActiveDetail)) {
            throw new RuntimeException();
        }

        if (approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
            //需要审核
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
        record.setDescription(getCurrentStatus(approvalRequest));
        record.setCreateTime(new Date());
        //record.setUpdateTime(new Date());
        record.setIsNew(1);
        record.setDeleteFlag(0);
        return record;
    }

    @Override
    @Transactional
    public boolean editApprovalForMdrcActive(ApprovalRequest approvalRequest, MdrcActiveDetail mdrcActiveDetail) {
        if (mdrcActiveDetail != null && mdrcActiveDetail.getRequestId() != null) {
            /**
             * 1、更新approvalRequest的updateTime
             * 2、更新mdrcActiveDetail
             * 3、插入新的approvalRecord
             * */
            if (!updateByPrimaryKeySelective(approvalRequest)) {
                return false;
            }

            if (!mdrcActiveDetailService.updateByRequestIdSelective(mdrcActiveDetail)) {
                throw new RuntimeException();
            }

            if (approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
                //需要审核
                ApprovalRecord approvalRecord = createApprovalRecord(approvalRequest);
                if (!approvalRecordService.insertApprovalRecord(approvalRecord)) {
                    throw new RuntimeException();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 审批营销卡激活
     * @param mdrcActiveDetail
     * @param newApprovalRecord
     * @param updateApprovalRecord
     * @param updateApprovalRequest
     * @author qinqinyan
     * */
    @Override
    @Transactional
    public boolean approvalForMdrcActive(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord, MdrcActiveDetail mdrcActiveDetail) {
        //1、审批操作
        /*if(!updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest, newApprovalRecord)){
            return false;
        }*/
        return true;
    }

    /**
     * 审核营销卡制卡审批
     * @param newApprovalRecord
     * @param updateApprovalRecord
     * @param updateApprovalRequest
     * @author qinqinyan
     * */
    @Override
    @Transactional
    public boolean approvalForMdrcCardmake(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord) {
        /*if(!updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest, newApprovalRecord)){
            return false;
        }*/
        return true;
    }

    @Override
    public List<ApprovalRequest> getRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.getRecords(queryObject.toMap());
    }

    @Override
    public List<ApprovalRequest> getECRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.getECRecords(queryObject.toMap());
    }

    @Override
    public Long countRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.countRecords(queryObject.toMap());
    }

    @Override
    public Long countECRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.countECRecords(queryObject.toMap());
    }

    @Override
    public List<ApprovalRequest> getMakecardRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.getMakecardRecords(queryObject.toMap());
    }

    @Override
    public Long countMakecardRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.countMakecardRecords(queryObject.toMap());
    }

    /**
     * 
     * @Title: doAfterApproval 
     * @Description: 审核通过后续操作
     * @param requestId
     * @return
     * @return: boolean
     */
    private boolean doAfterApprovaled(Long requestId) {
        ApprovalRequest request = approvalRequestService.getById(requestId);
        if (request == null || request.getType() == null
                || !ApprovalRequestStatus.APPROVED.getCode().equals(request.getResult())) {
            LOGGER.info("该审批类型不存在或非审核通过状态：requestId= {},request = {}. 不做审核通过后续处理.", requestId,
                    new Gson().toJson(request));
            return true;
        }

        ApprovalType approvalType = ApprovalType.getByCode(request.getType());
        if (approvalType == null) {

        } else if (ApprovalType.Enterprise_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.Product_Change_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.Account_Change_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.Activity_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.Ent_Information_Change_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.Ec_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.MDRC_Active_Approval.getCode().equals(approvalType.getCode())) {//营销卡激活审批流程

            return afterMdrcActiveApproval(requestId);

        } else if (ApprovalType.MDRC_MakeCard_Approval.getCode().equals(approvalType.getCode())) {

        } else if (ApprovalType.Admin_Change_Approval.getCode().equals(approvalType.getCode())) {

        }

        return true;
    }

    @Override
    public ApprovalRequest getById(Long id) {
        return mapper.getById(id);
    }

    /**
     * 
     * @Title: afterMdrcActiveApproval 
     * @Description: 审批结束后的后续处理
     * @param requestId
     * @return
     * @return: boolean
     */
    private boolean afterMdrcActiveApproval(Long requestId) {
        //获取指定激活信息，包括企业、产品、模板、激活数量
        MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);
        if (!validateMdrcActiveDetail(mdrcActiveDetail)) {
            LOGGER.info("激活卡信息失败，校验不通过：激活信息mdrcActiveDetail={}。", new Gson().toJson(mdrcActiveDetail));
            return false;
        }

        //企业编码、产品信息校验,海南营销卡不校验企业和产品信息
        if (!getProvinceFlag().equals("hainan")
                && StringUtils.isNotBlank(entProductService.validateEntAndPrd(mdrcActiveDetail.getEntId(),
                        mdrcActiveDetail.getProductId()))) {
            LOGGER.info("激活卡信息失败，企业产品信息校验不通过：激活信息mdrcActiveDetail={}。", new Gson().toJson(mdrcActiveDetail));
            return false;
        }

        //获取已入库的卡号
        List<String> cardNumberList = mdrcCardInfoService.getCardNumbersByCount(mdrcActiveDetail.getConfigId(),
                Long.valueOf(mdrcActiveDetail.getCount()), MdrcCardStatus.STORED.getCode());
        if (cardNumberList.size() != mdrcActiveDetail.getCount()) {
            LOGGER.info("激活卡信息失败，激活数量大于可激活的卡号数量：激活信息mdrcActiveDetail={}。", new Gson().toJson(mdrcActiveDetail));
            return false;
        }

        //批量激活卡号
        String startSerial = cardNumberList.get(0);
        String endSerial = cardNumberList.get(cardNumberList.size() - 1);
        return mdrcCardInfoService.activeRange(mdrcActiveDetail.getConfigId(), startSerial, endSerial,
                mdrcActiveDetail.getEntId(), mdrcActiveDetail.getProductId());

    }

    /**
     * 
     * @Title: validateMdrcActiveDetail 
     * @Description: 激活之前卡信息校验，激活需要卡号、企业信息、产品信息
     * @param mdrcActiveDetail
     * @return
     * @return: boolean
     */
    private boolean validateMdrcActiveDetail(MdrcActiveDetail mdrcActiveDetail) {
        if (mdrcActiveDetail == null || mdrcActiveDetail.getConfigId() == null || mdrcActiveDetail.getCount() == null
                || mdrcActiveDetail.getEntId() == null || mdrcActiveDetail.getProductId() == null) {
            return false;
        }
        return true;

    }

    @Override
    public List<ApprovalRequest> getMdrcActiveRecords(Map map) {
        return mapper.getMdrcActiveRecords(map);
    }

    @Override
    public Long countMdrcActiveRecords(Map map) {
        return mapper.countMdrcActiveRecords(map);
    }

    @Override
    public boolean isOverAuth(Long currentUserId, Long requestId) {
        if (currentUserId == null || requestId == null) {
            return true;
        }

        Manager fatherManager = managerService.getManagerByAdminId(currentUserId);
        if (fatherManager == null) {
            return true;
        }
        //省级管理员、市级管理员、客户经理、企业管理员需要校验子父节点关系,其他节点不需要校验
        if (!managerService.isProOrCityOrMangerOrEnt(fatherManager.getRoleId())) {
            return false;
        }

        ApprovalRequest request = mapper.getById(requestId);
        if (request == null || request.getCreatorId() == null) {
            return true;
        }

        //该规则是当前用户节点的子节点用户创建时，不越权
        Manager sonManager = managerService.getManagerByAdminId(request.getCreatorId());
        if (sonManager != null
                && (managerService.isParentManage(sonManager.getId(), fatherManager.getId()) || managerService
                        .isParentManage(fatherManager.getId(), sonManager.getId()))) {
            return false;
        }
        return true;
    }

    private AccountRecord buildAccountRecord(Long entId, Long ownerId, Long accountId, Double delta,
            AccountRecordType type, String serialNum, String description) {
        AccountRecord accountRecord = new AccountRecord();

        accountRecord.setEnterId(entId);
        accountRecord.setOwnerId(ownerId);
        accountRecord.setAccountId(accountId);
        accountRecord.setType(type.getValue());
        accountRecord.setSerialNum(serialNum);
        accountRecord.setCount(delta);
        accountRecord.setAppKey(null);
        accountRecord.setDescription(description);
        accountRecord.setCreateTime(new Date());
        accountRecord.setUpdateTime(new Date());
        accountRecord.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());
        return accountRecord;
    }

    @Override
    public List<ApprovalRequest> getApprovalRequests(Long entId, Integer approvalType, Integer status) {
        if (entId == null || approvalType == null || status == null) {
            return null;
        }
        return mapper.getApprovalRequests(entId, approvalType, status);
    }

    @Override
    public List<ApprovalRequest> getMakecardRecordsOrderBy(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.getMakecardRecordsOrderBy(queryObject.toMap());
        //return null;
    }

}
