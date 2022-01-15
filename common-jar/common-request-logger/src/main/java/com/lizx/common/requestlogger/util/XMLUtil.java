package com.lizx.common.requestlogger.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2021-06-07 16:31
 */
public class XMLUtil {

    private static Logger rootLogger = LoggerFactory.getLogger(XMLUtil.class);

    private static InputStream getResourceAsStream(String xmlName) throws FileNotFoundException {
        String localXmlPath = "/" + xmlName;
        String onlineXmlPath = "/BOOT-INF/classes/" + xmlName;

        List<String> xmlPathList = Arrays.asList(localXmlPath, onlineXmlPath);
        for (String xmlPath : xmlPathList) {
            InputStream xmlAsStream = XMLUtil.class.getResourceAsStream(xmlPath);
            if (xmlAsStream != null) {
                return xmlAsStream;
            }
        }
        throw new FileNotFoundException(xmlName);
    }

    public static Document loadXML(String xmlName) throws FileNotFoundException, DocumentException {
        // 1.创建Reader对象
        SAXReader reader = new SAXReader();
        // 2.获取配置文件流
        InputStream xmlAsStream = getResourceAsStream(xmlName);
        // 3.加载xml
        return reader.read(xmlAsStream);
    }

    /**
     * 获取日志根目录
     * configuration -> property(logPath)
     *
     * @param xmlName
     * @return
     */
    public static String rootLogPath(String xmlName) {
        try {
            // 加载xml
            Document document = loadXML(xmlName);
            // 根节点
            Element rootElement = document.getRootElement();
            Iterator elementIterator = rootElement.elementIterator();
            while (elementIterator.hasNext()) {
                Element element = (Element) elementIterator.next();
                // <property>节点
                String elementName = element.getName();
                if (!"property".equalsIgnoreCase(elementName)) {
                    continue;
                }
                // name = "logPath"
                String logPathKey = element.attributeValue("name");
                if (!"logPath".equalsIgnoreCase(logPathKey)) {
                    continue;
                }
                String logPathValue = element.attributeValue("value");
                if (logPathValue == null || logPathValue.isEmpty()) {
                    continue;
                }

                return logPathValue;
            }
        } catch (Exception e) {
            rootLogger.warn("获取logPath出现异常", e);
        }
        return null;
    }

    private XMLUtil() {
    }
}
