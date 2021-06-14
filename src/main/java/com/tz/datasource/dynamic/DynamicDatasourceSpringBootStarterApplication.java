package com.tz.datasource.dynamic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author zheng
 */
@SpringBootApplication
@MapperScan("com.tz.datasource.dynamic.config.reader.db")
public class DynamicDatasourceSpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceSpringBootStarterApplication.class, args);
    }

}
