import Lianjia.City;
import ORM.Service.CityService;
import ORM.Service.DistrictService;
import Utils.CommonUtils;

public class Sample {
	public static void main(String... args) {
		String cityName = "武汉";
		CityService cityService = new CityService();
		cityService.initDefaultData();
		City city = cityService.selectByName(cityName);
		DistrictService districtService = new DistrictService();
		try {
			CommonUtils.Logger().info(districtService.GetDistrictInfo(city));
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
			CommonUtils.Logger().error("获取行政区划信息失败");
		}
	}
}
