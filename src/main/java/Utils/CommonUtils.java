package Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommonUtils {
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
	static final HashMap<String, String> headers = new LinkedHashMap<>();
	private static final Logger logger = LogManager.getLogger(CommonUtils.class);

	static {
		headers.put("Host", "ajax.lianjia.com");
		headers.put("Referer", "https://sh.lianjia.com/ditu/");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
	}

	public static String getMD5(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(StandardCharsets.UTF_8));
		byte[] secretBytes = md.digest();
		String md5code = new BigInteger(1, secretBytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	public static String postHTTPRequest(String linkurl) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(linkurl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(60000);
			for (String key : headers.keySet()) {
				connection.setRequestProperty(key, headers.get(key));
			}
			connection.setRequestProperty("Cookie", cookie);
			connection.connect();
			String result = null;
			if (connection.getResponseCode() == 200) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					StringBuffer sb = new StringBuffer();
					String tmp = null;
					while (null != (tmp = br.readLine())) {
						sb.append(tmp);
					}
					result = sb.toString();
				}
			}
			return result.substring(result.indexOf("{"), result.lastIndexOf(")"));
		} finally {
			if (null != connection) {
				connection.disconnect();
			}
		}
	}

	public static String getAuthorization(HashMap dict) throws NoSuchAlgorithmException {
		String datastr = "vfkpbin1ix2rb88gfjebs0f60cbvhedlcity_id=%sgroup_type=%smax_lat=%s"
				+ "max_lng=%smin_lat=%smin_lng=%srequest_ts=%s";
		datastr = String.format(datastr,
				dict.get("city_id"), dict.get("group_type"), dict.get("max_lat"),
				dict.get("max_lng"), dict.get("min_lat"), dict.get("min_lng"), dict.get("request_ts"));
		return CommonUtils.getMD5(datastr);
	}
}
