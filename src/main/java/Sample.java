import Lianjia.City;
import ORM.Service.CityService;
import ORM.Service.DistrictService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sample {
	private static final Logger logger = LogManager.getLogger(Sample.class);

	public static void main(String... args) {
		String cityName = "武汉";
		CityService cityService = new CityService();
		City city = cityService.selectByName("武汉");
		DistrictService districtService = new DistrictService();
		try {
			districtService.GetDistrictInfo(city);
		} catch (Exception e) {
			logger.error(e);
			logger.error("获取行政区划信息失败");
		}
	}
}
