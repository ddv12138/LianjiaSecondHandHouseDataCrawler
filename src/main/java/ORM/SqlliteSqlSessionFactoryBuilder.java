package ORM;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlliteSqlSessionFactoryBuilder {
	static SqlliteSqlSessionFactoryBuilder builder;

	static {
		try {
			builder = new SqlliteSqlSessionFactoryBuilder();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String resource = "ORM/mybatis-config.xml";
	InputStream inputStream = Resources.getResourceAsStream(resource);
	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

	private SqlliteSqlSessionFactoryBuilder() throws IOException {
	}

	public static SqlSession getSession() {
		return builder.sqlSessionFactory.openSession();
	}
}
