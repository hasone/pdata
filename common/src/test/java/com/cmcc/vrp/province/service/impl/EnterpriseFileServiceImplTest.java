/**
 *
 */
package com.cmcc.vrp.province.service.impl;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.EnterpriseFileMapper;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.service.EnterpriseFileService;

/**
 * <p>Title:EnterpriseFileServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年10月28日
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterpriseFileServiceImplTest {

    @InjectMocks
    EnterpriseFileService efService = new EnterpriseFileServiceImpl();

    @Mock
    EnterpriseFileMapper enterpriseFileMapper;

    @Test
    public void testInsert() {
        EnterpriseFile enterpriseFile = new EnterpriseFile();
        int result = 1;
        when(enterpriseFileMapper.insert(Mockito.any(EnterpriseFile.class))).thenReturn(result);
        assertSame(result, efService.insert(enterpriseFile));

        Mockito.verify(enterpriseFileMapper, Mockito.times(1)).insert(Mockito.any(EnterpriseFile.class));
    }

    @Test
    public void testSelectByEntId() {
        Long entId = 1L;
        EnterpriseFile enterpriseFile = new EnterpriseFile();

        when(enterpriseFileMapper.selectByEntId(entId)).thenReturn(enterpriseFile);
        assertSame(enterpriseFile, efService.selectByEntId(entId));

        Mockito.verify(enterpriseFileMapper, Mockito.times(1)).selectByEntId(entId);
    }

    @Test
    public void testUpdate() {
        EnterpriseFile enterpriseFile = new EnterpriseFile();
        when(enterpriseFileMapper.update(enterpriseFile)).thenReturn(0).thenReturn(1);
        assertFalse(efService.update(enterpriseFile));
        assertTrue(efService.update(enterpriseFile));

        Mockito.verify(enterpriseFileMapper, Mockito.times(2)).update(enterpriseFile);
    }

    @Test
    public void testDeleteEnterpriseVerifyFiles() {
        Long entId = 1L;
        when(enterpriseFileMapper.deleteEnterpriseVerifyFiles(entId)).thenReturn(true);
        assertTrue(efService.deleteEnterpriseVerifyFiles(entId));

        Mockito.verify(enterpriseFileMapper, Mockito.times(1)).deleteEnterpriseVerifyFiles(entId);
    }
}
