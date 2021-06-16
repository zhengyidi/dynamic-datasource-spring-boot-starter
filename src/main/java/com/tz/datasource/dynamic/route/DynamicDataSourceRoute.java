package com.tz.datasource.dynamic.route;

/**
 * 获取需要切转到数据源的key 接口
 * <pre>
 *     实现时需要使用@Service注解
 * </pre>
 *
 * @author zheng
 */
public interface DynamicDataSourceRoute {

    String NAME = "com.tz.datasource.dynamic.route.DynamicDatasourceRoute";

    /**
     * 获取当前需要连接的数据源的key
     *
     * @return 数据源的key [default, 或配置在spring.datasource.dynamic下的key]
     */
    Object getDatasourceRouteKey();

}
