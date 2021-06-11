package com.tz.datasource.dynamic.config;

import com.tz.datasource.dynamic.config.druid.DruidConfig;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 数据源配置信息
 *
 * @author zheng
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

    /**
     * 动态切换数据源
     * <pre>
     *     读取spring.datasource.dynamic 下的数据信息
     * </pre>
     */
    @NestedConfigurationProperty
    private Map<String, DataSourceProperty> dynamic;

    /**
     * 设置动态数据源切换的切点
     * <pre>
     *     读取spring.datasource.pointcut 值
     * </pre>
     */
    @NestedConfigurationProperty
    private String pointcut;

    public DruidConfig getDruid() {
        return druid == null ? new DruidConfig() : druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
    }

    public Map<String, DataSourceProperty> getDynamic() {
        return dynamic;
    }

    public void setDynamic(Map<String, DataSourceProperty> dynamic) {
        this.dynamic = dynamic;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
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
