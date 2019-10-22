package ORM.Service;

import Lianjia.Community;
import Lianjia.District;
import ORM.Mapper.CommunityMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CommunityService implements CommunityMapper {

	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			CommunityMapper mapper = session.getMapper(CommunityMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			CommonUtils.Logger().error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	@Override
	public int bathInsertList(List<Community> communities) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			CommunityMapper mapper = session.getMapper(CommunityMapper.class);
			mapper.createTable();
			int res = mapper.bathInsertList(communities);
			session.commit();
			return res;
		} catch (Exception e) {
			CommonUtils.Logger().error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	public void getCommunityData(District district) {
		try {
			List<BigDecimal> lat = new LinkedList<>();
			List<BigDecimal> lng = new LinkedList<>();
			Optional<String> border = Optional.of(district.getBorder());
			for (String s : border.get().split(";")) {
				lng.add(new BigDecimal(s.split(",")[0]));
				lat.add(new BigDecimal(s.split(",")[1]));
			}
			List<BigDecimal[]> squares = new LinkedList<>();
			BigDecimal step = new BigDecimal(0.02);
			List<BigDecimal> lngOffset = CommonUtils.getStepRange(Collections.min(lng), Collections.max(lng), step);
			List<BigDecimal> latOffset = CommonUtils.getStepRange(Collections.min(lat), Collections.max(lat), step);
			for (BigDecimal x : lngOffset) {
				for (BigDecimal y : latOffset) {
					BigDecimal[] decimals = new BigDecimal[]{
							y.setScale(6, BigDecimal.ROUND_HALF_EVEN),
							y.subtract(step).setScale(6, RoundingMode.HALF_EVEN),
							x.setScale(6, RoundingMode.HALF_EVEN),
							x.subtract(step).setScale(6, RoundingMode.HALF_EVEN)};
					squares.add(decimals);
				}
			}
			String time_13 = new Date().getTime() + "";
			for (int i = 0; i < squares.size(); i++) {
				BigDecimal[] square = squares.get(i);
				CommonUtils.Logger().info("正在处理行政区划(" + district.getCity_name() + "_" + district.getName() + ")下小区信息（" + i + "/" + squares.size() + ")");
				HashMap<String, String> dict = new LinkedHashMap<>();
				dict.put("group_type", "community");
				dict.put("city_id", district.getCity_id());
				dict.put("max_lat", square[0].toString());
				dict.put("min_lat", square[1].toString());
				dict.put("max_lng", square[2].toString());
				dict.put("min_lng", square[3].toString());
				dict.put("request_ts", time_13);
				String authorization = CommonUtils.getAuthorization(dict);
				String realUrl = String.format(DistrictService.url,
						district.getCity_id(), dict.get("group_type"),
						square[0].toString(), square[1].toString(),
						square[2].toString(), square[3].toString(),
						"%7B%7D", time_13,
						authorization, time_13);
				JSONObject res = JSON.parseObject(CommonUtils.postHTTPRequest(realUrl));
			}
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
			e.printStackTrace();
		}
	}
}
