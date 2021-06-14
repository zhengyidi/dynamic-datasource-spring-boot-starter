package com.tz.datasource.dynamic.core;

import javax.sql.DataSource;

/**
 * 动态数据源管理工具
 */
public class DynamicDataSourceManager {

    private DynamicDataSourceManager() {
    }

    private static final DynamicRoutingDataSource DATA_SOURCES = SpringUtil.getBean("dataSource", DynamicRoutingDataSource.class);

    /**
     * 添加数据源
     *
     * @param name       数据源key
     * @param dataSource 数据源信息
     */
    public static void addDataSource(Object name, DataSource dataSource) {
        DATA_SOURCES.addDataSource(name, dataSource);
    }

    /**
     * 删除数据源
     *
     * @param name 数据源key
     */
    public static void removeDataSource(Object name) {
        DATA_SOURCES.removeDataSource(name);
    }

}
