package Utils;

import Lianjia.City;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CityUtils {
	public static String getAuthorization(HashMap dict) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String datastr = "vfkpbin1ix2rb88gfjebs0f60cbvhedlcity_id=%sgroup_type=%smax_lat=%s"
				+ "max_lng=%smin_lat=%smin_lng=%srequest_ts=%s";
		datastr = String.format(datastr,
				dict.get("city_id"), dict.get("group_type"), dict.get("max_lat"),
				dict.get("max_lng"), dict.get("min_lat"), dict.get("min_lng"), dict.get("request_ts"));
		return CommonUtils.getMD5(datastr);
	}

	public static void GetDistrictInfo(City city) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		int time_13 = Math.round(new Date().getTime() * 1000);
		HashMap<String, String> dict = new LinkedHashMap<>();
		dict.put("group_type", "district");
		dict.put("city_id", city.getCity_id());
		dict.put("max_lat", city.getMax_lat());
		dict.put("min_lat", city.getMin_lat());
		dict.put("max_lng", city.getMax_lng());
		dict.put("min_lng", city.getMin_lng());
		dict.put("request_ts", time_13 + "");
		String authorization = getAuthorization(dict);
		String realUrl = String.format(City.url,
				city.getCity_id(), dict.get("group_type"),
				city.getMax_lat(), city.getMin_lat(),
				city.getMax_lng(), city.getMin_lng(),
				"%7B%7D", time_13,
				authorization, time_13);
	}
}
