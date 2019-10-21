package Utils;

import Lianjia.City;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CityUtils {
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
	static final HashMap<String, String> headers = new LinkedHashMap<>();
	static final String cookie = "lianjia_uuid=9bdccc1a-7584-4639-ba95-b42cf21bbbc7;" +
			"jzqa=1.3180246719396510700.1534145942.1534145942.1534145942.1;" +
			"jzqckmp=1;" +
			"ga=GA1.2.964691746.1534145946;" +
			"gid=GA1.2.826685830.1534145946;" +
			"UM_distinctid=165327625186a-029cf60b1994ee-3461790f-fa000-165327625199d3;" +
			"select_city=310000;" +
			"lianjia_ssid=34fc4efa-7fcc-4f3f-82ae-010401f27aa8;" +
			"_smt_uid=5b72c5f7.5815bcdf;" +
			"Hm_lvt_9152f8221cb6243a53c83b956842be8a=1537530243;" +
			"select_city=110000;_jzqc=1;" +
			"_gid=GA1.2.178601063.1541866763;" +
			"_jzqb=1.2.10.1541866760.1";

	static {
		headers.put("Host", "ajax.lianjia.com");
		headers.put("Referer", "https://sh.lianjia.com/ditu/");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
	}
	public static String getAuthorization(HashMap dict) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String datastr = "vfkpbin1ix2rb88gfjebs0f60cbvhedlcity_id=%sgroup_type=%smax_lat=%s"
				+ "max_lng=%smin_lat=%smin_lng=%srequest_ts=%s";
		datastr = String.format(datastr,
				dict.get("city_id"), dict.get("group_type"), dict.get("max_lat"),
				dict.get("max_lng"), dict.get("min_lat"), dict.get("min_lng"), dict.get("request_ts"));
		return CommonUtils.getMD5(datastr);
	}

	public static String GetDistrictInfo(City city) throws IOException, NoSuchAlgorithmException {
		//1571630320903
		String time_13 = new Date().getTime() + "";
		HashMap<String, String> dict = new LinkedHashMap<>();
		dict.put("group_type", "district");
		dict.put("city_id", city.getCity_id());
		dict.put("max_lat", city.getMax_lat());
		dict.put("min_lat", city.getMin_lat());
		dict.put("max_lng", city.getMax_lng());
		dict.put("min_lng", city.getMin_lng());
		dict.put("request_ts", time_13);
		String authorization = getAuthorization(dict);
		String realUrl = String.format(url,
				city.getCity_id(), dict.get("group_type"),
				city.getMax_lat(), city.getMin_lat(),
				city.getMax_lng(), city.getMin_lng(),
				"%7B%7D", time_13,
				authorization, time_13);
		return CommonUtils.postHTTPRequest(realUrl, headers, cookie);
	}
}
