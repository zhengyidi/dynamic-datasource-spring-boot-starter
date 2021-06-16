package com.tz.datasource.dynamic.core;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tz.datasource.dynamic.config.DataSourcePropertiesWrapper;
import com.tz.datasource.dynamic.config.DataSourceProperty;
import com.tz.datasource.dynamic.config.SpringDataSourceWrapper;
import com.tz.datasource.dynamic.config.druid.DruidConfig;
import com.tz.datasource.dynamic.config.druid.DruidDataSourceCreator;
import com.tz.datasource.dynamic.route.DynamicDataSourceRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * 动态数据源自动配置类
 */
@Slf4j
@Configuration
@Import(SpringUtil.class)
@ConditionalOnProperty(value = DynamicDataSourceConstants.ENABLE_CONFIG, havingValue = "true")
@AutoConfigureBefore(value = DruidDataSourceAutoConfigure.class, name = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure")
@EnableConfigurationProperties(SpringDataSourceWrapper.class)
public class DynamicDataSourceAutoConfig {

    private final DataSourcePropertiesWrapper dataSourceProperties;

    private final ApplicationContext applicationContext;

    @Autowired
    public DynamicDataSourceAutoConfig(ApplicationContext applicationContext, SpringDataSourceWrapper dataSourceProperties) {
        this.applicationContext = applicationContext;
        this.dataSourceProperties = dataSourceProperties.getDataSource();
    }

    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        // 默认数据源信息
        DataSourceProperty defaultDataSourceProperty = dataSourceProperties;
        DruidConfig defaultDruidConfig = dataSourceProperties.getDruid();
        if (defaultDataSourceProperty.existsDataSource()) {
            defaultDataSourceProperty.setName("default");
            DataSource defaultDataSource = createDruidDataSource(defaultDruidConfig, defaultDataSourceProperty);
            dynamicDataSource.setResolvedDefaultDataSource(defaultDataSource);
        }
        return dynamicDataSource;
    }

    /**
     * Mybatis plus 分页处理类
     *
     * @return mybatis plus 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //如果是不同类型的库，请不要指定DbType，其会自动判断。
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 设置切换数据的切面
     *
     * @param datasourceRoute 动态切换数据源的路由逻辑实现
     * @return 切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DynamicDataSourceRoute.class)
    @ConditionalOnProperty(DynamicDataSourceConstants.POINTCUT_CONFIG)
    public Advisor configDynamicDatasourcePoint(@Autowired(required = false) DynamicDataSourceRoute datasourceRoute) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(dataSourceProperties.getDynamic().getPointcut());
        advisor.setAdvice(new DynamicDataSourceAspect(datasourceRoute));
        return advisor;
    }

    /**
     * 创建Druid数据源创建类
     *
     * @param druidConfig 读取到的Druid数据源信息
     * @return Druid数据源创建者
     */
    private DataSource createDruidDataSource(DruidConfig druidConfig, DataSourceProperty dataSourceProperty) {
        return new DruidDataSourceCreator(druidConfig, applicationContext).createDataSource(dataSourceProperty);
    }
}
