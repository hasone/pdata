package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.dao.AdminManagerMapper;
import com.cmcc.vrp.province.dao.ApprovalDetailDefinitionMapper;
import com.cmcc.vrp.province.dao.ApprovalProcessDefinitionMapper;
import com.cmcc.vrp.province.dao.EntManagerMapper;
import com.cmcc.vrp.province.dao.EnterpriseApprovalRecordMapper;
import com.cmcc.vrp.province.dao.EnterpriseMapper;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseApprovalRecord;
import com.cmcc.vrp.province.model.Manager;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinqinyan on 2016/9/9.
 */
@Ignore
public class ApprovalRequestTest {
    @Autowired
    ApprovalProcessDefinitionMapper approvalProcessDefinitionMapper;
    @Autowired
    ApprovalDetailDefinitionMapper approvalDetailDefinitionMapper;
    @Autowired
    EnterpriseMapper enterpriseMapper;
    @Autowired
    EnterpriseApprovalRecordMapper enterpriseApprovalRecordMapper;
    @Autowired
    EntManagerMapper entManagerMapper;
    @Autowired
    AdminManagerMapper adminManagerMapper;


    @Ignore
    @Test
    public void createApproval() throws IOException {
        FileWriter writer;
        writer = new FileWriter("approval-sql.txt");

        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionMapper
            .selectByType(ApprovalType.Enterprise_Approval.getCode());
        List<ApprovalDetailDefinition> approvalDetailDefinitions = approvalDetailDefinitionMapper
            .getByApprovalProcessId(approvalProcessDefinition.getId());

        List<Enterprise> enterprises = enterpriseMapper.getEnterprisesByStatus();

        //待审核的记录
        List<EnterpriseApprovalRecord> entApprovalRecordIsNew = new ArrayList<EnterpriseApprovalRecord>();

        if (enterprises != null) {
            //审批请求id
            Long approvalRequestId = 1L;
            //审批记录id
            Long approvalRecordId = 1L;
            Long processId = approvalProcessDefinition.getId();

            for (Enterprise enter : enterprises) {

                /**
                 * step1、创建审批请求sql，默认status=0,再最后一步时在更新status
                 * */
                //客户经理
                Manager manager = entManagerMapper.getManagerForEnter(enter.getId());
                Long adminId = adminManagerMapper.selectAdminIdByManagerId(manager.getParentId()).get(0);

                String approvalRequestSQL = MessageFormat.format("insert into approval_request (id, process_id, ent_id, creator_id," +
                        "status, create_time, update_time, delete_flag) values ({0}, {1}, {2}, {3}, '0', now(),now(),'0');",
                    approvalRequestId, processId, enter.getId(), adminId);
                writer.write(approvalRequestSQL);
                writer.write("\n");

                /**
                 * step2、创建历史审批记录sql
                 * */
                //获取审核记录
                List<EnterpriseApprovalRecord> enterpriseApprovalRecords = enterpriseApprovalRecordMapper.selectByEntId(enter.getId());

                if (enterpriseApprovalRecords != null) {
                    for (EnterpriseApprovalRecord enterpriseApprovalRecord : enterpriseApprovalRecords) {
                        if (enterpriseApprovalRecord.getIsnew().toString().equals("1")) {
                            //未审批记录,放在最后统一插入
                            entApprovalRecordIsNew.add(enterpriseApprovalRecord);
                        } else {
                            //已经审批记录
                            String approvalRecordSQL = MessageFormat.format("insert into approval_record (id, request_id, creator_id, operator_id," +
                                    "comment, description, delete_flag, create_time, update_time, is_new) values " +
                                    "({0}, {1}, {2}, {3}, {5}, '0', now(), now(), '0');",
                                approvalRecordId, approvalRequestId, enterpriseApprovalRecord.getCreatorId(), enterpriseApprovalRecord.getOperatorId(),
                                enterpriseApprovalRecord.getOperatorComment(), enterpriseApprovalRecord.getDescription());
                            writer.write(approvalRecordSQL);
                            writer.write("\n");

                            approvalRecordId++;
                        }
                    }
                }

                /**
                 * step3、创建历史审批记录sql
                 * */
                if (entApprovalRecordIsNew != null) {
                    for (EnterpriseApprovalRecord item : entApprovalRecordIsNew) {


                        if (item.getNewStatus().toString().equals("4")) {
                            //带客户经理审核
                            String approvalRecordIsNewSQL = MessageFormat.format("insert into approval_record (id, request_id, creator_id, operator_id," +
                                    "comment, description, delete_flag, create_time, update_time, is_new) values " +
                                    "({0}, {1}, {2}, {3}, {5}, '0', now(), null, '0');",
                                approvalRecordId, approvalRequestId, item.getCreatorId(), null,
                                null, item.getDescription());
                            writer.write(approvalRecordIsNewSQL);
                            writer.write("\n");
                            approvalRecordId++;

                            //更新审批请求记录sql
                            ApprovalDetailDefinition currApprovalDetailDefinition = approvalDetailDefinitions.get(0);
                            Integer currStatus = currApprovalDetailDefinition.getPrecondition();
                            String updateApprovalRequestSQL = MessageFormat.format("update approval_request set status = {0} where ent_id = {1};",
                                currStatus, item.getEntId());
                            writer.write(updateApprovalRequestSQL);
                            writer.write("\n");

                        } else if (item.getNewStatus().toString().equals("5")) {
                            //带省级管理员审核
                            String approvalRecordIsNewSQL = MessageFormat.format("insert into approval_record (id, request_id, creator_id, operator_id," +
                                    "comment, description, delete_flag, create_time, update_time, is_new) values " +
                                    "({0}, {1}, {2}, {3}, {5}, '0', now(), null, '0');",
                                approvalRecordId, approvalRequestId, item.getCreatorId(), null,
                                null, item.getDescription());
                            writer.write(approvalRecordIsNewSQL);
                            writer.write("\n");
                            approvalRecordId++;

                            //更新审批请求记录sql
                            ApprovalDetailDefinition currApprovalDetailDefinition = approvalDetailDefinitions.get(1);
                            Integer currStatus = currApprovalDetailDefinition.getPrecondition();
                            String updateApprovalRequestSQL = MessageFormat.format("update approval_request set status = {0} where ent_id = {1};",
                                currStatus, item.getEntId());
                            writer.write(updateApprovalRequestSQL);
                            writer.write("\n");

                        } else if (item.getNewStatus().toString().equals("6")) {
                            //带省级管理员审核
                            String approvalRecordIsNewSQL = MessageFormat.format("insert into approval_record (id, request_id, creator_id, operator_id," +
                                    "comment, description, delete_flag, create_time, update_time, is_new) values " +
                                    "({0}, {1}, {2}, {3}, {5}, '0', now(), null, '0');",
                                approvalRecordId, approvalRequestId, item.getCreatorId(), null,
                                null, item.getDescription());
                            writer.write(approvalRecordIsNewSQL);
                            writer.write("\n");
                            approvalRecordId++;

                            //更新审批请求记录sql
                            ApprovalDetailDefinition currApprovalDetailDefinition = approvalDetailDefinitions.get(2);
                            Integer currStatus = currApprovalDetailDefinition.getPrecondition();
                            String updateApprovalRequestSQL = MessageFormat.format("update approval_request set status = {0} where ent_id = {1};",
                                currStatus, item.getEntId());
                            writer.write(updateApprovalRequestSQL);
                            writer.write("\n");

                        } else if (item.getNewStatus().toString().equals("10")) {
                            //未填写合作信息
                            /*String approvalRecordIsNewSQL = MessageFormat.format("insert into approval_record (id, request_id, creator_id, operator_id," +
                                            "comment, description, delete_flag, create_time, update_time, is_new) values " +
                                            "({0}, {1}, {2}, {3}, {5}, '0', now(), null, '0');",
                                    approvalRecordId, approvalRequestId, item.getCreatorId(), null,
                                    null, item.getDescription());
                            writer.write(approvalRecordIsNewSQL);
                            writer.write("\n");
                            approvalRecordId++;*/
                        }
                    }

                }
            }
        }
    }
}
