package com.tz.datasource.dynamic.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.tz.datasource.dynamic.config.druid.DruidConfig;
import com.tz.datasource.dynamic.config.druid.DruidDataSourceCreator;
import com.tz.datasource.dynamic.config.reader.DataSourceConfigurationReader;
import com.tz.datasource.dynamic.core.DynamicDataSourceConstants;
import com.tz.datasource.dynamic.core.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * 动态数据源初始化加载
 */
@Configuration
@AutoConfigureAfter(value = MybatisPlusAutoConfiguration.class, name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
@Slf4j
public class DynamicDataSourceLoader {

    private final SpringDataSourceWrapper dataSourcePropertiesWrapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public DynamicDataSourceLoader(SpringDataSourceWrapper dataSourcePropertiesWrapper, ApplicationContext applicationContext) {
        this.dataSourcePropertiesWrapper = dataSourcePropertiesWrapper;
        this.applicationContext = applicationContext;
    }

    @Bean
    public void load() {
        // 加载动态数据源读取类的所有实现
        Map<String, DataSourceConfigurationReader> dataSourceReaders = applicationContext.getBeansOfType(DataSourceConfigurationReader.class);
        DynamicRoutingDataSource dataSources = applicationContext.getBean("dataSource", DynamicRoutingDataSource.class);
        DruidConfig defaultDruidConfig = dataSourcePropertiesWrapper.getDataSource().getDruid();
        dataSourceReaders.values().stream().map(DataSourceConfigurationReader::getDynamicDataSources).filter(Objects::nonNull).forEach(dynamicDataSources ->
                dynamicDataSources.forEach((name, dataSourceProperty) -> {
                    if (!dataSourceProperty.existsDataSource()) {
                        log.warn("{} datasource [{}] loaded failed what without config in [url, username, password, driverClassName], please check configuration.", DynamicDataSourceConstants.LOG_PREFIX, name);
                        return;
                    }
                    dataSourceProperty.setName(name.toString());
                    DataSource dataSource = new DruidDataSourceCreator(defaultDruidConfig, applicationContext).createDataSource(dataSourceProperty);
                    dataSources.addDataSource(name, dataSource);
                })
        );
    }

}
