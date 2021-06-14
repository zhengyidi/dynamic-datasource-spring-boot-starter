package com.tz.datasource.dynamic.config;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Collections;
import java.util.Map;

/**
 * 读取动态数据源设置配置信息
 */
public class DynamicDataSourceConfig {

    private String pointcut;

    private boolean enable;

    @NestedConfigurationProperty
    private Map<Object, DataSourceProperty> dataSources;

    public String getPointcut() {
        return pointcut;
    }

    /**
     * 设置动态数据源切点
     *
     * @param pointcut 切换数据源的切点
     */
    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public boolean isEnable() {
        return enable;
    }

    /**
     * 设置是否启用动态数据源设置
     *
     * @param enable true 启用， false 不启用
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<Object, DataSourceProperty> getDataSources() {
        return dataSources == null ? Collections.emptyMap() : dataSources;
    }

    public void setDataSources(Map<Object, DataSourceProperty> dataSources) {
        this.dataSources = dataSources;
    }
}
