package ORM;

import Lianjia.City;
import ORM.Service.DistrictService;
import Utils.CommonUtils;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class UtilsTest {
	private static final Logger logger = LogManager.getLogger(UtilsTest.class);

	@Test
	public void MD5Test() {
		try {
			logger.info(CommonUtils.getMD5("待加密字符串"));
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void districtInfoTest() {
		try {
			City city = new City();
			city.setCity_id("420100");
			city.setCity_name("武汉");
			city.setMax_lat("31.370301");
			city.setMin_lat("29.982801");
			city.setMax_lng("115.086901");
			city.setMin_lng("113.699400");
			DistrictService districtService = new DistrictService();
			logger.info(districtService.GetDistrictInfo(city));
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	@Test
	public void stepRangeTest() {
		BigDecimal start = new BigDecimal("29.982801");
		BigDecimal end = new BigDecimal("31.370301");
		BigDecimal step = new BigDecimal("0.02");
		logger.info(start);
		logger.info(CommonUtils.getStepRange(start, end, step));
	}
}
