package com.lizx.common.requestlogger.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2021-05-20 10:04
 */
public class HasBodyResponse extends HttpServletResponseWrapper {

    private static Logger rootLogger = LoggerFactory.getLogger(HasBodyResponse.class);

    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    private PrintWriter printWriter;

    public HasBodyResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        // 将数据写到 bytes 中
        return new HasBodyOutputStream(bytes);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        printWriter = new PrintWriter(new OutputStreamWriter(bytes, StandardCharsets.UTF_8));
        return printWriter;
    }

    /**
     * 获取缓存在 PrintWriter 中的响应数据
     *
     * @return
     */
    public byte[] getBytes() {
        if (null != printWriter) {
            printWriter.close();
            return bytes.toByteArray();
        }

        try {
            bytes.flush();
        } catch (IOException e) {
            rootLogger.error(e.getMessage(), e);
        }

        return bytes.toByteArray();
    }

    private static class HasBodyOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream byteArrayOutputStream;

        public HasBodyOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputStream = byteArrayOutputStream;
        }

        @Override
        public void write(int b) throws IOException {
            // 将数据写到 stream　中
            byteArrayOutputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}
