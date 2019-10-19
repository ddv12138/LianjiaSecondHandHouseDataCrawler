package Lianjia;

public class City {
	static String url_fang = "https://ajax.lianjia.com/map/resblock/ershoufanglist/?callback=jQuery11110617424919783834_1541868368031"
			+ "&id=%s"
			+ "&order=0"
			+ "&page=%d"
			+ "&filters=%s"
			+ "&request_ts=%d"
			+ "&source=ljpc"
			+ "&authorization=%s"
			+ "&_=%d";
	static String url = "https://ajax.lianjia.com/map/search/ershoufang/?callback=jQuery1111012389114747347363_1534230881479"
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
	String city_id, city_name, max_lat, min_lat, max_lng, min_lng;
}
