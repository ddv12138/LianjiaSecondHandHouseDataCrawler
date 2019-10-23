package ORM.Service;

import Lianjia.Community;
import Lianjia.House;
import ORM.Mapper.HouseMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

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

	public void getCompleteHouseDataByCommunity(Community community) {
		int pageCount = (int) Math.ceil(community.getCount() / 10) + 1;
		int count = 0;
		for (int i = 0; i < pageCount; i++) {
			String time_13 = new Date().getTime() + "";
			String authorization = CommonUtils.getMD5(String.format(auth_fang, community.getId(), 0, i, time_13));
			String url = String.format(url_fang, community.getId(), i, "%7B%7D", time_13, authorization, time_13);
			JSONObject res = JSON.parseObject(CommonUtils.postHTTPRequest(url));
			if (!CommonUtils.JSONResultCheck(res)) return;
			Map<String, JSONObject> houseMap = res.getJSONObject("data").getJSONObject("ershoufang_info").getJSONObject("list").toJavaObject(Map.class);
			List<House> houseList = new LinkedList<>();
			try {
				houseList = res.getJSONObject("data").getJSONArray("list").toJavaList(House.class);
				Iterator<House> it = houseList.iterator();
				while (it.hasNext()) {
					House house = it.next();
					if (null != this.selectByHouseId(house.getHouseId())) {
						it.remove();
						continue;
					}
					house.setCommunityUUID(community.getUuid() + "");
				}
				this.bathInsertList(houseList);
				CommonUtils.Logger().info(community.getCity_name() + "_" + community.getDistrict_name() + "_" + community.getName() + ":(" + count + "/" + community.getCount() + ")");
				continue;
			} catch (ClassCastException e) {
				CommonUtils.Logger().error(e);
			}
			for (String houseId : houseMap.keySet()) {
				House house = houseMap.get(houseId).toJavaObject(House.class);
				house.setCommunityUUID(community.getUuid() + "");
				houseList.add(house);
			}
			this.bathInsertList(houseList);
			count += houseList.size();
			CommonUtils.Logger().info(community.getCity_name() + "_" + community.getDistrict_name() + "_" + community.getName() + ":(" + count + "/" + community.getCount() + ")");
		}
	}
}
