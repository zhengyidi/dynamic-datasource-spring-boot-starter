package com.tz.datasource.dynamic.config.reader;

import com.tz.datasource.dynamic.config.DataSourceProperty;

import java.util.Map;

/**
 * 动态数据源配置读取接口
 */
public interface DataSourceConfigurationReader {

    /**
     * 获取动态数据源
     *
     * @return 动态数据源集合
     */
    Map<Object, DataSourceProperty> getDynamicDataSources();

}
