package com.tz.datasource.dynamic.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态路由添加映射数据源
 *
 * @author zheng
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractDataSource {

    private static final String DEFAULT_DATASOURCE_NAME = "default_datasource_name";

    /**
     * 处理的数据源映射map
     */
    private final Map<Object, DynamicDataSource> resolvedDataSources = new ConcurrentHashMap<>();

    /**
     * 默认数据源信息. 如果不能根据 {@link #determineCurrentLookupKey()} 获取到数据源信息, 默认数据源将会被使用。
     */
    private DynamicDataSource resolvedDefaultDataSource;

    /**
     * 决定当前使用数据源的key
     *
     * @return 通过调用 {@link #addDataSource(Object, DataSource)} 方法设置的name值
     */
    @Nullable
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    /**
     * 通过当前数据源返回的key 信息获取数据源
     * {@link #determineCurrentLookupKey() current lookup key}
     *
     * @see #determineCurrentLookupKey()
     */
    protected DataSource determineTargetDataSource() {
        Object lookupKey = determineCurrentLookupKey();
        if (lookupKey == null) {
            return this.resolvedDefaultDataSource;
        }
        DataSource dataSource = this.resolvedDataSources.getOrDefault(lookupKey, this.resolvedDefaultDataSource);
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for name [" + lookupKey + "].");
        }
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    public Map<Object, DataSource> getResolvedDataSources() {
        return Collections.unmodifiableMap(this.resolvedDataSources);
    }

    public DataSource getResolvedDefaultDataSource() {
        return resolvedDefaultDataSource;
    }

    public void setResolvedDefaultDataSource(DataSource resolvedDefaultDataSource) {
        this.resolvedDefaultDataSource = resolveDataSource(DEFAULT_DATASOURCE_NAME, resolvedDefaultDataSource);
    }

    /**
     * 动态添加数据源信息
     *
     * @param name       数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(Object name, DataSource dataSource) {
        DynamicDataSource source = this.resolveDataSource(name, dataSource);
        // 将数据源信息放入map中
        DynamicDataSource oldDataSource = this.resolvedDataSources.put(name, source);
        // 清除旧数据源信息
        disconnectDataSource(oldDataSource);
        log.info("DataSource [{}] added success.", name);
    }

    /**
     * 动态移除数据源信息
     *
     * @param name 数据源名称
     */
    public synchronized void removeDataSource(Object name) {
        DynamicDataSource dataSource = this.resolvedDataSources.get(name);
        if (dataSource == null) {
            log.warn("Could not find DataSource {} while removed DataSource.", name);
            return;
        }
        disconnectDataSource(dataSource);
        log.info("DataSource [{}] removed success.", name);
    }

    private DynamicDataSource resolveDataSource(Object name, DataSource dataSource) {
        return new DynamicDataSource(name, dataSource);
    }

    private void disconnectDataSource(DynamicDataSource dataSource) {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
