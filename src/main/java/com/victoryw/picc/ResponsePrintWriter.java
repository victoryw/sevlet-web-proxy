package com.victoryw.picc;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponsePrintWriter implements ResponseWriter {
    private PrintWriter writer;

    ResponsePrintWriter(ServletResponse resp) throws IOException {
        writer = resp.getWriter();
    }

    @Override
    public void println(String outPutString) {
        writer.println(outPutString);
    }
}
