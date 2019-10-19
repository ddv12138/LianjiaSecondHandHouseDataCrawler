package ORM.Service;

import ORM.Mapper.HouseDAO;
import ORM.SqlliteSqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HouseService implements HouseDAO {
	private static final Logger logger = LogManager.getLogger(HouseDAO.class);

	@Override
	public void createTable(String tableName) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			HouseDAO mapper = session.getMapper(HouseDAO.class);
			mapper.createTable("house");
			session.commit();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
}
