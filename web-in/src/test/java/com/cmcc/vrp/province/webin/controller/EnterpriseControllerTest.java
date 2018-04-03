package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.QueryObject;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class EnterpriseControllerTest {

    @TestSubject
    private final EnterpriseController controller = new EnterpriseController();

    @Mock
    EnterprisesService enterprisesService;

    QueryObject queryObject = new QueryObject();

    @Ignore
    @Test
    public void testEnterpriseIndex() {

//		List<Enterprise> list = new ArrayList<Enterprise>();
//		// expect(
//		// service.queryPaginationAdminCount(EasyMock.anyObject(QueryObject.class))).andReturn(10);
//		expect(enterprisesService.showForPageResultCount(queryObject))
//				.andReturn(10);
//		expect(enterprisesService.showEnterprisesForPageResult(queryObject))
//				.andReturn(list);
//
//		replay(enterprisesService);
//		String value = controller.enterpriseIndex(new ModelMap(), queryObject);
//
//		Assert.assertEquals(value, "enterprises/index.ftl");
//
//		verify(enterprisesService);

    }

	

	/*@Test
    public void testAddSubmit() {
		HttpServletRequest request = null ;
		Enterprise enterprise = new Enterprise();
		enterprise.setName("kok");
		enterprise.setCode("CJ290");
		enterprise.setPhone("18818919924");

		expect(enterprisesService.insertSelective(enterprise)).andReturn(true);
		expect(enterprisesService.getEnterpriseList()).andReturn(
				new ArrayList<Enterprise>());

		replay(enterprisesService);
		Assert.assertEquals(
				controller.addOrEditSubmit(new ModelMap(), enterprise,request),
				"redirect:index.html");

		verify(enterprisesService);
	}*/

	/*@Test
	public void testEditSubmit() {
		HttpServletRequest request = null ;
		Enterprise enterprise = new Enterprise();
		enterprise.setName("kok");
		enterprise.setCode("CJ290");
		enterprise.setPhone("18818919924");
		enterprise.setId(1l);

		expect(enterprisesService.getEnterpriseList()).andReturn(
				new ArrayList<Enterprise>());
		expect(enterprisesService.updateByPrimaryKeySelective(enterprise))
				.andReturn(true);
		replay(enterprisesService);

		Assert.assertEquals(
				controller.addOrEditSubmit(new ModelMap(), enterprise,request),
				"redirect:index.html");
		verify(enterprisesService);
	}
*/

//	@Test
//	public void testDelete() {
//		List<Enterprise> list = new ArrayList<Enterprise>();
//		Enterprise enterprise = new Enterprise();
//		enterprise.setId(1l);
//		expect(enterprisesService.deleteByPrimaryKey(enterprise)).andReturn(
//				true);
//		expect(enterprisesService.showForPageResultCount(queryObject))
//				.andReturn(10);
//		expect(enterprisesService.showEnterprisesForPageResult(queryObject))
//				.andReturn(list);
//		replay(enterprisesService);
//		String value = controller.delete(new ModelMap(), queryObject,
//				enterprise);
//
//		Assert.assertEquals(value, "enterprises/index.ftl");
//		verify(enterprisesService);
//	}
}
