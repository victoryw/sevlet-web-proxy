package com.victoryw.picc;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class ResponseOutPutStream implements ResponseWriter {
    private ServletOutputStream outputStream;

    public ResponseOutPutStream(ServletResponse resp) throws IOException {
        outputStream  = resp. getOutputStream();
    }

    @Override
    public void println(String outPutString) throws IOException {
        outputStream.println(outPutString);
    }
}
