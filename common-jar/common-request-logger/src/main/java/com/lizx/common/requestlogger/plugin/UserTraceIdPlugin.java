package com.lizx.common.requestlogger.plugin;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 用户traceId插件
 *
 * @author lizexin
 * @date 2021-06-07 15:52
 */
public interface UserTraceIdPlugin {

    /**
     * 用户自定义tracedId（http接口）
     *
     * @param httpServletRequest
     * @return
     */
    default String httpUserTraceId(HttpServletRequest httpServletRequest) {
        return null;
    }

    /**
     * 用户自定义tracedId（mqtt接口）
     *
     * @param mqttMessage
     * @return
     */
    default String mqttUserTraceId(String mqttMessage) {
        return null;
    }

    /**
     * 用户自定义tracedId（thrift接口）
     *
     * @param method
     * @param args
     * @return
     */
    default String thriftUserTraceId(Method method, Object[] args) {
        return null;
    }

}
