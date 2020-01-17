package ORM;

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
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ORMTest {
	private static final Logger logger = LogManager.getLogger(ORMTest.class);

	@Test
	void CityTest() {
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
	void CommunityDataTest() {
		DistrictService districtService = new DistrictService();
		CommunityService communityService = new CommunityService();
		District district = districtService.selectByName("东西湖");
		communityService.getCommunityData(district);
		district = districtService.selectByName("武昌");
		communityService.getCommunityData(district);
	}

	@Test
	@Ignore
	void HouseDataTest() {
		DistrictService districtService = new DistrictService();
		CommunityService communityService = new CommunityService();
		District district = districtService.selectByName("东西湖");
		communityService.getCommunityData(district);
		district = districtService.selectByName("武昌");
		communityService.getCommunityData(district);
	}

	@Test
	@Ignore
	void HouseDataTestMultiThread() {
		CommunityService communityService = new CommunityService();
		List<Community> communities = communityService.selectAll();
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

	@Test
	@Ignore
	void HouseDataTestSingleThread() {
		CommunityService communityService = new CommunityService();
		List<Community> communities = communityService.selectAll();
		HouseService houseService = new HouseService();
		houseService.createTable();
		Map<String, House> resMap = new LinkedHashMap<>();
		for (Community community : communities) {
			houseService.getCompleteHouseDataByCommunity(community, resMap);
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

	@Test
	void selectCommunityByDistrict() {
		CommunityService communityService = new CommunityService();
		DistrictService districtService = new DistrictService();
		District district = districtService.selectByName("东湖高新");
		List<Community> communities = communityService.selectByDistrict(district);
		CommonUtils.Logger().info(communities.size());
	}

	@Test
	void selectDistrictByCity() {
		CityService cityService = new CityService();
		City city = cityService.selectByName("武汉");
		DistrictService districtService = new DistrictService();
		List<District> districts = districtService.selectByCity(city);
		CommonUtils.Logger().info(districts);
	}

	@Test
	void houseCountTestByDistrict() {
		CommunityService communityService = new CommunityService();
		DistrictService districtService = new DistrictService();
		District district = districtService.selectByName("东湖高新");
		int count = communityService.countPreHouseNumByDistrict(district);
		CommonUtils.Logger().info(count);
	}

	@Test
	void housePreCountTestByCity() {
		CommunityService communityService = new CommunityService();
		CityService cityService = new CityService();
		City city = cityService.selectByName("武汉");
		int count = communityService.countPreHouseNumByCity(city);
		CommonUtils.Logger().info(count);
	}
}
