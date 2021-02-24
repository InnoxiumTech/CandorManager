package uk.co.innoxium.candor.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;


public class LoggerProxyStream extends OutputStream {

    private final Logger logger;
    private final Level logLevel;
    private final OutputStream outputStream;
    private final StringBuilder sbBuffer;

    public LoggerProxyStream(Logger logger, Level logLevel, OutputStream outputStream) {

        this.logger = logger;
        this.logLevel = logLevel;
        this.outputStream = outputStream;
        sbBuffer = new StringBuilder();
    }

    @Override
    public void write(int b) throws IOException {

        doWrite(String.valueOf((char) b));
    }

    @Override
    public void write(byte[] b) throws IOException {

        doWrite(new String(b));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {

        doWrite(new String(b, off, len));
    }

    private void doWrite(String str) throws IOException {

        sbBuffer.append(str);
        if(sbBuffer.charAt(sbBuffer.length() - 1) == '\n') {

            // The output is ready
            sbBuffer.setLength(sbBuffer.length() - 1); // remove '\n'
            if(sbBuffer.charAt(sbBuffer.length() - 1) == '\r') {

                sbBuffer.setLength(sbBuffer.length() - 1); // remove '\r'
            }
            String buf = sbBuffer.toString();
            sbBuffer.setLength(0);
            outputStream.write(buf.getBytes());
            outputStream.write('\n');
            logger.log(logLevel, buf);
        }
    }
}