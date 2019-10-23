package ORM;

import Lianjia.Community;
import Lianjia.District;
import ORM.Service.CityService;
import ORM.Service.CommunityService;
import ORM.Service.DistrictService;
import ORM.Service.HouseService;
import Utils.CommonUtils;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


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

	@Test
	@Ignore
	public void HouseDataTest() {
		CommunityService communityService = new CommunityService();
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
				List<Community> tmpList = communities.subList(startIndex, endIndex);
				pool.submit(new Runnable() {
					@Override
					public void run() {
						for (Community community : tmpList) {
							HouseService service = new HouseService();
							service.getCompleteHouseDataByCommunity(community);
						}
					}
				});
			}
		}
		pool.shutdown();
		while (true) {
			if (pool.isTerminated()) {
				break;
			}
		}
	}
}
