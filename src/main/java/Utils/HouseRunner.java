package Utils;

import Lianjia.Community;
import Lianjia.House;
import ORM.Service.HouseService;

import java.util.List;
import java.util.Map;

public class HouseRunner implements Runnable {
	private List<Community> communities;
	private Map<String,House> resMap;

	public HouseRunner(List<Community> communities,Map<String,House> resMap) {
		this.communities = communities;
		this.resMap = resMap;
	}

	@Override
	public void run() {
		for (Community community : communities) {
			HouseService service = new HouseService();
			service.getCompleteHouseDataByCommunity(community,resMap);
		}
		CommonUtils.Logger().info(Thread.currentThread().getName() + "----一个线程结束，起止id为" + communities.get(0).getUuid() + "----->" + communities.get(communities.size() - 1).getUuid());
	}
}
