import Lianjia.City;
import Lianjia.Community;
import Lianjia.District;
import Lianjia.House;
import ORM.Service.CityService;
import ORM.Service.CommunityService;
import ORM.Service.DistrictService;
import ORM.Service.HouseService;
import Utils.CommonUtils;
import Utils.HouseRunner;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sample {
	public static void main(String... args) {
		String cityName = "深圳";
		CityService cityService = new CityService();
		City city = cityService.selectByName(cityName);
		List<District> districts = getDistrictData(city);
		int count = getCommunityData(districts);
		CommonUtils.Logger().info("读取到" + count + "条小区信息");
//		HouseDataTestMultiThread(city);
	}

	private static List<District> getDistrictData(City city) {
		DistrictService districtService = new DistrictService();
		districtService.createTable();
		return districtService.GetDistrictInfo(city);
	}

	private static int getCommunityData(List<District> districts) {
		CommunityService communityService = new CommunityService();
		communityService.createTable();
		int count = 0;
		for (District district : districts) {
			count += communityService.getCommunityData(district);
		}
		return count;
	}

	private static void HouseDataTestMultiThread(City city) {
		CommunityService communityService = new CommunityService();
		DistrictService districtService = new DistrictService();
		List<District> districts = districtService.selectByCity(city);

		List<Community> communities = new ArrayList<>();
		for (District district : districts) {
			communities.addAll(communityService.selectByDistrict(district));
		}

		HouseService houseService = new HouseService();
		houseService.createTable();

		int dataLength = communities.size();
		int coreNum = Runtime.getRuntime().availableProcessors();
		int dataPreThread = (int) Math.round(Math.ceil(communities.size() / (double) (coreNum)));
		int groupNum = (int) Math.round(Math.ceil(dataLength / (double) dataPreThread));
		if (groupNum <= 0) groupNum = 1;

		Map<String, House> resMap = new LinkedHashMap<>();
		resMap = Collections.synchronizedMap(resMap);
		ExecutorService pool = Executors.newFixedThreadPool(groupNum);
		for (int i = 0; i < groupNum; i++) {
			int startIndex = i * dataPreThread;
			int endIndex = (i + 1) * dataPreThread;
			if (endIndex > dataLength) {
				endIndex = dataLength;
			}
			List<Community> tmpList = communities.subList(startIndex, endIndex);
			pool.submit(new HouseRunner(tmpList, resMap));
		}
		pool.shutdown();
		while (true) {
			if (pool.isTerminated()) {
				break;
			}
		}
		List<House> resList = new ArrayList<>(resMap.values());
		int subNum = resList.size() / 100 + 1;
		for (int i = 0; i < subNum; i++) {
			int startIndex = i * 100;
			int endIndex = startIndex + 100;
			if (endIndex > resList.size()) endIndex = resList.size();
			houseService.bathInsertList(resList.subList(startIndex, endIndex));
		}
	}
}
