package com.tz.datasource.dynamic.exception;

/**
 * 数据源不存在异常
 */
public class DataSourceNotExistsException extends RuntimeException {

    public DataSourceNotExistsException() {
        super();
    }

    public DataSourceNotExistsException(String message) {
        super(message);
    }

    public DataSourceNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceNotExistsException(Throwable cause) {
        super(cause);
    }

    protected DataSourceNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
