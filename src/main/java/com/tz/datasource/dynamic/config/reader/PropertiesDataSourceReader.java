package com.tz.datasource.dynamic.config.reader;

import com.tz.datasource.dynamic.config.DataSourceProperty;
import com.tz.datasource.dynamic.config.SpringDataSourceWrapper;
import com.tz.datasource.dynamic.core.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PropertiesDataSourceReader implements DataSourceConfigurationReader {

    @Override
    public Map<Object, DataSourceProperty> getDynamicDataSources() {
        SpringDataSourceWrapper dataSourcePropertiesWrapper = SpringUtil.getBean(SpringDataSourceWrapper.class);
        return dataSourcePropertiesWrapper.getDataSource().getDynamic().getDataSources();
    }

}
