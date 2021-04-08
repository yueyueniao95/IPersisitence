package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;

import java.beans.IntrospectionException;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements  SqlSession{

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration){
        this.configuration = configuration;
    }

    public <E> E selectOne(String statementId, Object... params) throws SQLException, ClassNotFoundException, IntrospectionException, InstantiationException, NoSuchFieldException, IllegalAccessException {
        List<E> list = selectAll(statementId, params);
        if(list.size() == 1){
            return  list.get(0);
        }else{
            throw  new RuntimeException("不存在数据或者存在多条数据");
        }

    }

    public <E> List<E> selectAll(String statementId, Object... params) throws SQLException, ClassNotFoundException, IntrospectionException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        List<Object> list = defaultExecutor.query(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return (List<E>) list;
    }
}
