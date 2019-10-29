import Lianjia.City;
import Lianjia.Community;
import Lianjia.District;
import Lianjia.House;
import ORM.Service.CityService;
import ORM.Service.CommunityService;
import ORM.Service.DistrictService;
import ORM.Service.HouseService;
import Utils.HouseRunner;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sample {
	public static void main(String... args) {
		String cityName = "深圳";
		CityService cityService = new CityService();
		City city = cityService.selectByName(cityName);
//		List<District> districts = getDistrictData(city);
//		int count = getCommunityData(districts);
//		CommonUtils.Logger().info("读取到" + count + "条小区信息");
		HouseDataTestMultiThread(city);
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
		List<Community> communities = communityService.selectByCity(city);

		int dataLength = communityService.countPreHouseNumByCity(city);
		int coreNum = Runtime.getRuntime().availableProcessors();
		int dataPreThread = (int) Math.round(Math.ceil(dataLength / (double) (coreNum)));

		Map<Integer, Integer> indexMap = new HashMap<>();
		int houseCount = 0;
		for (int i = 0; i < communities.size(); i++) {
			houseCount += communities.get(i).getCount();
			for (int j = i + 1; j < communities.size(); j++) {
				houseCount += communities.get(j).getCount();
				if (houseCount > dataPreThread) {
					indexMap.put(i, j);
					houseCount = 0;
					i = j - 1;
					break;
				}
			}
		}

		Map<String, House> resMap = new LinkedHashMap<>();
		resMap = Collections.synchronizedMap(resMap);
		ExecutorService pool = Executors.newFixedThreadPool(indexMap.size());
		for (Map.Entry<Integer, Integer> entry : indexMap.entrySet()) {
			List<Community> tmpList = communities.subList(entry.getKey(), entry.getValue());
			pool.submit(new HouseRunner(tmpList, resMap));
		}
		pool.shutdown();
		while (true) {
			if (pool.isTerminated()) {
				break;
			}
		}
		HouseService houseService = new HouseService();
		houseService.createTable();
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
