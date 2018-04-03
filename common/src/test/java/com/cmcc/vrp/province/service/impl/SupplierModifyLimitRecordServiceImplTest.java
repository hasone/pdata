package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierModifyLimitRecordMapper;
import com.cmcc.vrp.province.model.SupplierModifyLimitRecord;
import com.cmcc.vrp.province.service.SupplierModifyLimitRecordService;

@RunWith(MockitoJUnitRunner.class)
public class SupplierModifyLimitRecordServiceImplTest {
    
    @InjectMocks
    SupplierModifyLimitRecordService supplierModifyLimitRecordService =
    new SupplierModifyLimitRecordServiceImpl();
    
    @Mock
    SupplierModifyLimitRecordMapper mapper;
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierModifyLimitRecord.class))).thenReturn(1);
        assertTrue(supplierModifyLimitRecordService.insertSelective(new SupplierModifyLimitRecord()));
    }

}
