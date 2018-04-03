package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierProdModifyLimitRecordMapper;
import com.cmcc.vrp.province.model.SupplierProdModifyLimitRecord;
import com.cmcc.vrp.province.service.SupplierProdModifyLimitRecordService;

@RunWith(MockitoJUnitRunner.class)
public class SupplierProdModifyLimitRecordServiceImplTest {
    @InjectMocks
    SupplierProdModifyLimitRecordService supplierProdModifyLimitRecordService
    = new SupplierProdModifyLimitRecordServiceImpl();
    
    @Mock
    SupplierProdModifyLimitRecordMapper mapper;
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierProdModifyLimitRecord.class))).thenReturn(1);
        assertTrue(supplierProdModifyLimitRecordService.insertSelective(new SupplierProdModifyLimitRecord()));
    }
    

}
