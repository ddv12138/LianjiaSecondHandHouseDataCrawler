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

public class CommonUtils {
	private static final Logger logger = LogManager.getLogger(CommonUtils.class);

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

	public static String postHTTPRequest(String linkurl, HashMap<String, String> headers, String cookie) throws IOException {
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
			logger.info(url);
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
}
