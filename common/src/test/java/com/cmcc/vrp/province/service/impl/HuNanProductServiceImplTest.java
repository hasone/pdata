/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EnterprisesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

/**
 * <p>Title:HuNanProductServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月8日
 */
@RunWith(MockitoJUnitRunner.class)
public class HuNanProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl pService = new HuNanProductServiceImpl();

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    ProductMapper productMapper;

    @Test
    public void testSelectAllProductsByEnterCode() {
        String enterpriseCode = "1111111";
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        List<Product> list = new ArrayList();

        when(enterprisesService.selectByCode(enterpriseCode)).thenReturn(null).thenReturn(enterprise);
        assertNull(pService.selectAllProductsByEnterCode(null));
        assertNull(pService.selectAllProductsByEnterCode(enterpriseCode));

        when(productMapper.selectAllProductsByEnterId(Mockito.anyLong())).thenReturn(list);
        assertSame(list, pService.selectAllProductsByEnterCode(enterpriseCode));

        Mockito.verify(productMapper, Mockito.times(1)).selectAllProductsByEnterId(Mockito.anyLong());
    }

}
