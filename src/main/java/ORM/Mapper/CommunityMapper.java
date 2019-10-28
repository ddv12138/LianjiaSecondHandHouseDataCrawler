package ORM.Mapper;

import Lianjia.Community;
import Lianjia.District;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommunityMapper {
	void createTable();

	int bathInsertList(List<Community> communities);

	Community selectByName(@Param("name") String name);

	List<Community> selectAll();

	List<Community> selectByDistrict(District district);
}
