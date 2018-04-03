/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.ProductOnlineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

/**
 * <p>Title:EmptyProductOnlineServiceImlTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月7日
 */
@RunWith(MockitoJUnitRunner.class)
public class EmptyProductOnlineServiceImlTest {

    @InjectMocks
    ProductOnlineService epoService = new EmptyProductOnlineServiceIml();

    @Test
    public void testIsOnlineProduct() {
        assertTrue(epoService.isOnlineProduct(1L));
    }

}
