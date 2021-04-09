package com.lagou.sqlSession;

import java.beans.IntrospectionException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

     <E> E selectOne(String statementId, Object... params) throws SQLException, ClassNotFoundException, IntrospectionException, InstantiationException, NoSuchFieldException, IllegalAccessException;

     <E> List<E> selectAll(String statementId, Object... params) throws SQLException, ClassNotFoundException, IntrospectionException, InstantiationException, IllegalAccessException, NoSuchFieldException;

     <T> T getMapper(Class<T> typeClass);

     Integer save(String statementId, Object... params) throws Exception;

    //修改
     Integer update(String statementId, Object... params) throws Exception;

    //删除
     Integer delete(String statementId, Object... params) throws Exception;

}
