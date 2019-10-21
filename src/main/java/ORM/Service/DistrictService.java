package ORM.Service;

import Lianjia.City;
import Lianjia.District;
import ORM.Mapper.DistrictMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DistrictService implements DistrictMapper {
	static final String url_fang = "https://ajax.lianjia.com/map/resblock/ershoufanglist/?callback=jQuery11110617424919783834_1541868368031"
			+ "&id=%s"
			+ "&order=0"
			+ "&page=%d"
			+ "&filters=%s"
			+ "&request_ts=%s"
			+ "&source=ljpc"
			+ "&authorization=%s"
			+ "&_=%s";
	static final String url = "https://ajax.lianjia.com/map/search/ershoufang/?callback=jQuery1111012389114747347363_1534230881479"
			+ "&city_id=%s"
			+ "&group_type=%s"
			+ "&max_lat=%s"
			+ "&min_lat=%s"
			+ "&max_lng=%s"
			+ "&min_lng=%s"
			+ "&filters=%s"
			+ "&request_ts=%s"
			+ "&source=ljpc"
			+ "&authorization=%s"
			+ "&_=%s";
	private static final Logger logger = LogManager.getLogger(DistrictService.class);

	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			DistrictMapper mapper = session.getMapper(DistrictMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			logger.error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	@Override
	public int bathInsertList(List<District> districts) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			DistrictMapper mapper = session.getMapper(DistrictMapper.class);
			mapper.createTable();
			int res = mapper.bathInsertList(districts);
			session.commit();
			return res;
		} catch (Exception e) {
			logger.error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	public int GetDistrictInfo(City city) throws IOException, NoSuchAlgorithmException {
		String time_13 = new Date().getTime() + "";
		HashMap<String, String> dict = new LinkedHashMap<>();
		dict.put("group_type", "district");
		dict.put("city_id", city.getCity_id());
		dict.put("max_lat", city.getMax_lat());
		dict.put("min_lat", city.getMin_lat());
		dict.put("max_lng", city.getMax_lng());
		dict.put("min_lng", city.getMin_lng());
		dict.put("request_ts", time_13);
		String authorization = CommonUtils.getAuthorization(dict);
		String realUrl = String.format(url,
				city.getCity_id(), dict.get("group_type"),
				city.getMax_lat(), city.getMin_lat(),
				city.getMax_lng(), city.getMin_lng(),
				"%7B%7D", time_13,
				authorization, time_13);
		JSONObject res = JSON.parseObject(CommonUtils.postHTTPRequest(realUrl));
		return insertDistrictData(city, res);
	}

	private int insertDistrictData(City city, JSONObject res) {
		if (res.getIntValue("errno") != 0) {
			logger.error(res.getString("error"));
			throw new RuntimeException("请求失败，返回结果无效");
		}
		Map<String, JSONObject> districts = res.getJSONObject("data").getObject("list", Map.class);
		List<District> districtList = new LinkedList<>();
		for (String district_id : districts.keySet()) {
			District district = districts.get(district_id).toJavaObject(District.class);
			district.setCity_id(city.getCity_id());
			district.setCity_name(city.getCity_name());
			districtList.add(district);
			logger.info("处理行政区划数据: " + district.getCity_name() + "-" + district.getName());
		}
		DistrictService districtService = new DistrictService();
		return districtService.bathInsertList(districtList);
	}

}
