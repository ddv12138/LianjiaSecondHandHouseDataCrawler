package ORM.Service;

import Lianjia.Community;
import Lianjia.House;
import ORM.Mapper.HouseMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HouseService implements HouseMapper {

	static final String url_fang = "https://ajax.lianjia.com/map/resblock/ershoufanglist/?callback=jQuery11110617424919783834_1541868368031"
			+ "&id=%s"
			+ "&order=0"
			+ "&page=%d"
			+ "&filters=%s"
			+ "&request_ts=%s"
			+ "&source=ljpc"
			+ "&authorization=%s"
			+ "&_=%s";
	static String auth_fang = "vfkpbin1ix2rb88gfjebs0f60cbvhedlid=%sorder=%dpage=%drequest_ts=%s";

	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			HouseMapper mapper = session.getMapper(HouseMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
		}
	}

	@Override
	public int bathInsertList(List<House> houseList) {
		if (houseList.isEmpty()) return -1;
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			HouseMapper mapper = session.getMapper(HouseMapper.class);
			int count = mapper.bathInsertList(houseList);
			session.commit();
			return count;
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
		}
		return -1;
	}

	@Override
	public House selectByHouseId(String housId) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			HouseMapper mapper = session.getMapper(HouseMapper.class);
			return mapper.selectByHouseId(housId);
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
		}
		return null;
	}

	public void getCompleteHouseDataByCommunity(Community community,Map<String,House> resMap) {
		int pageCount = (int) Math.ceil(community.getCount() / 10) + 1;
		for (int i = 1; i < pageCount; i++) {
			String time_13 = new Date().getTime() + "";
			String authorization = CommonUtils.getMD5(String.format(auth_fang, community.getId(), 0, i, time_13));
			String url = String.format(url_fang, community.getId(), i, "%7B%7D", time_13, authorization, time_13);
			JSONObject res = JSON.parseObject(CommonUtils.postHTTPRequest(url));
			if (!CommonUtils.JSONResultCheck(res)) return;
			try {
				Map<String, JSONObject> houseMap = res.getJSONObject("data").getJSONObject("ershoufang_info").getJSONObject("list").toJavaObject(Map.class);
				for (String houseId : houseMap.keySet()) {
					House house = houseMap.get(houseId).toJavaObject(House.class);
					if (null == house.getHouseId() || null != this.selectByHouseId(house.getHouseId())) {
						continue;
					}
					house.setCommunityUUID(community.getUuid() + "");
					resMap.put(house.getHouseId(),house);
				}
				CommonUtils.Logger().info(resMap.size());
//				CommonUtils.Logger().info(community.getCity_name() + "_" + community.getDistrict_name() + "_" + community.getName() + ":(" + count + "/" + community.getCount() + ")");
				continue;
			} catch (ClassCastException e) {
				CommonUtils.Logger().error(e);
			}
			List<House> houseList = res.getJSONObject("data").getJSONObject("ershoufang_info").getJSONArray("list").toJavaList(House.class);
			Iterator<House> it = houseList.iterator();
			while (it.hasNext()) {
				House house = it.next();
				if (null != this.selectByHouseId(house.getHouseId())) {
					it.remove();
					continue;
				}
				house.setCommunityUUID(community.getUuid() + "");
				resMap.put(house.getHouseId(),house);
			}
			CommonUtils.Logger().info(resMap.size());
//			CommonUtils.Logger().info(community.getCity_name() + "_" + community.getDistrict_name() + "_" + community.getName() + ":(" + count + "/" + community.getCount() + ")");
		}
	}
}
