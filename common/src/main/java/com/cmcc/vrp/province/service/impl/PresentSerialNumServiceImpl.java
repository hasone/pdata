package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.PresentSerialNumMapper;
import com.cmcc.vrp.province.model.PresentSerialNum;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 批量赠送时块操作的流水号对应关联表
 * <p>
 * Created by sunyiwei on 2016/11/1.
 */
@Service
public class PresentSerialNumServiceImpl implements PresentSerialNumService {
    @Autowired
    PresentSerialNumMapper presentSerialNumMapper;

    @Override
    public boolean batchInsert(List<PresentSerialNum> psns) {
        return validate(psns) && presentSerialNumMapper.batchInsert(psns) == psns.size();
    }

    @Override
    public boolean batchInsert(String blockSerialNum, List<String> pltSns) {
        return batchInsert(build(blockSerialNum, pltSns));
    }

    @Override
    public PresentSerialNum selectByPltSn(String pltSn) {
        return StringUtils.isBlank(pltSn) ? null : presentSerialNumMapper.getByPltSn(pltSn);
    }

    private List<PresentSerialNum> build(String blockSerialNum, List<String> pltSns) {
        if (StringUtils.isBlank(blockSerialNum) || pltSns == null || pltSns.isEmpty()) {
            return null;
        }

        List<PresentSerialNum> psns = new LinkedList<PresentSerialNum>();
        for (String pltSn : pltSns) {
            PresentSerialNum psn = new PresentSerialNum();
            psn.setBlockSerialNum(blockSerialNum);
            psn.setPlatformSerialNum(pltSn);
            psn.setCreateTime(new Date());
            psn.setUpdateTime(new Date());
            psn.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

            psns.add(psn);
        }

        return psns;
    }

    private boolean validate(List<PresentSerialNum> psns) {
        if (psns == null) {
            return false;
        }

        for (PresentSerialNum psn : psns) {
            if (!validate(psn)) {
                return false;
            }
        }

        return true;
    }

    private boolean validate(PresentSerialNum psn) {
        return psn != null
            && StringUtils.isNotBlank(psn.getBlockSerialNum())
            && StringUtils.isNotBlank(psn.getPlatformSerialNum());
    }
}
