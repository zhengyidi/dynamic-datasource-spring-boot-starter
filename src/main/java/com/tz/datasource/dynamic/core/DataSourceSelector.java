package com.tz.datasource.dynamic.core;

import com.tz.datasource.dynamic.exception.DataSourceNotExistsException;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据源选择器
 */
@Slf4j
public class DataSourceSelector {

    private DataSourceSelector() {
    }

    /**
     * 选择数据源
     *
     * @param dataSourceKey 数据源key
     * @throws DataSourceNotExistsException 当未找到数据源时抛出
     */
    public static void selectDataSource(Object dataSourceKey) {
        // check dataSource key exists
        DynamicRoutingDataSource dataSource = SpringUtil.getBean("dataSource", DynamicRoutingDataSource.class);
        boolean dataSourceExists = dataSource.getResolvedDataSources().containsKey(dataSourceKey);
        if (!dataSourceExists) {
            throw new DataSourceNotExistsException(String.format("%s DataSource [%s] is not exists.", DynamicDataSourceConstants.LOG_PREFIX, dataSourceKey));
        }

        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceKey);
        if (log.isDebugEnabled()) {
            log.debug("{} Switch to dataSource [{}] success.", DynamicDataSourceConstants.LOG_PREFIX, dataSourceKey);
        }
    }

    /**
     * 选择默认数据源
     */
    public static void selectDefaultDataSource() {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        if (log.isDebugEnabled()) {
            log.debug("{} Switch to default dataSource success.", DynamicDataSourceConstants.LOG_PREFIX);
        }
    }

}
