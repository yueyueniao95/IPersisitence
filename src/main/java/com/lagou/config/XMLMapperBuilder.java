package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration){
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();

        List<Element> list = rootElement.selectNodes("//select");

        for (Element element : list) {
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(element.attributeValue("id"));
            mappedStatement.setSql(element.getTextTrim());
            mappedStatement.setParamterType(element.attributeValue("paramterType"));
            mappedStatement.setResultType(element.attributeValue("resultType"));
            String key = rootElement.attributeValue("namespace")+"."+mappedStatement.getId();
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }

    }

}
