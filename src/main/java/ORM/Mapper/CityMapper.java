package ORM.Mapper;

import Lianjia.City;
import org.apache.ibatis.annotations.Param;

public interface CityMapper {
	void createTable();

	int insert(City city);

	City selectByName(@Param("city_name") String city_name);

	int initDefaultData();
}
