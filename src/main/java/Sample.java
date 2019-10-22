import Lianjia.City;
import Lianjia.District;
import ORM.Service.CityService;
import ORM.Service.CommunityService;
import ORM.Service.DistrictService;
import Utils.CommonUtils;

import java.util.List;

public class Sample {
	public static void main(String... args) {
		String cityName = "武汉";
		CityService cityService = new CityService();
		cityService.createTable();
		DistrictService districtService = new DistrictService();
		districtService.createTable();
		CommunityService communityService = new CommunityService();
		communityService.createTable();
		cityService.initDefaultData();
		City city = cityService.selectByName(cityName);
		try {
			List<District> districts = districtService.GetDistrictInfo(city);
			for (District district : districts) {
				communityService.getCommunityData(district);
			}
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
		}
	}
}
