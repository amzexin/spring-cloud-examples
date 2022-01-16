package com.lizx.common.requestlogger.test;

import com.lizx.common.requestlogger.RequestLoggerFactory;
import org.junit.Test;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestLoggerFactoryTest {

    @Test
    public void test1() {
        Logger requestMonitorLogger = RequestLoggerFactory.getRequestMonitorLogger();
        requestMonitorLogger.info("today is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
