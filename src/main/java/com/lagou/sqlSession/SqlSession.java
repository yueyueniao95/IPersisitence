package com.lagou.sqlSession;

import java.beans.IntrospectionException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    public <E> E selectOne(String statementId, Object... params) throws SQLException, ClassNotFoundException, IntrospectionException, InstantiationException, NoSuchFieldException, IllegalAccessException;

    public <E> List<E> selectAll(String statementId, Object... params) throws SQLException, ClassNotFoundException, IntrospectionException, InstantiationException, IllegalAccessException, NoSuchFieldException;

}
