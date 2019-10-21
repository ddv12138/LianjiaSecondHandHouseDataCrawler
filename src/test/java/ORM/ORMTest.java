package ORM;

import ORM.Service.CityService;
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
}
