package com.lizx.common.requestlogger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.lizx.common.requestlogger.http.RepeatedlyReadRequest;
import com.lizx.common.requestlogger.util.TraceIdUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Description: 请求监控上报内容
 *
 * @author Lizexin
 * @date 2020-01-20 10:52
 */
@Data
public class RequestReportContent {
    /**
     * 接口类型
     */
    @JSONField(ordinal = 1)
    private RequestTypeEnum requestType;
    /**
     * 踪迹编号
     */
    @JSONField(ordinal = 2)
    private String traceId;
    /**
     * 接口详情
     */
    @JSONField(ordinal = 3)
    private InterfaceDetail interfaceDetail;
    /**
     * http请求参数
     * key: [httpQueryString, httpRequestHeaders, httpRequestBody]
     * value: 对应的值
     * <p>
     * thrift请求参数
     * key: thriftParams
     * value: {paramType paramName: paramValue}
     * <p>
     * mqtt请求参数
     * key: mqttMessage
     * value: messageContent
     */
    @JSONField(ordinal = 4)
    private Map<String, String> requestParams;
    /**
     * 响应结果
     */
    @JSONField(ordinal = 5)
    private String responseResult;
    /**
     * 发生错误时的堆栈信息
     */
    @JSONField(ordinal = 6)
    private String throwableInfo;
    /**
     * 请求时间
     */
    @JSONField(ordinal = 7)
    private long requestTime;
    /**
     * 接口耗时|单位：毫秒
     */
    @JSONField(ordinal = 8)
    private long handleTime;

    private void baseContent(RequestTypeEnum requestType) {
        // 上报数据基础信息
        this.requestType = requestType;
        this.requestTime = System.currentTimeMillis();
        this.traceId = TraceIdUtil.getTraceId();
    }

    /**
     * web请求来了
     *
     * @param httpServletRequest
     * @return
     */
    public void requestStart(HttpServletRequest httpServletRequest) {

        // 上报基础信息填充
        baseContent(RequestTypeEnum.HTTP);

        // 接口基本信息
        InterfaceDetail webInterfaceDetail = new InterfaceDetail();
        this.interfaceDetail = webInterfaceDetail;
        webInterfaceDetail.setHttpRequestMethod(httpServletRequest.getMethod());
        webInterfaceDetail.setHttpRequestURI(httpServletRequest.getRequestURI());

        // 请求参数
        Map<String, String> requestParams = new HashMap<>();
        this.requestParams = requestParams;
        requestParams.put("httpQueryString", httpServletRequest.getQueryString());
        if (httpServletRequest instanceof RepeatedlyReadRequest) {
            requestParams.put("httpRequestBody", ((RepeatedlyReadRequest) httpServletRequest).getBody());
        }

        // 获取所有header
        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            requestHeaders.put(headerKey, httpServletRequest.getHeader(headerKey));
        }
        requestParams.put("httpRequestHeaders", JSON.toJSONString(requestHeaders));
    }

    /**
     * thrift请求来了
     *
     * @param method
     * @param args
     * @return
     */
    public void requestStart(Method method, Object[] args) {
        // 上报基础信息填充
        baseContent(RequestTypeEnum.THRIFT);

        // 接口详情
        InterfaceDetail thriftInterfaceDetail = new InterfaceDetail();
        this.interfaceDetail = thriftInterfaceDetail;
        thriftInterfaceDetail.setThriftClassName(method.getDeclaringClass().getName());
        thriftInterfaceDetail.setThriftMethodName(method.getName());

        // 无请求参数直接返回
        if (args == null || args.length <= 0) {
            return;
        }

        // 请求参数
        Map<String, Object> thriftParams = new LinkedHashMap<>(args.length);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            Parameter parameterKey = parameters[i];
            thriftParams.put(parameterKey.getType().getName() + " " + parameterKey.getName(), args[i]);
        }

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("thriftParams", JSON.toJSONString(thriftParams));
        this.requestParams = requestParams;
    }

    /**
     * 生成基础上报内容
     *
     * @param topic
     * @param message
     * @return
     */
    public void requestStart(String topic, String message, int qos) {
        // 上报基础信息填充
        baseContent(RequestTypeEnum.MQTT);

        // 接口详情
        InterfaceDetail mqttInterfaceDetail = new InterfaceDetail();
        this.interfaceDetail = mqttInterfaceDetail;
        mqttInterfaceDetail.setMqttTopic(topic);
        mqttInterfaceDetail.setMqttQos(qos);

        // 请求参数
        this.requestParams = Collections.singletonMap("mqttMessage", message);
    }


    /**
     * 设置结果与耗时
     *
     * @param responseResult
     * @param throwable
     */
    public void requestEnd(Object responseResult, Throwable throwable) {
        // 响应结果
        if (responseResult == null) {
            this.responseResult = null;
        } else if (responseResult instanceof String) {
            this.responseResult = (String) responseResult;
        } else {
            this.responseResult = JSON.toJSONString(responseResult);
        }

        // 异常信息
        if (throwable != null) {
            StringBuilder errorInfo = new StringBuilder();
            while (throwable != null) {
                errorInfo.append(throwable.toString()).append("\n");
                StackTraceElement[] stackTrace = throwable.getStackTrace();
                for (StackTraceElement stackTraceElement : stackTrace) {
                    errorInfo.append("\tat ").append(stackTraceElement.toString()).append("\n");
                }
                throwable = throwable.getCause();
            }
            this.throwableInfo = errorInfo.toString();
        }

        // 接口耗时
        this.handleTime = System.currentTimeMillis() - this.requestTime;
    }

}
