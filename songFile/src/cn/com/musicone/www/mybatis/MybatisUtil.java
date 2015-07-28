/**
 * 
 */
package cn.com.musicone.www.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Mybatis初始化
 * @author Jelly.Liu
 *
 */
public class MybatisUtil {
	private static final Logger logger = LogManager.getLogger(MybatisUtil.class);
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	
	/**
	 * Mybatis 数据库配置初始化
	 * @throws IOException 
	 */
	public static void init() throws IOException{
		reader = Resources.getResourceAsReader("Configuration.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	}
	/**
	 * 获取sqlSessionFactory
	 * @return
	 */
	public static SqlSessionFactory getSqlSessionFactory(){
		return sqlSessionFactory;
	}
	
	/**
	 * 获取SqlSession
	 * @return
	 */
	public static SqlSession getSqlSession(){
		if(sqlSessionFactory == null){
			return null;
		}
		return sqlSessionFactory.openSession(true);
	}
	
	/**
	 * 关闭session
	 * @param session
	 */
	public static void closeSqlSession(SqlSession session){
		if(session == null){
			return;
		}
		session.close();
	}
}
