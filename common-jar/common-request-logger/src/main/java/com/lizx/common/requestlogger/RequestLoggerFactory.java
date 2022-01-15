package com.lizx.common.requestlogger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.lizx.common.requestlogger.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2021-05-18 16:37
 */
public class RequestLoggerFactory {

    private static final String ROLLING_FILE_LOGGER_NAME = "requestMonitor";

    private static final String LOGBACK_XML_FILENAME = "logback-spring.xml";

    private static final int MAX_HISTORY = 1;

    private static Logger requestMonitorLogger = null;

    private static ConsoleAppender<ILoggingEvent> consoleAppender() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // region <appender>
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setName("CONSOLE");

        // endregion

        // region <encoder>

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%msg%n");
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.setContext(loggerContext);
        encoder.start();
        consoleAppender.setEncoder(encoder);

        // endregion

        consoleAppender.start();

        return consoleAppender;
    }

    private static RollingFileAppender<ILoggingEvent> rollingFileAppender(String loggerName, String rootLogPath) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        String logPath = System.getProperty("user.dir") + "/" + rootLogPath;

        // 配置RollingFileAppender: 用于滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);

        // region base info

        // appender name
        rollingFileAppender.setName(loggerName + "Appender");

        // 被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
        rollingFileAppender.setFile(logPath + "/" + loggerName + ".log");

        // 如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
        rollingFileAppender.setAppend(true);

        // endregion

        // region <rollingPolicy>

        // 基于大小和时间的FNATP
        SizeAndTimeBasedFNATP<ILoggingEvent> sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP<>();
        sizeAndTimeBasedFNATP.setMaxFileSize(new FileSize(100 * FileSize.MB_COEFFICIENT));

        // TimeBasedRollingPolicy是最常用的滚动策略，它根据时间来制定滚动策略，既负责滚动也负责触发滚动。
        TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy = new TimeBasedRollingPolicy<>();
        timeBasedRollingPolicy.setContext(loggerContext);
        // 基于时间的文件命名和触发策略
        timeBasedRollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
        // 历史文件保存格式
        timeBasedRollingPolicy.setFileNamePattern(logPath + "/" + loggerName + "-%d{yyyy-MM-dd}_%i.log.zip");
        // 历史文件保存2天
        timeBasedRollingPolicy.setMaxHistory(MAX_HISTORY);
        // 设置appender
        timeBasedRollingPolicy.setParent(rollingFileAppender);
        // 启动timeBasedRollingPolicy
        timeBasedRollingPolicy.start();

        // 当发生滚动时，决定RollingFileAppender的行为，涉及文件移动和重命名。
        rollingFileAppender.setRollingPolicy(timeBasedRollingPolicy);

        // endregion

        // region <encoder>

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%msg%n");
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.setContext(loggerContext);
        encoder.start();
        rollingFileAppender.setEncoder(encoder);

        // endregion

        // 启动策略
        rollingFileAppender.start();

        return rollingFileAppender;
    }

    private static String rootLogPath() {
        String logPath = XMLUtil.rootLogPath(LOGBACK_XML_FILENAME);
        if (logPath == null) {
            return "logPath_IS_UNDEFINED";
        }
        return logPath;
    }

    static {
        String rootLogPath = rootLogPath();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger requestMonitorLogger = loggerContext.getLogger(ROLLING_FILE_LOGGER_NAME);
        requestMonitorLogger.setLevel(Level.INFO);
//        requestMonitorLogger.addAppender(consoleAppender());
        requestMonitorLogger.addAppender(rollingFileAppender(ROLLING_FILE_LOGGER_NAME, rootLogPath));
        requestMonitorLogger.setAdditive(false);

        RequestLoggerFactory.requestMonitorLogger = requestMonitorLogger;
    }

    public static Logger getRequestMonitorLogger() {
        return requestMonitorLogger;
    }

    private RequestLoggerFactory() {
    }
}
