package ORM.Mapper;

import org.apache.ibatis.annotations.Param;

public interface HouseDAO {
	void createTable(@Param("tableName") String tableName);
}
