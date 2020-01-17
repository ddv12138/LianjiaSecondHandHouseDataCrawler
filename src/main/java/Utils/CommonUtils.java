package Utils;

import Lianjia.Community;
import Lianjia.District;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CommonUtils {
	static final String cookie = "lianjia_uuid=9cdccc1a-7584-4639-ba95-b42cf21bbbc7;" +
			"jzqa=1.3180246719396510700.1534145942.1534145942.1534145942.1;" +
			"jzqckmp=1;" +
			"ga=GA1.2.964691746.1534145946;" +
			"gid=GA1.2.826685830.1534145946;" +
			"UM_distinctid=165327625186a-029cf60b1994ee-3461790f-fa000-165327625199d3;" +
			"select_city=420000;" +
			"lianjia_ssid=34fc4efa-7fcc-4f3f-82ae-010401f27aa8;" +
			"_smt_uid=5b72c5f7.5815bcdf;" +
			"Hm_lvt_9152f8221cb6243a53c83b956842be8a=1537530243;" +
			"select_city=420000;" +
			"_jzqc=1;" +
			"_gid=GA1.2.178601063.1541866763;" +
			"_jzqb=1.2.10.1541866760.1";
	private static final HashMap<String, String> headers = new LinkedHashMap<>();

	static {
		headers.put("Host", "ajax.lianjia.com");
		headers.put("Referer", "https://wh.lianjia.com/ditu/");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
	}

	public static final String url = "https://ajax.lianjia.com/map/search/ershoufang/?callback=jQuery1111012389114747347363_1534230881479"
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

	public static Logger Logger() {
		return LogManager.getRootLogger();
	}

	public static String getMD5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assert md != null;
		md.update(str.getBytes(StandardCharsets.UTF_8));
		byte[] secretBytes = md.digest();
		String md5code = new BigInteger(1, secretBytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++) md5code = "0" + md5code;
		return md5code;
	}

	public static String postHTTPRequest(String linkUrl) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(linkUrl);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
			Proxy proxy2 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("188.131.157.4", 8888));
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setUseCaches(false);
			for (String key : headers.keySet()) {
				connection.setRequestProperty(key, headers.get(key));
			}
//			connection.setRequestProperty("Cookie", cookie);
			connection.connect();
			String result = null;
			if (connection.getResponseCode() == 200) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					StringBuilder sb = new StringBuilder();
					String tmp;
					while (null != (tmp = br.readLine())) {
						sb.append(tmp);
					}
					result = sb.toString();
				}
			}
			assert result != null;
			String resStr = result.substring(result.indexOf("{"), result.lastIndexOf(")"));
			JSONObject res = JSON.parseObject(resStr);
			if (null != res && !res.isEmpty() && res.getIntValue("errno") == 10001 && res.getString("error").contains("data")) {
				CommonUtils.Logger().error("被封ip，重新连接");
				postHTTPRequest(linkUrl);
			}
			return resStr;
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
			CommonUtils.Logger().info("连接失败，重试中");
			if (null != connection) {
				connection.disconnect();
				connection = null;
			}
			postHTTPRequest(linkUrl);
		} finally {
			if (null != connection) {
				connection.disconnect();
			}
		}
		return null;
	}

	public static String getNormalAuthorization(HashMap dict) {
		String datastr = "vfkpbin1ix2rb88gfjebs0f60cbvhedlcity_id=%sgroup_type=%smax_lat=%s"
				+ "max_lng=%smin_lat=%smin_lng=%srequest_ts=%s";
		datastr = String.format(datastr,
				dict.get("city_id"), dict.get("group_type"), dict.get("max_lat"),
				dict.get("max_lng"), dict.get("min_lat"), dict.get("min_lng"), dict.get("request_ts"));
		return CommonUtils.getMD5(datastr);
	}

	public static List<BigDecimal> getStepRange(BigDecimal start, BigDecimal end, BigDecimal step) {
		List<BigDecimal> res = new LinkedList<>();
		do {
			res.add(start);
			start = start.add(step);
		} while (start.compareTo(end) < 0);
		return res;
	}

	public static boolean JSONResultCheck(JSONObject res) {
		if (null == res || res.isEmpty() || res.getIntValue("errno") != 0) {
			if (null != res && !res.isEmpty()) {
				CommonUtils.Logger().error(res.getString("error"));
			}
			return false;
		}
		return true;
	}

	public static boolean isCommunityInDistrict(Community community, District district) {
		BigDecimal pointLat = new BigDecimal(community.getLatitude());
		BigDecimal pointlng = new BigDecimal(community.getLongitude());
		List<BigDecimal> lat = new LinkedList<>();
		List<BigDecimal> lng = new LinkedList<>();
		Optional<String> border = Optional.of(district.getBorder());
		String[] borderPoints = border.get().split(";");
		for (String s : borderPoints) {
			lng.add(new BigDecimal(s.split(",")[0]));
			lat.add(new BigDecimal(s.split(",")[1]));
		}
		BigDecimal maxLat = Collections.max(lat);
		BigDecimal minLat = Collections.min(lat);

		BigDecimal maxLng = Collections.max(lng);
		BigDecimal minLng = Collections.min(lng);

		//初级判断
		boolean isLatValid = pointLat.compareTo(minLat) >= 0 && pointLat.compareTo(maxLat) <= 0;
		boolean isLngValid = pointlng.compareTo(minLng) >= 0 && pointlng.compareTo(maxLng) <= 0;
		if (!isLatValid || !isLngValid) {
			return false;
		}

		//PNPoly算法，也叫射线法，未处理测量点刚好在多边形边上的特殊情况
		boolean crossFlag = false;
		for (int i = 0, j = borderPoints.length - 1; i < borderPoints.length; j = i++) {
			boolean isPointInBetween = (lat.get(i).compareTo(pointLat) > 0) != (lat.get(j).compareTo(pointLat) > 0);
//			CommonUtils.Logger().info(lat.get(j)+"-"+lat.get(i)+"="+lat.get(j).subtract(lat.get(i)));
			if (lat.get(j).compareTo(lat.get(i)) == 0) continue;
			boolean isCrossLine = lng.get(j).subtract(lng.get(i))
					.multiply(pointLat.subtract(lat.get(i)))
					.divide(
							lat.get(j).subtract(lat.get(i))
							, 11, RoundingMode.UP).add(lng.get(i)).compareTo(pointlng) > 0;
			if (isPointInBetween && isCrossLine) crossFlag = !crossFlag;
		}
		return crossFlag;
	}
}
