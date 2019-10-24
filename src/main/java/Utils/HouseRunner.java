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
	}
}
