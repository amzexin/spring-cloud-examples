package com.lizx.common.requestlogger.http;

import com.alibaba.fastjson.JSON;
import com.lizx.common.requestlogger.RequestLoggerFactory;
import com.lizx.common.requestlogger.RequestReportContent;
import com.lizx.common.requestlogger.plugin.UserTraceIdPlugin;
import com.lizx.common.requestlogger.util.TraceIdUtil;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: Controller统一Log过滤器
 *
 * @author Lizexin
 * @date 2020-01-07 13:36
 */
public class HttpRequestFilter implements Filter {

    private static Logger requestLogger = RequestLoggerFactory.getRequestMonitorLogger();

    private static final String APPLICATION_JSON = "application/json";

    private static final String TEXT_PLAIN = "text/plain";

    private UserTraceIdPlugin userTraceIdPlugin;

    public HttpRequestFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 包装ServletRequest让body中的数据可以重复获取
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestContentType = httpServletRequest.getContentType();
        if (contentTypeIsString(requestContentType)) {
            httpServletRequest = new RepeatedlyReadRequest(httpServletRequest);
        }

        // 设置trace_id
        String userTraceId = userTraceIdPlugin == null ? null : userTraceIdPlugin.httpUserTraceId(httpServletRequest);
        TraceIdUtil.setupTraceId(userTraceId);

        // 生成基础上报内容
        RequestReportContent requestReportContent = new RequestReportContent();
        requestReportContent.requestStart(httpServletRequest);

        try {
            // 包装servletResponse让返回结果可以获取
            HasBodyResponse hasBodyResponse = new HasBodyResponse((HttpServletResponse) servletResponse);

            // 执行真正的逻辑
            filterChain.doFilter(httpServletRequest, hasBodyResponse);

            // 获取json返回结果
            byte[] bytes = hasBodyResponse.getBytes();
            String responseContentType = servletResponse.getContentType();
            String responseBody = null;
            if (contentTypeIsString(responseContentType)) {
                responseBody = new String(bytes);
            }

            // 记录执行结果与耗时
            requestReportContent.requestEnd(responseBody, null);

            // 将结果重新write
            servletResponse.getOutputStream().write(bytes);

        } catch (IOException e) {
            requestReportContent.requestEnd(null, e);
            throw e;
        } catch (ServletException e) {
            requestReportContent.requestEnd(null, e);
            throw e;
        } finally {
            requestLogger.info("{}", JSON.toJSONString(requestReportContent));
            TraceIdUtil.clearTraceId();
        }
    }

    @Override
    public void destroy() {

    }

    private boolean contentTypeIsString(String contentType) {
        return contentType != null && (contentType.contains(APPLICATION_JSON) || contentType.contains(TEXT_PLAIN));
    }

    // region 构造filter

    public HttpRequestFilter userTraceIdPlugin(UserTraceIdPlugin userTraceIdPlugin) {
        this.userTraceIdPlugin = userTraceIdPlugin;
        return this;
    }

    // endregion

}
