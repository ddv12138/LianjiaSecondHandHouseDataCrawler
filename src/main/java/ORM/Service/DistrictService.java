package ORM.Service;

import Lianjia.City;
import Lianjia.District;
import ORM.Mapper.DistrictMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

public class DistrictService implements DistrictMapper {
	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			DistrictMapper mapper = session.getMapper(DistrictMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			CommonUtils.Logger().error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	@Override
	public int bathInsertList(List<District> districts) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			DistrictMapper mapper = session.getMapper(DistrictMapper.class);
			int res = mapper.bathInsertList(districts);
			session.commit();
			return res;
		} catch (Exception e) {
			CommonUtils.Logger().error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	public List<District> GetDistrictInfo(City city) {
		List<District> districtList = new ArrayList<>();
		try {
			String time_13 = new Date().getTime() + "";
			HashMap<String, String> dict = new LinkedHashMap<>();
			dict.put("group_type", "district");
			dict.put("city_id", city.getCity_id());
			dict.put("max_lat", city.getMax_lat());
			dict.put("min_lat", city.getMin_lat());
			dict.put("max_lng", city.getMax_lng());
			dict.put("min_lng", city.getMin_lng());
			dict.put("request_ts", time_13);
			String authorization = CommonUtils.getNormalAuthorization(dict);
			String realUrl = String.format(CommonUtils.url,
					city.getCity_id(), dict.get("group_type"),
					city.getMax_lat(), city.getMin_lat(),
					city.getMax_lng(), city.getMin_lng(),
					"%7B%7D", time_13,
					authorization, time_13);
			JSONObject res = JSON.parseObject(CommonUtils.postHTTPRequest(realUrl));
			if (!CommonUtils.JSONResultCheck(res)) return null;
			Map<String, JSONObject> districts = res.getJSONObject("data").getObject("list", Map.class);
			districtList = new LinkedList<>();
			for (String district_id : districts.keySet()) {
				District district = districts.get(district_id).toJavaObject(District.class);
				district.setCity_id(city.getCity_id());
				district.setCity_name(city.getCity_name());
				districtList.add(district);
				CommonUtils.Logger().info("处理行政区划数据: " + district.getCity_name() + "-" + district.getName() + "-均价" + district.getUnit_price() + "-总计" + district.getCount() + "个");
			}
			this.deleteByCityId(city.getCity_id());
			this.bathInsertList(districtList);
		} catch (Exception e) {
			e.printStackTrace();
			CommonUtils.Logger().error(e);
		}
		return districtList;
	}

	@Override
	public District selectByName(String name) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			DistrictMapper mapper = session.getMapper(DistrictMapper.class);
			return mapper.selectByName(name);
		} catch (Exception e) {
			CommonUtils.Logger().error("查询失败，数据表可能不存在");
			throw e;
		}
	}

	@Override
	public int deleteByCityId(String city_id) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			DistrictMapper mapper = session.getMapper(DistrictMapper.class);
			int count = mapper.deleteByCityId(city_id);
			session.commit();
			return count;
		} catch (Exception e) {
			CommonUtils.Logger().error("查询失败，数据表可能不存在");
			throw e;
		}
	}
}
