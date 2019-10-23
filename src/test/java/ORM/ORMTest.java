package ORM;

import Lianjia.District;
import ORM.Service.CityService;
import ORM.Service.CommunityService;
import ORM.Service.DistrictService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;


public class ORMTest {
	private static final Logger logger = LogManager.getLogger(ORMTest.class);

	@Test
	public void CityTest() {
		CityService service = new CityService();
		try {
			service.createTable();
		} catch (Exception e) {
			logger.error("创建表失败,表可能已经存在");
		}
		try {
			logger.info(service.initDefaultData());
		} catch (Exception e) {
			logger.error("数据插入失败，数据可能已经存在");
		}
		logger.info(service.selectByName("武汉"));
	}

	@Test
	@Ignore
	public void CommunityDataTest() {
		DistrictService districtService = new DistrictService();
		CommunityService communityService = new CommunityService();
		District district = districtService.selectByName("东西湖");
		communityService.getCommunityData(district);
		district = districtService.selectByName("武昌");
		communityService.getCommunityData(district);
	}
}
