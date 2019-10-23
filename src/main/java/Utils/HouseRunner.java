package Utils;

import Lianjia.Community;
import Lianjia.House;
import ORM.Service.HouseService;

import java.util.List;

public class HouseRunner implements Runnable {
	private List<Community> communities;

	public HouseRunner(List<Community> communities) {
		this.communities = communities;
	}

	@Override
	public void run() {
		for (Community community : communities) {
			HouseService service = new HouseService();
			service.getCompleteHouseDataByCommunity(community);
		}
	}
}
