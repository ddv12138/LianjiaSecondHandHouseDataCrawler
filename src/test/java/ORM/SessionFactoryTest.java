package ORM;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;


public class SessionFactoryTest {
	private static final Logger logger= LogManager.getLogger(SessionFactoryTest.class);
	@Test
	public void getSqlSessionTest(){
		SqlSessionFactory factory =  SqlliteSqlSessionFactoryBuilder.builder.sqlSessionFactory;
		SqlSession session = factory.openSession();
		logger.info(session.selectOne("org.mybatis.HouseMapper.createHouseDeatilTable","house").toString());
	}
}
