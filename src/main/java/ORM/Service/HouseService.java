package ORM.Service;

import ORM.Mapper.HouseMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HouseService implements HouseMapper {
	private static final Logger logger = LogManager.getLogger(HouseMapper.class);

	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			HouseMapper mapper = session.getMapper(HouseMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
}
