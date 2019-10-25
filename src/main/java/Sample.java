import Lianjia.City;
import Lianjia.Community;
import Lianjia.District;
import ORM.Service.CityService;
import ORM.Service.CommunityService;
import ORM.Service.DistrictService;
import ORM.Service.HouseService;
import Utils.CommonUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sample {
	public static void main(String... args) {
		String cityName = "武汉";
		CityService cityService = new CityService();
		cityService.createTable();
		DistrictService districtService = new DistrictService();
		districtService.createTable();
		CommunityService communityService = new CommunityService();
		communityService.createTable();
		HouseService houseService = new HouseService();
		houseService.createTable();
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

		List<Community> communities = communityService.selectAll();

		int dataLength = communities.size();
		int coreNum = Runtime.getRuntime().availableProcessors();
		int dataPreThread = (int) Math.round(Math.ceil(communities.size() / (double) (coreNum)));
		int groupNum = (int) Math.round(Math.ceil(dataLength / (double) dataPreThread));
		if (groupNum <= 0) groupNum = 1;

		ExecutorService pool = Executors.newFixedThreadPool(groupNum);
		for (int i = 0; i < groupNum; i++) {
			int startIndex = i * dataPreThread;
			int endIndex = (i + 1) * dataPreThread;
			if (endIndex > dataLength) {
				endIndex = dataLength;
			}
			List<Community> tmpList = communities.subList(startIndex, endIndex);
//			pool.submit(new HouseRunner(tmpList));
		}
		while (true) {
			if (pool.isTerminated()) {
				break;
			}
		}
	}
}
