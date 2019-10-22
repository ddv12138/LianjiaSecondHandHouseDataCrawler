package ORM.Service;

import ORM.Mapper.HouseMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import org.apache.ibatis.session.SqlSession;

public class HouseService implements HouseMapper {

	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			HouseMapper mapper = session.getMapper(HouseMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			CommonUtils.Logger().error(e);
			throw e;
		}
	}
}
