package com.tz.datasource.dynamic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 默认数据源包装类
 * <pre>
 *     将spring原生数据源读取以及druid设置，动态数据源的设置进行包装使用
 *     即读取：
 *     spring:
 *       datasource:
 *         url: ...
 *         username: ...
 *         druid:
 *           ...
 *         dynamic:
 *           enable: ...
 *           pointcut: ...
 *           data-sources:
 *              dbname:
 *                url: ...
 *                username: ...
 * </pre>
 */
//@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourcePropertiesWrapper extends DataSourceProperty {

    @NestedConfigurationProperty
    private DynamicDataSourceConfig dynamic;

    public DynamicDataSourceConfig getDynamic() {
        return dynamic;
    }

    public void setDynamic(DynamicDataSourceConfig dynamic) {
        this.dynamic = dynamic;
    }
}
