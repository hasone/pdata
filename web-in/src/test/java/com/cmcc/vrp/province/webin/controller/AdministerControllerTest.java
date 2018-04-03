package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;

@RunWith(EasyMockRunner.class)
public class AdministerControllerTest {

    /**
     * class to be tested
     */
    @TestSubject
    private final AdministerController administerController = new AdministerController();

    /**
     * create mock object
     */
    @Mock
    AdministerService service;

    PageResult<Administer> pageResult = new PageResult<Administer>();
    ModelMap map = new ModelMap();
    List<Administer> list = new ArrayList<Administer>();
    Administer administer = new Administer();

    public void config() {
        // 假设 queryPaginationAdminCount 返回值为10
        expect(
            service.queryPaginationAdminCount(EasyMock
                .anyObject(QueryObject.class))).andReturn(10);

        // 假设queryPaginationAdminList 根据总记录数10和每页数5 计算出第一页需要返回5条记录
        expect(
            service.queryPaginationAdminList(EasyMock
                .anyObject(QueryObject.class))).andReturn(list);
    }

    @Test
    public void testIndex() {
//		// record expected behavior
//		list.add(new Administer());
//		list.add(new Administer());
//		list.add(new Administer());
//		list.add(new Administer());
//		list.add(new Administer());
//
//		config();
//		// start
//		replay(service);
//
//		administerController.index(map, pageResult.getQueryObject());
//
//		// 获取返回的pageResult
//		pageResult = (PageResult) map.get("pageResult");
//
//		// 断言对比
//		Assert.assertEquals(pageResult.getList().size(), 5);
//
//		// end
//		verify(service);

    }

    @Test
    public void testSave() {

//		administer.setMobilePhone("18620132798");
//		administer.setPassword("8888888");
//		administer.setUserName("kok");
////		config();
//		// 假设在Controller端验证成功并且调用了service成功返回1
//		expect(service.insertAdminister(administer, 1l, 1l)).andReturn(true);
//
//		// start
//		replay(service);
//
//		String returnValue = administerController.save(map, administer, 1l, 1l, null);
//
//		Assert.assertEquals(returnValue, "redirect:index.html");
//
//		// end
//		verify(service);
    }

    @Test
    public void testUpdate() {

//		administer.setId(1l);
//		administer.setMobilePhone("18620132798");
//		administer.setPassword("8888888");
//		administer.setUserName("kok");
////		config();
//		// 假设在Controller端验证成功并且调用了service成功返回1
//		expect(service.updateAdminister(administer, 1l, 1l)).andReturn(true);
//
////		expect(service.selectAdministerById( 1l)).andReturn(administer);
//		// start
//		replay(service);
//
//		String returnValue = administerController.save(map, administer, 1l, 1l, null);
//
//		Assert.assertEquals(returnValue, "redirect:index.html");
//
//		// end
//		verify(service);
    }


    @Test
    public void testDelete() {

//		administer.setId(1l);
//		config();
//		// 假设在Controller端验证成功并且调用了service成功返回1
//		expect(service.deleteById(1l)).andReturn(true);
//
////		expect(service.selectAdministerById( 1l)).andReturn(administer);
//		// start
//		replay(service);
//
//		String returnValue = administerController.delete(map, pageResult.getQueryObject(), 1l);
//
//		Assert.assertEquals(returnValue, "/admin/index.ftl");
//
//		// end
//		verify(service);
    }

}
