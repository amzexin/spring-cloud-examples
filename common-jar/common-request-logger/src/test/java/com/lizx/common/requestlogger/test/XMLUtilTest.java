package com.lizx.common.requestlogger.test;

import com.lizx.common.requestlogger.util.XMLUtil;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2021-06-07 16:41
 */
public class XMLUtilTest {

    @Test
    public void rootLogPathTest() throws DocumentException, FileNotFoundException {
        String xmlName = "logback222.xml";
//        xmlName = "pom.xml";
        String logPath = XMLUtil.rootLogPath(xmlName);
        System.out.println(logPath);
    }
}
