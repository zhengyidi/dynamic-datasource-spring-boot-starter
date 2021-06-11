package com.tz.datasource.dynamic.config;

import com.tz.datasource.dynamic.config.druid.DruidConfig;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;

/**
 * 多数据源信息
 */
public class DataSourceProperty extends DataSourceProperties {

    /**
     * druid数据源配置信息
     * <pre>
     *     读取 spring.datasource.druid 配置
     * </pre>
     */
    @NestedConfigurationProperty
    private DruidConfig druid;


    public DruidConfig getDruid() {
        return druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
    }

    /**
     * 判断是否存在数据源信息
     *
     * @return 数据源信息
     */
    public boolean existsDataSource() {
        return StringUtils.hasText(getUrl()) &&
                StringUtils.hasText(getUsername()) &&
                StringUtils.hasText(getPassword()) &&
                StringUtils.hasText(getDriverClassName());
    }
}
