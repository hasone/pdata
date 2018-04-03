package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.service.SmsTemplateService;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import junit.framework.Assert;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * @author wujiamin
 * @date 2016年10月17日下午4:39:40
 */
/**
 * @author wujiamin
 * @date 2016年10月17日下午4:40:56
 */
/**
 * @author wujiamin
 * @date 2016年10月17日下午4:41:05
 */
@RunWith(EasyMockRunner.class)
public class SmsTemplateControllerTest {

    @TestSubject
    SmsTemplateController templateController = new SmsTemplateController();

    @Mock
    SmsTemplateService service;

    PageResult pageResult = new PageResult();
    ModelMap map = new ModelMap();
    List<SmsTemplate> list = new ArrayList<SmsTemplate>();
    SmsTemplate template = new SmsTemplate();

    /**
     * title: config desc: wuguoping 2017年5月31日
     */
    public void config() {
        // 假设 countSmsTemplate 返回值为10
        QueryObject queryObject = pageResult.getQueryObject();
        expect(service.countSmsTemplate(queryObject.getQueryCriterias())).andReturn(10);

        // 假设showSmsTemplate 根据总记录数10和每页数5 计算出第一页需要返回5条记录
        expect(service.showSmsTemplate(queryObject.toMap())).andReturn(list);
    }

    /**
     * title: indexTest
     * desc: 
     * wuguoping
     * 2017年5月31日
     */
    @Ignore
    @Test
    public void indexTest() {
        //
        // list.add(new SmsTemplate());
        // list.add(new SmsTemplate());
        // list.add(new SmsTemplate());
        // list.add(new SmsTemplate());
        // list.add(new SmsTemplate());
        //
        // config();
        // // start
        // replay(service);
        //
        // templateController.index(map, pageResult.getQueryObject());
        //
        // // 获取返回的pageResult
        // pageResult = (PageResult) map.get("pageResult");
        //
        // // 断言对比
        // Assert.assertEquals(pageResult.getList().size(), 5);
        //
        // // end
        // verify(service);

    }

    @Test
    public void saveTest() {

        template.setName("充值提醒");
        template.setContent("尊敬的用户${0} 您好，您的用户余额已不足${1}元");
        // config();
        // 假设在Controller端验证成功并且调用了service成功返回1
        expect(service.insertSelective(template)).andReturn(true);

        expect(service.checkSms(template.getName())).andReturn(null);
        // start
        replay(service);

        String returnValue = templateController.save(map, template);

        Assert.assertEquals(returnValue, "redirect:index.html");

        // end
        verify(service);
    }

    /**
     * @Title: editTest
     * @Author: wujiamin
     * @date 2016年10月17日下午4:36:11
     */
    @Test
    public void editTest() {

        template.setId(1);

        // 假设selectByPrimaryKey能查询出值
        expect(service.selectByPrimaryKey(template.getId())).andReturn(template);

        // start
        replay(service);

        String returnValue = templateController.edit(map, template);
        Assert.assertEquals(returnValue, "/sms/edit.ftl");

        // end
        verify(service);

    }

    @Test
    public void updateTest() {

        template.setId(1);
        template.setName("hello");
        template.setContent("world");

        // config();
        // 假设updateByPrimaryKeySelective执行成功
        expect(service.updateByPrimaryKeySelective(template)).andReturn(true);
        expect(service.checkSms(template.getName())).andReturn(null);
        // start
        replay(service);

        String returnValue = templateController.update(map, template);
        Assert.assertEquals(returnValue, "redirect:index.html");

        // end
        verify(service);

    }

    /**
     * @Title: deleteTest
     * @Author: wujiamin
     * @date 2016年10月17日下午4:39:22
     */
    @Ignore
    @Test
    public void deleteTest() {

        // template.setId(1);
        //
        // config();
        // // 假设deleteByPrimaryKey能查询出值
        // expect(service.deleteByPrimaryKey(template.getId()))
        // .andReturn(true);
        //
        // // start
        // replay(service);
        //
        // String returnValue = templateController.delete(map,
        // template,pageResult.getQueryObject());
        // Assert.assertEquals(returnValue, "/sms/index.ftl");
        //
        // // end
        // verify(service);

    }

    /**
     * @Title: showSmsTemplateTest
     * @Author: wujiamin
     * @date 2016年10月17日下午4:36:52
     */
    @Test
    public void showSmsTemplateTest() {

        template.setId(1);

        // 假设deleteByPrimaryKey能查询出值
        expect(service.selectByPrimaryKey(template.getId())).andReturn(template);
        // start
        replay(service);
        String returnValue = templateController.showSmsTemplate(map, template);
        SmsTemplate smsTemplate = (SmsTemplate) map.get("smsTemplate");
        Assert.assertEquals(returnValue, "/sms/showSmsTemplate.ftl");
        Assert.assertEquals(smsTemplate.getId(), 1);
        // end
        verify(service);

    }

}
