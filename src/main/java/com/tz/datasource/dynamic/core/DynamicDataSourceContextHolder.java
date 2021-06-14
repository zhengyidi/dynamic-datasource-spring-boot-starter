package com.tz.datasource.dynamic.core;

/**
 * 线程数据源
 *
 * @author zheng
 */
public class DynamicDataSourceContextHolder {

    private DynamicDataSourceContextHolder() {
    }

    /**
     * 线程私有容器，存储当前线程的数据源key
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 选择当前的数据源
     *
     * @param key 数据源key
     */
    static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 获取当前线程的数据源key
     *
     * @return 获取当前线程的数据源key
     */
    static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * To set DataSource as default
     */
    static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}
