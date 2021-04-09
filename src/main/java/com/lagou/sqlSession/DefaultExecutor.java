package com.lagou.sqlSession;

import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultExecutor implements  Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException {

        //获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //获取sql
        String sql = mappedStatement.getSql();

        //对sql进行解析， 1.将自定义#{}占位符用JDBC定义占位符？替换， 2.获取#{}里面参数的值， 用于sql预处理时候设置参数
        BoundSql boundSql = getBoundSql(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();

        Class<?> paramClass = Class.forName(mappedStatement.getParamterType());

        for (int i = 0; i < parameterMappingList.size(); i++) {
            Field declaredField = paramClass.getDeclaredField(parameterMappingList.get(i).getContent());
            declaredField.setAccessible(Boolean.TRUE);
            Object value = declaredField.get(params[0]);
            preparedStatement.setObject(i+1, value);
        }

        //参数封装完执行sql获取结果集
        ResultSet resultSet = preparedStatement.executeQuery();

        //获取返回结果封装对象
        Class<?> resultClass = Class.forName(mappedStatement.getResultType());

        ArrayList<Object> objects = new ArrayList<Object>();
        //封装结果集
        while (resultSet.next()){
            ResultSetMetaData metaData = resultSet.getMetaData();
            Object o = resultClass.newInstance();
            for (int i = 1; i <= metaData.getColumnCount() ; i++) {
                //依次获取字段名
                String columnName = metaData.getColumnName(i);
                //字段名对应的字段值
                Object object = resultSet.getObject(columnName);

                Field declaredField = resultClass.getDeclaredField(columnName);
                declaredField.setAccessible(true);
                declaredField.set(o, object);
            }
            objects.add(o);
        }
        return (List<E>) objects;
    }

    public Integer executeUpdate(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //获取sql
        String sql = mappedStatement.getSql();

        //对sql进行解析， 1.将自定义#{}占位符用JDBC定义占位符？替换， 2.获取#{}里面参数的值， 用于sql预处理时候设置参数
        BoundSql boundSql = getBoundSql(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();

        Class<?> paramClass = Class.forName(mappedStatement.getParamterType());

        for (int i = 0; i < parameterMappingList.size(); i++) {
            Field declaredField = paramClass.getDeclaredField(parameterMappingList.get(i).getContent());
            declaredField.setAccessible(Boolean.TRUE);
            Object value = declaredField.get(params[0]);
            preparedStatement.setObject(i+1, value);
        }

        //参数封装完执行sql获取结果集
        int i = preparedStatement.executeUpdate();
        return  i;
    }

    private BoundSql getBoundSql(String sql){

        //标记处理类：主要是配合通⽤标记解析器GenericTokenParser类完成对配置⽂件等的解析⼯作，其中TokenHandler主要完成处理
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //GenericTokenParser :通⽤的标记解析器，完成了代码⽚段中的占位符的解析，然后再根据给定的标记处理器(TokenHandler)来进⾏表达式的处理
        //三个参数：分别为openToken (开始标记)、closeToken (结束标记)、handler (标记处 理器)
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parse = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parse, parameterMappings);
        return boundSql;
    }

}
