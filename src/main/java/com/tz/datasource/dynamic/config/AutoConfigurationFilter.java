package com.tz.datasource.dynamic.config;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

import java.util.Collections;
import java.util.List;

/**
 * 自动配置过滤
 */
public class AutoConfigurationFilter implements AutoConfigurationImportFilter {

    private static final List<String> FILTER_LIST = Collections.singletonList("org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration");

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] result = new boolean[autoConfigurationClasses.length];

        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            result[i] = !FILTER_LIST.contains(autoConfigurationClasses[i]);
        }
        return result;
    }
}
