package ORM.Mapper;

import Lianjia.City;
import Lianjia.District;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DistrictMapper {
	void createTable();

	int bathInsertList(List<District> districts);

	District selectByName(String name);

	int deleteByCityId(@Param("city_id") String city_id);

	List<District> selectByCity(City city);
}
