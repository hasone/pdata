package com.cmcc.vrp.boss.neimenggu;

import com.cmcc.vrp.boss.neimenggu.model.query.ResponseQueryData;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sunyiwei on 2016/4/22.
 */
@Ignore
public class NMBossQueryServiceImplTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(NMBossQueryServiceImpl.class);

    @Autowired
    NMBossQueryServiceImpl nmBossQueryService;

    @Autowired
    EnterprisesService enterprisesService;

    @Ignore
    @Test
    public void testGetRecvObject() throws Exception {
        final String str = "{\"groupId\":\"471710008909\",\"groupName\":\"�˵���ҵ�����EC_\",\"balance\":\"-1048078800\",\"effDate\":\"2009-02-13\",\"status\":0,\"prodList\":[{\"prodId\":\"40006095\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����3Ԫ10MB\",\"prodType\":2},{\"prodId\":\"40006097\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����1.8Ԫ10MB\",\"prodType\":2},{\"prodId\":\"40006098\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����5Ԫ30MB\",\"prodType\":2},{\"prodId\":\"40006099\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����10Ԫ70MB\",\"prodType\":2},{\"prodId\":\"40006100\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����7Ԫ70MB\",\"prodType\":2},{\"prodId\":\"40006101\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����20Ԫ150MB\",\"prodType\":2},{\"prodId\":\"40006102\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����16Ԫ150MB\",\"prodType\":2},{\"prodId\":\"40006103\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����30Ԫ280MB\",\"prodType\":2},{\"prodId\":\"40006104\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����50Ԫ600MB\",\"prodType\":2},{\"prodId\":\"40006105\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����70Ԫ1024MB\",\"prodType\":2},{\"prodId\":\"40006106\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����100Ԫ2048MB\",\"prodType\":2},{\"prodId\":\"40006109\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-49��2TB�����ײ�300Ԫ/GB\",\"prodType\":2},{\"prodId\":\"40006110\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-122��5TB�����ײ�300Ԫ/GB\",\"prodType\":2},{\"prodId\":\"40006118\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����30Ԫ��500M\",\"prodType\":2},{\"prodId\":\"40006119\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����50Ԫ��1024M\",\"prodType\":2},{\"prodId\":\"40006120\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����70Ԫ��2048M\",\"prodType\":2},{\"prodId\":\"40006121\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����100Ԫ��3072M\",\"prodType\":2},{\"prodId\":\"40006122\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����130Ԫ��4096M\",\"prodType\":2},{\"prodId\":\"40006123\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����180Ԫ��6144M\",\"prodType\":2},{\"prodId\":\"40006124\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������ͳ��-����280Ԫ��11264M\",\"prodType\":2},{\"prodId\":\"40006301\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����������\",\"prodType\":2},{\"prodId\":\"40008918\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-10Ԫ��100M\",\"prodType\":2},{\"prodId\":\"40008919\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-20Ԫ��300M\",\"prodType\":2},{\"prodId\":\"40013142\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-1M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013143\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-2M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013144\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-5M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013145\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-10M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013146\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-20M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013147\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-50M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013148\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-100M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013149\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-200M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013150\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-500M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"40013151\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ��-1024M�������Ӱ�\",\"prodType\":2},{\"prodId\":\"80021211\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����������\",\"prodType\":2},{\"prodId\":\"40006096\",\"prodAttrType\":0,\"groupId\":0,\"prodName\":\"ͨ������-����5Ԫ30MB\",\"prodType\":2}]}";

        ResponseQueryData rqd = nmBossQueryService.getRecvObject(str);
        convertToAccount(rqd, 342L);
    }

    private Account convertToAccount(ResponseQueryData rqd, Long prdId) {
        Enterprise enterprise = enterprisesService.selectByCode(rqd.getGroupId());
        if (enterprise == null) {
            LOGGER.error("��ѯ��ҵ��Ϣ���ؿ�ֵ. EntCode = {}. ", rqd.getGroupId());
            return null;
        }

        Account account = new Account();
        account.setEnterId(enterprise.getId());
        account.setOwnerId(enterprise.getId());
        account.setProductId(prdId);
        account.setCount(NumberUtils.toDouble(rqd.getBalance()) / 100); //ԭ����BOSS�෵�ص����Է�Ϊ��λ�Ľ��

        return account;
    }
}
