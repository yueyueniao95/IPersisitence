package com.lagou.config;

import com.lagou.io.Resoures;
import com.lagou.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {

        Configuration configuration = new Configuration();

        Document document = new SAXReader().read(inputStream);

        //解析核心配置文件数据库连接部分
        Element rootElement = document.getRootElement();
        List<Element> propertyElements = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element propertyElement : propertyElements) {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.setProperty(name,value);
        }

        //连接池
        ComboPooledDataSource comboPooledDataSource = new
                ComboPooledDataSource();

        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        //填充 configuration
        configuration.setDataSource(comboPooledDataSource);

        //解析核心配置文件mapper部分封装到mappedstatement
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
        for (Element mapperElement : mapperElements) {
            xmlMapperBuilder.parse(Resoures.getInputStream(mapperElement.attributeValue("resource")));
        }

        return  configuration;
    }

}
