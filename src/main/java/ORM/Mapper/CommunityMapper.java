package ORM.Mapper;

import Lianjia.Community;

import java.util.List;

public interface CommunityMapper {
	void createTable();

	int bathInsertList(List<Community> communities);
}
