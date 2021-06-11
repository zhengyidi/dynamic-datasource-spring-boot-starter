package com.tz.datasource.dynamic.config;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

public class DynamicDataSourceConfig {

    private String pointcut;

    private boolean enable;

    @NestedConfigurationProperty
    private Map<Object, DataSourceProperty> dataSources;

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<Object, DataSourceProperty> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<Object, DataSourceProperty> dataSources) {
        this.dataSources = dataSources;
    }
}
