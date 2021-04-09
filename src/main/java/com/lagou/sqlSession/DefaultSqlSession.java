package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

    public <T> T getMapper(Class<T> typeClass) {
        Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{typeClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String key = className+"."+methodName;

                if(methodName.contains("delete")){
                     return  delete(key, args);
                }else if (methodName.contains("update")){
                    return  update(key, args);
                }else if(methodName.contains("selectAll")){
                    return  selectAll(key, args);
                }else if(methodName.contains("selectOne")){
                    return  selectOne(key, args);
                }else if(methodName.contains("save")){
                    return save(key, args);
                }
                else {
                    return null;
                }
            }
        });
        return (T) o;
    }

    public Integer save(String statementId, Object... params) throws Exception {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        Integer count = defaultExecutor.executeUpdate(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return  count;
    }

    public Integer update(String statementId, Object... params) throws Exception {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        Integer count = defaultExecutor.executeUpdate(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return  count;
    }

    public Integer delete(String statementId, Object... params) throws Exception {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        Integer count = defaultExecutor.executeUpdate(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return count;
    }
}
