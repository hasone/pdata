package com.cmcc.vrp.province.mdrc.service.impl;

import com.cmcc.vrp.province.dao.MdrcBatchConfigMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:40:22
*/
@Service
public class MdrcBatchConfigTransactional {

    private static Logger LOGGER = Logger.getLogger(MdrcBatchConfigTransactional.class);

    @Autowired
    MdrcBatchConfigMapper mdrcBatchConfigMapper;

    @Autowired
    MdrcCardInfoService mdrcCardInfoService;

    /**
     * @param list
     * @param config
     * @throws TransactionException
     */
    @Transactional
    public void updateRecord(List<MdrcCardInfo> list, MdrcBatchConfig config)
        throws TransactionException {
        //批量更新卡密码
        mdrcCardInfoService.batchUpdatePassword(list);
        LOGGER.info("Batch update password completed...");

        //保存秘钥，文件密码
        mdrcBatchConfigMapper.updateByPrimaryKey(config);
        LOGGER.info("Update batch config completed...");
    }
}
