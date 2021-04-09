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

        //增加修改删除更新标签解析
        List<Element> deleteList = rootElement.selectNodes("//delete");
        List<Element> saveList = rootElement.selectNodes("//save");
        List<Element> updateList = rootElement.selectNodes("//update");

        list.addAll(deleteList);
        list.addAll(saveList);
        list.addAll(updateList);

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
