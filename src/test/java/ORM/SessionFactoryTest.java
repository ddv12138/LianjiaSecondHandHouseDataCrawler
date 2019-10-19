package ORM;

import ORM.Service.HouseService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;


public class SessionFactoryTest {
	private static final Logger logger= LogManager.getLogger(SessionFactoryTest.class);
	@Test
	public void getSqlSessionTest(){
		HouseService service = new HouseService();
		try {
			service.createTable("house");
		} catch (Exception e) {
			logger.info("数据表已存在");
		}
	}
}
