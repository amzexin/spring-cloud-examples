package com.lizx.common.requestlogger.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2021-06-07 16:31
 */
public class XMLUtil {

    private static final Set<String> LOG_DIRECTORY_LIST = new HashSet<>(Arrays.asList("LOG_HOME", "logPath"));

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
    public static String rootLogPath(String xmlName) throws DocumentException, FileNotFoundException {
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

            // name = "${LOG_DIRECTORY_VARIABLE}"
            String logPathKey = element.attributeValue("name");
            if (!LOG_DIRECTORY_LIST.contains(logPathKey)) {
                continue;
            }

            // 日志文件对应的目录
            String logPathValue = element.attributeValue("value");
            if (logPathValue == null || logPathValue.isEmpty()) {
                continue;
            }

            return logPathValue;
        }
        return null;
    }

    private XMLUtil() {
    }
}
