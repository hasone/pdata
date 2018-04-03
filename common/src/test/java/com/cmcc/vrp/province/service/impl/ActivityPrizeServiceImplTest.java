/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.enums.PrizeType;
import com.cmcc.vrp.province.dao.ActivityPrizeMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月20日
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityPrizeServiceImplTest {

    @InjectMocks
    ActivityPrizeService activityPrizeService = new ActivityPrizeServiceImpl();

    @Mock
    ActivityPrizeMapper activityPrizeMapper;
    @Mock
    ProductService productService;
    @Mock
    ActivitiesService activitiesService;

    @Test
    public void testDelete() {

        Long id = 1L;
        when(activityPrizeMapper.deleteByPrimaryKey(id)).thenReturn(1);

        assertFalse(activityPrizeService.delete(null));
        assertTrue(activityPrizeService.delete(id));
        verify(activityPrizeMapper).deleteByPrimaryKey(id);

    }

    @Test
    public void testDelete2() {

        Long id = 1L;
        when(activityPrizeMapper.deleteByPrimaryKey(id)).thenReturn(0);

        assertFalse(activityPrizeService.delete(id));
        verify(activityPrizeMapper).deleteByPrimaryKey(id);

    }

    @Test
    public void testInsert() {

        ActivityPrize activityPrize = new ActivityPrize();
        when(activityPrizeMapper.insert(activityPrize)).thenReturn(1);

        assertTrue(activityPrizeService.insert(activityPrize));
        verify(activityPrizeMapper).insert(activityPrize);

    }

    @Test
    public void testInsert2() {

        ActivityPrize activityPrize = new ActivityPrize();
        when(activityPrizeMapper.insert(activityPrize)).thenReturn(0);


        assertFalse(activityPrizeService.insert(null));
        assertFalse(activityPrizeService.insert(activityPrize));
        verify(activityPrizeMapper).insert(activityPrize);

    }

    @Test
    public void testSelectByPrimaryKey() {

        Long id = 1L;
        ActivityPrize activityPrize = new ActivityPrize();
        when(activityPrizeMapper.selectByPrimaryKey(id)).thenReturn(activityPrize);

        assertNull(activityPrizeService.selectByPrimaryKey(null));
        assertNotNull(activityPrizeService.selectByPrimaryKey(id));
        verify(activityPrizeMapper).selectByPrimaryKey(id);

    }

    @Test
    public void testUpdateByPrimaryKey() {

        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setId(1L);
        when(activityPrizeMapper.updateByPrimaryKey(activityPrize)).thenReturn(1);

        assertTrue(activityPrizeService.updateByPrimaryKey(activityPrize));
        verify(activityPrizeMapper).updateByPrimaryKey(activityPrize);

    }

    @Test
    public void testUpdateByPrimaryKey2() {

        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setId(1L);
        when(activityPrizeMapper.updateByPrimaryKey(activityPrize)).thenReturn(0);

        assertFalse(activityPrizeService.updateByPrimaryKey(null));
        assertFalse(activityPrizeService.updateByPrimaryKey(activityPrize));
        verify(activityPrizeMapper).updateByPrimaryKey(activityPrize);

    }

    /**
     * selectByActivityId
     * @author qinqinyan
     * */
    @Test
    public void testSelectByActivityId() {
        //invalid
        assertNull(activityPrizeService.selectByActivityId(null));

        //invalid
        String activityId = "test";
        Mockito.when(activityPrizeMapper.getDetailByActivityId(anyString())).thenReturn(new ArrayList<ActivityPrize>());
        assertNotNull(activityPrizeService.selectByActivityId(activityId));
        verify(activityPrizeMapper).getDetailByActivityId(anyString());
    }
    /**
     * selectByActivityIdForIndividual
     * @author qinqinyan
     * */
    @Test
    public void testSelectByActivityIdForIndividual(){
        //invalid
        assertNull(activityPrizeService.selectByActivityIdForIndividual(null));

        //valid
        String activityId = "test";
        Mockito.when(activityPrizeMapper.selectByActivityIdForIndividual(anyString())).thenReturn(new ArrayList<ActivityPrize>());
        assertNotNull(activityPrizeService.selectByActivityIdForIndividual(activityId));
        verify(activityPrizeMapper).selectByActivityIdForIndividual(anyString());
    }

    /**
     * batchInsert
     * @author qinqinyan
     * */
    @Test
    public void testBatchInsert() {
        List<ActivityPrize> records = new ArrayList<ActivityPrize>();
        //invalid
        assertFalse(activityPrizeService.batchInsert(records));

        //valid
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setActivityId("test");
        records.add(activityPrize);

        Mockito.when(activityPrizeMapper.batchInsert(anyMap())).thenReturn(1);
        assertTrue(activityPrizeService.batchInsert(records));

        Mockito.verify(activityPrizeMapper).batchInsert(anyMap());
    }

    @Test
    public void testDeleteByActivityId() {
        //invalid
        assertFalse(activityPrizeService.deleteByActivityId(null));

        //valid
        String activityId = "aaa";
        when(activityPrizeMapper.deleteByActivityId(activityId)).thenReturn(1);

        assertTrue(activityPrizeService.deleteByActivityId(activityId));
        verify(activityPrizeMapper).deleteByActivityId(activityId);
    }

    @Test
    public void testGetDetailByActivityId() {

        String activityId = "aaa";
        List<ActivityPrize> activityPrizeList = new ArrayList();
        when(activityPrizeMapper.getDetailByActivityId(activityId)).thenReturn(activityPrizeList);

        assertNull(activityPrizeService.getDetailByActivityId(null));
        assertNotNull(activityPrizeService.getDetailByActivityId(activityId));
        verify(activityPrizeMapper).getDetailByActivityId(activityId);
    }

    /**
     * batchInsertForFlowCard
     * @author qinqinyan
     * */
    @Test
    public void testBatchInsertForFlowCard(){
        //init
        Activities activities = initActivities();

        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String cmMobileList = "18867103698";
        String cuMobileList = "18867101234";
        String ctMobileList = "";

        Product cmProduct = initProduct(cmProductId);
        cmProduct.setType(PrizeType.FLOWPACKAGE.getType());
        Product cuProduct = initProduct(cuProductId);
        cuProduct.setType(PrizeType.FLOWPOOL.getType());
        Product ctProduct = initProduct(ctProductId);
        ctProduct.setType(PrizeType.FLOWPOOL.getType());

        //invalid
        assertFalse(activityPrizeService.batchInsertForFlowCard(null,
                cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList));

        //valid
        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);
        Mockito.when(activityPrizeMapper.batchInsert(anyMap())).thenReturn(3);

        assertTrue(activityPrizeService.batchInsertForFlowCard(activities,
                cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList));

        Mockito.verify(productService, times(3)).selectProductById(anyLong());
        Mockito.verify(activityPrizeMapper).batchInsert(anyMap());
    }

    /**
     * batchInsertForFlowCard
     * @author qinqinyan
     * */
    @Test
    public void testBatchInsertForQRcord(){
        //init
        Activities activities = initActivities();

        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;

        Product cmProduct = initProduct(cmProductId);
        cmProduct.setType(PrizeType.FLOWPACKAGE.getType());
        Product cuProduct = initProduct(cuProductId);
        cuProduct.setType(PrizeType.FLOWPOOL.getType());
        Product ctProduct = initProduct(ctProductId);
        ctProduct.setType(PrizeType.FLOWPOOL.getType());

        //invalid
        assertFalse(activityPrizeService.batchInsertForQRcord(null,
                cmProductId, cuProductId, ctProductId));

        //valid
        Mockito.when(productService.selectProductById(cmProductId)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(cuProductId)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(ctProductId)).thenReturn(ctProduct);
        Mockito.when(activityPrizeMapper.batchInsert(anyMap())).thenReturn(3);

        assertTrue(activityPrizeService.batchInsertForQRcord(activities,
                cmProductId, cuProductId, ctProductId));

        Mockito.verify(productService, times(3)).selectProductById(anyLong());
        Mockito.verify(activityPrizeMapper).batchInsert(anyMap());
    }

    /**
     * deleteActivityPrize
     * @author qinqinyan
     * */
    @Test
    public void testDeleteActivityPrize(){
        List<Long> delProdIds = new ArrayList<Long>();
        String activityId = "test";

        assertTrue(activityPrizeService.deleteActivityPrize(delProdIds, activityId));

        Long delProdId = 1L;
        delProdIds.add(delProdId);
        assertTrue(activityPrizeService.deleteActivityPrize(delProdIds, null));

        Mockito.when(activityPrizeMapper.deleteActivityPrize(anyList(), anyString())).thenReturn(1);
        assertTrue(activityPrizeService.deleteActivityPrize(delProdIds, activityId));
        Mockito.verify(activityPrizeMapper).deleteActivityPrize(anyList(), anyString());
    }

    /**
     * addActivityPrize
     * @author qinqinyan
     * */
    @Test
    public void testAddActivityPrize(){
        Activities activities = initActivities();
        List<Long> addProdIds = new ArrayList<Long>();
        String activityId = "test";
        Long cmMobileCnt = 1L;
        Long cuMobileCnt = 1L;
        Long ctMobileCnt = 1L;

        assertTrue(activityPrizeService.addActivityPrize(addProdIds, activityId,
                cmMobileCnt, cuMobileCnt, ctMobileCnt));

        Long addProdId1 = 1L;
        Long addProdId2 = 2L;
        Long addProdId3 = 3L;
        addProdIds.add(addProdId1);
        addProdIds.add(addProdId2);
        addProdIds.add(addProdId3);

        Product cmProduct = initProduct(addProdId1);
        cmProduct.setIsp(IspType.CMCC.getValue());
        cmProduct.setType(PrizeType.FLOWPACKAGE.getType());

        Product cuProduct = initProduct(addProdId2);
        cuProduct.setIsp(IspType.UNICOM.getValue());
        cuProduct.setType(PrizeType.FLOWPOOL.getType());

        Product ctProduct = initProduct(addProdId3);
        ctProduct.setIsp(IspType.TELECOM.getValue());
        ctProduct.setType(PrizeType.FLOWPACKAGE.getType());


        assertTrue(activityPrizeService.addActivityPrize(addProdIds, null,
                cmMobileCnt, cuMobileCnt, ctMobileCnt));

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(productService.selectProductById(addProdId1)).thenReturn(cmProduct);
        Mockito.when(productService.selectProductById(addProdId2)).thenReturn(cuProduct);
        Mockito.when(productService.selectProductById(addProdId3)).thenReturn(ctProduct);
        Mockito.when(activityPrizeMapper.batchInsert(anyMap())).thenReturn(3);

        assertTrue(activityPrizeService.addActivityPrize(addProdIds, activityId,
                cmMobileCnt, cuMobileCnt, ctMobileCnt));
        assertTrue(activityPrizeService.addActivityPrize(addProdIds, activityId,
                null, null, null));

        Mockito.verify(activitiesService, times(2)).selectByActivityId(anyString());
        Mockito.verify(productService, times(6)).selectProductById(anyLong());
        Mockito.verify(activityPrizeMapper, times(2)).batchInsert(anyMap());
    }

    /**
     * insertForRedpacket
     * @author qinqinyan
     * */
    @Test
    public void testInsertForRedpacket(){
        //invalid
        assertFalse(activityPrizeService.insertForRedpacket(null));

        //valid
        Mockito.when(activityPrizeMapper.insertForRedpacket(any(ActivityPrize.class))).thenReturn(1);
        assertTrue(activityPrizeService.insertForRedpacket(new ActivityPrize()));
        Mockito.verify(activityPrizeMapper).insertForRedpacket(any(ActivityPrize.class));
    }

    private Product initProduct(Long productId){
        Product product = new Product();
        product.setId(productId);
        product.setName("测试产品");
        product.setProductSize(10L);
        return product;
    }

    private Activities initActivities(){
        Activities activities = new Activities();
        activities.setActivityId("test");
        activities.setEntId(1L);
        return activities;
    }

    /**
     * 批量更新奖品信息
     * @author qinqinyan
     * */
    @Test
    public void testBatchUpdateSelective(){
        List<ActivityPrize> activityPrizeList = new ArrayList<ActivityPrize>();
        assertFalse(activityPrizeService.batchUpdateSelective(activityPrizeList));

        ActivityPrize record = new ActivityPrize();
        record.setId(1L);
        activityPrizeList.add(record);
        Mockito.when(activityPrizeMapper.batchUpdateSelective(anyMap())).thenReturn(1);
        assertTrue(activityPrizeService.batchUpdateSelective(activityPrizeList));
        Mockito.verify(activityPrizeMapper).batchUpdateSelective(anyMap());
    }

    @Test
    public void testUpdateActivityPrize(){
        List<Long> updateProdIds = new ArrayList<Long>();
        assertTrue(activityPrizeService.updateActivityPrize(updateProdIds, null, null, null, null));

        updateProdIds.add(1L);
        updateProdIds.add(2L);
        updateProdIds.add(3L);

        String activityId = "test";
        Long cmMobileCnt = 1L;
        Long cuMobileCnt = 1L;
        Long ctMobileCnt = 1L;

        List<ActivityPrize> activityPrizeList = new ArrayList<ActivityPrize>();
        ActivityPrize item1 = new ActivityPrize();
        item1.setProductId(1L);
        item1.setIsp(IspType.CMCC.getValue());
        ActivityPrize item2 = new ActivityPrize();
        item2.setProductId(2L);
        item2.setIsp(IspType.UNICOM.getValue());
        ActivityPrize item3 = new ActivityPrize();
        item3.setProductId(3L);
        item3.setIsp(IspType.TELECOM.getValue());
        activityPrizeList.add(item1);
        activityPrizeList.add(item2);
        activityPrizeList.add(item3);

        Mockito.when(activityPrizeMapper.getDetailByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(activityPrizeMapper.updateCountByPrimaryKey(any(ActivityPrize.class))).thenReturn(1);
        assertTrue(activityPrizeService.updateActivityPrize(updateProdIds, activityId, cmMobileCnt, cuMobileCnt, ctMobileCnt));

        Mockito.verify(activityPrizeMapper, atLeastOnce()).getDetailByActivityId(anyString());
        Mockito.verify(activityPrizeMapper, atLeastOnce()).updateCountByPrimaryKey(any(ActivityPrize.class));
    }

}
