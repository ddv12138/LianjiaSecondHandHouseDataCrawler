package ORM;

import Utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class UtilsTest {
	private static final Logger logger = LogManager.getLogger(ORMTest.class);

	@Test
	public void MD5Test() {
		try {
			logger.info(CommonUtils.getMD5("待加密字符串"));
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
}
