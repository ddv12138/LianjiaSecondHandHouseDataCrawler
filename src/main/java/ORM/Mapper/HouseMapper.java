package ORM.Mapper;

import Lianjia.House;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseMapper {
	void createTable();

	int bathInsertList(List<House> houseList);

	House selectByHouseId(@Param("houseId") String houseId);
}
