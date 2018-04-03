/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ActivityRecordMapper;
import com.cmcc.vrp.province.model.ActivityRecord;
import com.cmcc.vrp.province.service.ActivityRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月18日
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityRecordServiceImplTest {

    @InjectMocks
    ActivityRecordService activityRecordService = new ActivityRecordServiceImpl();

    @Mock
    ActivityRecordMapper activityRecordMapper;

    @Test
    public void testInsert() {

        ActivityRecord activityRecord = new ActivityRecord();

        when(activityRecordMapper.insert(activityRecord)).thenReturn(1);
        //Mockito.any(ActivityRecord.class);

        assertTrue(activityRecordService.insert(activityRecord));
        verify(activityRecordMapper).insert(activityRecord);


    }

    @Test
    public void testInsert2() {
        ActivityRecord activityRecord = new ActivityRecord();

        when(activityRecordMapper.insert(activityRecord)).thenReturn(0);

        try {
            activityRecordService.insert(activityRecord);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
