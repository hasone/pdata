package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.util.Constants;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * 企业扩展信息的UT， 使用Spring方式
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
@Ignore
public class EnterprisesExtInfoServiceImplTest2 extends BaseTest {
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;

    /**
     * 根据企业ID获取企业扩展信息UT
     *
     * @throws Exception
     */
    @Test
    @Rollback
    public void test() throws Exception {
        Long entId = new Random().nextLong();
        EnterprisesExtInfo enterprisesExtInfo = validEei(entId);

        //get
        assertNull(enterprisesExtInfoService.get(entId));

        //insert first
        assertTrue(enterprisesExtInfoService.insert(enterprisesExtInfo));

        //then get
        assertNotNull(enterprisesExtInfoService.get(entId));

        //then update
        enterprisesExtInfo.setEcCode(randStr());
        enterprisesExtInfo.setEcPrdCode(randStr());
        assertTrue(enterprisesExtInfoService.update(enterprisesExtInfo));

        //then delete
        assertTrue(enterprisesExtInfoService.delete(entId));
    }

    private EnterprisesExtInfo validEei(Long entId) {
        EnterprisesExtInfo eei = new EnterprisesExtInfo();
        eei.setEnterId(entId);
        eei.setEcCode(randStr());
        eei.setEcPrdCode(randStr());
        eei.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return eei;
    }

    private EnterprisesExtInfo nullEnterId() {
        EnterprisesExtInfo eei = validEei(343L);
        eei.setEnterId(null);

        return eei;
    }
}