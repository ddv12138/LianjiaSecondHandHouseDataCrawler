package ORM.Mapper;

import Lianjia.District;

import java.util.List;

public interface DistrictMapper {
	void createTable();

	int bathInsertList(List<District> districts);
}
