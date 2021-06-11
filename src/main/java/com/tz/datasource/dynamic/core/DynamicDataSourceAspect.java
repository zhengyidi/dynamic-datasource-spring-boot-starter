package com.tz.datasource.dynamic.core;

import com.tz.datasource.dynamic.route.DynamicDatasourceRoute;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 动态数据源切换aop切面
 *
 * @author zheng
 */
public class DynamicDataSourceAspect implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    private final DynamicDatasourceRoute datasourceRoute;

    public DynamicDataSourceAspect(DynamicDatasourceRoute datasourceRoute) {
        this.datasourceRoute = datasourceRoute;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        before(methodInvocation.getMethod());
        Object returnValue = methodInvocation.proceed();
        after(methodInvocation.getMethod());
        return returnValue;
    }

    /**
     * 切面前方法， 获取实现的数据源路由接口返回的数据源key，将其设置到当前线程中
     *
     * @param method 切面方法
     */
    public void before(Method method) {
        String datasourceRouteKey = datasourceRoute.getDatasourceRouteKey();
        DynamicDataSourceContextHolder.setDataSourceKey(datasourceRouteKey);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Change DataSource to [{}] in Method [{}].", datasourceRouteKey, method);
        }
    }

    /**
     * 切面后事件，将当前线程中的key清空掉，避免内存泄漏
     *
     * @param method 切面方法
     */
    public void after(Method method) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Restore DataSource to [{}] in Method [{}]", DynamicDataSourceContextHolder.getDataSourceKey(), method.getName());
        }
    }
}
