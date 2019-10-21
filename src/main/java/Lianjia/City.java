package Lianjia;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class City {
	public static final String url_fang = "https://ajax.lianjia.com/map/resblock/ershoufanglist/?callback=jQuery11110617424919783834_1541868368031"
			+ "&id=%s"
			+ "&order=0"
			+ "&page=%d"
			+ "&filters=%s"
			+ "&request_ts=%d"
			+ "&source=ljpc"
			+ "&authorization=%s"
			+ "&_=%d";
	public static final String url = "https://ajax.lianjia.com/map/search/ershoufang/?callback=jQuery1111012389114747347363_1534230881479"
			+ "&city_id=%s"
			+ "&group_type=%s"
			+ "&max_lat=%s"
			+ "&min_lat=%s"
			+ "&max_lng=%s"
			+ "&min_lng=%s"
			+ "&filters=%s"
			+ "&request_ts=%d"
			+ "&source=ljpc"
			+ "&authorization=%s"
			+ "&_=%d";
	public static final HashMap<String, String> headers = new LinkedHashMap<>();

	static {
		headers.put("Host", "ajax.lianjia.com");
		headers.put("Referer", "https://sh.lianjia.com/ditu/");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
	}

	public final String cookie = "lianjia_uuid=9bdccc1a-7584-4639-ba95-b42cf21bbbc7;" +
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

	String city_id, city_name, max_lat, min_lat, max_lng, min_lng;

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getMax_lat() {
		return max_lat;
	}

	public void setMax_lat(String max_lat) {
		this.max_lat = max_lat;
	}

	public String getMin_lat() {
		return min_lat;
	}

	public void setMin_lat(String min_lat) {
		this.min_lat = min_lat;
	}

	public String getMax_lng() {
		return max_lng;
	}

	public void setMax_lng(String max_lng) {
		this.max_lng = max_lng;
	}

	public String getMin_lng() {
		return min_lng;
	}

	public void setMin_lng(String min_lng) {
		this.min_lng = min_lng;
	}
}
