package com.victoryw.picc;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import java.io.IOException;

class IncludedResponsePrintCrossServletContainerProxy {
    private ResponseWriter responsePrintWriter;

    IncludedResponsePrintCrossServletContainerProxy(final ServletContext servletContext,
                                                    final ServletResponse resp) throws IOException {
        responsePrintWriter = getResponseWriter(servletContext, resp);
    }

    void println(String outPutString) throws IOException {
        responsePrintWriter.println(outPutString);
    }

    private ResponseWriter getResponseWriter(ServletContext servletContext, ServletResponse resp) throws IOException {
        if (servletContext.getServerInfo().contains("jetty")) {
            return new ResponsePrintWriter(resp);
        }

        if(servletContext.getServerInfo().contains("WebLogic")) {
            return new ResponseOutPutStream(resp);
        }

        throw new UnSupportedServletContainerException(servletContext.getServerInfo());
    }

}
