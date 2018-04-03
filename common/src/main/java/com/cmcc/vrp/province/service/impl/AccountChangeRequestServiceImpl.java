package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountChangeApprovalOperationResult;
import com.cmcc.vrp.enums.AccountChangeRequestStatus;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.dao.AccountChangeRequestMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.model.AccountChangeApprovalRecord;
import com.cmcc.vrp.province.model.AccountChangeRequest;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountChangeApprovalRecordService;
import com.cmcc.vrp.province.service.AccountChangeRequestService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

/**
 * Created by sunyiwei on 2016/4/19.
 */
@Service("accountChangeRequestService")
public class AccountChangeRequestServiceImpl implements AccountChangeRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountChangeRequestServiceImpl.class);

    @Autowired
    AccountChangeRequestMapper acrm;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    AccountChangeApprovalRecordService approvalRecordService;

    @Autowired
    AccountService accountService;
    
    @Autowired
    ProductService productService;

    @Override
    public boolean insert(AccountChangeRequest acr) {
        return validate(acr)
            && acrm.insert(acr) == 1;
        //&& insertApprovalRecord(acr);  //插入相应的审批记录，如果有必要的话
    }

    @Override
    public boolean commit(Long id, Long operatorId, String comment, String serialNum) {
        return updateStatus(id, operatorId, comment, serialNum,
            AccountChangeRequestStatus.APPROVING_ONE, AccountChangeApprovalOperationResult.COMMIT);
    }

    @Override
    public List<AccountChangeRequest> query(QueryObject queryObject) {
        Map map = queryObject.toMap();

        if (map.get("districtId") != null) {
            List<Long> districtIds = districtMapper.selectNodeById(Long.parseLong((String) map.get("districtId")));
            map.put("districtIds", districtIds);
        }
        
        if (map.get("status") != null) {
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }

        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }

        return acrm.query(map);
    }

    @Override
    public int queryCount(QueryObject queryObject) {
        Map map = queryObject.toMap();

        if (map.get("districtId") != null) {
            List<Long> districtIds = districtMapper.selectNodeById(Long.parseLong((String) map.get("districtId")));
            map.put("districtIds", districtIds);
        }
        
        if (map.get("status") != null) {
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));
        }

        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        return acrm.queryCount(map);
    }

    @Override
    public AccountChangeRequest get(Long id) {
        return acrm.get(id);
    }

    @Override
    public boolean approval(Long id, Long operatorId, String comment, String serialNum) {
        AccountChangeRequest acr = acrm.get(id);
        AccountChangeRequestStatus acrs = null;
        if (acr.getStatus() == AccountChangeRequestStatus.APPROVING_ONE.getValue()) {
            acrs = AccountChangeRequestStatus.APPROVING_TWO;
        }
        if (acr.getStatus() == AccountChangeRequestStatus.APPROVING_TWO.getValue()) {
            acrs = AccountChangeRequestStatus.APPROVING_THREE;
        }
        if (acr.getStatus() == AccountChangeRequestStatus.APPROVING_THREE.getValue()) {
            acrs = AccountChangeRequestStatus.APPROVED;
        }

        //审批通过了，可以充值了
        boolean flag = updateStatus(id, operatorId, comment, serialNum, acrs, AccountChangeApprovalOperationResult.APPROVAL);
        if (flag && acrs == AccountChangeRequestStatus.APPROVED) { //更新审批记录成功并且审批通过了，就可以加钱了
            Long ownerId = acr.getEntId();
            Long prdId = acr.getPrdId();
            Product product = productService.get(prdId);
            Double count = 0.0;
            if(product != null){
                if(ProductType.FLOW_ACCOUNT.getValue() == product.getType().byteValue()){//流量池账户，MB转换为KB
                    count = SizeUnits.MB.toKB(acr.getCount()) * 1.0;
                }else{//资金账户、话费账户，元转分
                    count = acr.getCount() * 100;//元转换为分
                }
            }                      
            LOGGER.info("企业的充值请求审批通过，开始充值. EndId = {}, PrdId = {}, Count = {}, SerialNum = {}.", ownerId, prdId, count, serialNum);
            if (accountService.addCount(ownerId, prdId, AccountType.ENTERPRISE, count, serialNum, "充值请求审批通过，增加余额")) {
                accountService.recoverAlert(ownerId, prdId);
                LOGGER.info("企业帐户余额增加成功. EntId = {}, PrdId = {}, Count = {}, SerialNum = {}.", ownerId, prdId, count, serialNum);
                return true;
            } else {
                LOGGER.info("企业帐户余额增加失败. EntId = {}, PrdId = {}, Count = {}, SerialNum = {}.", ownerId, prdId, count, serialNum);
                return false;
            }
        }

        return flag;
    }

    @Override
    public boolean reject(Long id, Long operatorId, String comment, String serialNum) {
        return updateStatus(id, operatorId, comment, serialNum,
            AccountChangeRequestStatus.REJECT, AccountChangeApprovalOperationResult.REJECT);
    }

    private boolean updateStatus(Long id, Long operatorId, String comment, String serialNum,
                                 AccountChangeRequestStatus newStatus,
                                 AccountChangeApprovalOperationResult operationResult) {
        if (id == null
            || operatorId == null
            || StringUtils.isBlank(comment)
            || StringUtils.isBlank(serialNum)) {
            return false;
        }

        //1. 更新状态
        //2. 增加审批记录
        return acrm.updateStatus(id, newStatus.getValue(), newStatus.getDesc(), operatorId) == 1
            && approvalRecordService.insert(build(id, operatorId, comment, serialNum, operationResult));
    }

    private boolean validate(AccountChangeRequest acr) {
        return acr != null
            && acr.getCount() > 0
            && acr.getAccountId() != null
            && acr.getEntId() != null
            && acr.getPrdId() != null
            && acr.getCreatorId() != null
            && StringUtils.isNotBlank(acr.getSerialNum())
            && acr.getStatus() != null
            && (acr.getStatus() == AccountChangeRequestStatus.SAVED.getValue() //新增的申请状态只能是草稿或者已提交状态
            || acr.getStatus() == AccountChangeRequestStatus.APPROVING_ONE.getValue());
    }

    private AccountChangeApprovalRecord build(Long requestId, Long operatorId, String operatorComment,
                                              String serialNum, AccountChangeApprovalOperationResult operationResult) {
        AccountChangeApprovalRecord acar = new AccountChangeApprovalRecord();

        acar.setAccountChangeRequestId(requestId);
        acar.setOperatorId(operatorId);
        acar.setOperatorComment(operatorComment);
        acar.setSerialNum(serialNum);
        acar.setOperatorResult(operationResult.getValue());
        acar.setCreateTime(new Date());
        acar.setUpdateTime(new Date());
        acar.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());

        return acar;
    }

    @Override
    public boolean updateCount(Long accountChangeRequestId, Double deltaCount, Long operatorId) {
        return acrm.updateCount(accountChangeRequestId, deltaCount,
            AccountChangeRequestStatus.APPROVING_ONE.getValue(),
            AccountChangeRequestStatus.APPROVING_ONE.getDesc(),
            operatorId) > 0;
    }
}
