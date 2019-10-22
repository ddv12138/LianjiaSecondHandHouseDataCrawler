package ORM.Service;

import Lianjia.City;
import ORM.Mapper.CityMapper;
import ORM.SqlliteSqlSessionFactoryBuilder;
import Utils.CommonUtils;
import org.apache.ibatis.session.SqlSession;

public class CityService implements CityMapper {
	@Override
	public void createTable() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			CityMapper mapper = session.getMapper(CityMapper.class);
			mapper.createTable();
			session.commit();
		} catch (Exception e) {
			CommonUtils.Logger().error("创建表失败,表可能已经存在");
			throw e;
		}
	}

	@Override
	public int insert(City city) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			CityMapper mapper = session.getMapper(CityMapper.class);
			mapper.createTable();
			int res = mapper.insert(city);
			session.commit();
			return res;
		} catch (Exception e) {
			CommonUtils.Logger().error("数据插入失败，数据可能已经存在");
			throw e;
		}
	}

	@Override
	public City selectByName(String city_name) {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			CityMapper mapper = session.getMapper(CityMapper.class);
			mapper.createTable();
			City res = mapper.selectByName(city_name);
			session.commit();
			return res;
		} catch (Exception e) {
			CommonUtils.Logger().error("数据查询失败，数据可能不存在");
			throw e;
		}
	}

	@Override
	public int initDefaultData() {
		try (SqlSession session = SqlliteSqlSessionFactoryBuilder.getSession()) {
			CityMapper mapper = session.getMapper(CityMapper.class);
			mapper.createTable();
			int res = mapper.initDefaultData();
			session.commit();
			return res;
		} catch (Exception e) {
			CommonUtils.Logger().error("数据插入失败，数据可能已经存在");
			throw e;
		}
	}

}
