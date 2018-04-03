package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountChangeApprovalRecordMapper;
import com.cmcc.vrp.province.model.AccountChangeApprovalRecord;
import com.cmcc.vrp.province.service.AccountChangeApprovalRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunyiwei on 2016/4/19.
 */
@Service("accountChangeApprovalRecordService")
public class AccountChangeApprovalRecordServiceImpl implements AccountChangeApprovalRecordService {
    @Autowired
    AccountChangeApprovalRecordMapper acarm;

    @Override
    public boolean insert(AccountChangeApprovalRecord acar) {
        return validate(acar)
            && acarm.insert(acar) == 1;
    }

    @Override
    public AccountChangeApprovalRecord get(Long id) {
        if (id == null) {
            return null;
        }

        return acarm.get(id);
    }

    @Override
    public List<AccountChangeApprovalRecord> getByRequestId(Long requestId) {
        if (requestId == null) {
            return null;
        }

        return acarm.getByRequestId(requestId);
    }

    private boolean validate(AccountChangeApprovalRecord acar) {
        return acar != null
            && acar.getAccountChangeRequestId() != null
            && acar.getOperatorId() != null
            && StringUtils.isNotBlank(acar.getSerialNum())
            && acar.getOperatorResult() != null;
    }
}
