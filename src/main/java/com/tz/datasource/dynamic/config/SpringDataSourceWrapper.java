package com.tz.datasource.dynamic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "spring")
public class SpringDataSourceWrapper {

    @NestedConfigurationProperty
    private DataSourcePropertiesWrapper dataSource;

    public DataSourcePropertiesWrapper getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourcePropertiesWrapper dataSource) {
        this.dataSource = dataSource;
    }
}
