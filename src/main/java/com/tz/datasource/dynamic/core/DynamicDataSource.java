package com.tz.datasource.dynamic.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 动态数据源
 *
 * @author zheng
 */
@Slf4j
public class DynamicDataSource extends AbstractDataSource implements Closeable {

    private Object lookupKey;

    private DataSource dataSource;

    public DynamicDataSource(Object lookupKey, DataSource dataSource) {
        this.lookupKey = lookupKey;
        this.dataSource = dataSource;
    }

    public Object getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(Object lookupKey) {
        this.lookupKey = lookupKey;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }


    @Override
    public void close() {
        Class<? extends DataSource> clazz = dataSource.getClass();
        try {
            Method closeMethod = clazz.getDeclaredMethod("close");
            closeMethod.invoke(dataSource);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.warn("Close the datasource named [{}] failed,", this.lookupKey, e);
        }
    }
}
