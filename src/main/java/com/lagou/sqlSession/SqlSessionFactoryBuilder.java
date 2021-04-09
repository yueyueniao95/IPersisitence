package com.lagou.sqlSession;

import com.lagou.config.XMLConfigBuilder;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class SqlSessionFactoryBuilder {

    public static SqlSessionFactory build(InputStream inputStream) throws PropertyVetoException, DocumentException {

        //第一步解析xml封装核心配置容器
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);

        //第二部创建sqlsessionfactory对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }

}
