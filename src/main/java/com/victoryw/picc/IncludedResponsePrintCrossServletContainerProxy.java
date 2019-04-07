package com.victoryw.picc;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.ws.ResponseWrapper;
import java.io.IOException;

/**
 * This class is used to make the compatibility of WebLogic and Jetty, When print string in the
 * {@link javax.servlet.RequestDispatcher#include(ServletRequest, ServletResponse)} servlet
 *
 * <p>This is from the issue: <br/>
 * When use the {@link javax.servlet.ServletResponseWrapper} to wrap the {@link javax.servlet.ServletResponse}
 * and  {@link javax.servlet.RequestDispatcher#include(ServletRequest, ServletResponse)} other servlet in the jsp
 * {@code  <jsp:include page="/OtherServlet" />}, the WebLogic will pass the ServletResponseWrapper
 * instead of its own internal type Response object to the included servlet,
 * there will throw exception by the WebLogic jsp render if you use the {@link ServletResponse#getWriter()} in the included servlet.
 * But because in the same case, there also will throw exception if we change the code to  {@link ServletResponse#getOutputStream()} and run in the jetty
 * So the class is help us to print string correctly by choose the method according to the servlet container.
 * </p>
 *
 * @author wang victory yan
 */
class IncludedResponsePrintCrossServletContainerProxy {
    private ResponseWriter responsePrintWriter;

    IncludedResponsePrintCrossServletContainerProxy(final ServletContext servletContext,
                                                    final ServletResponse resp) throws IOException {
        responsePrintWriter = createResponseWriter(servletContext, resp);
    }

    void println(String outPutString) throws IOException {
        responsePrintWriter.println(outPutString);
    }

    private ResponseWriter createResponseWriter(ServletContext servletContext, ServletResponse resp) throws IOException {
        if (servletContext.getServerInfo().contains("jetty")) {
            return new ResponsePrintWriter(resp);
        }

        if(servletContext.getServerInfo().contains("WebLogic")) {
            return new ResponseOutPutStream(resp);
        }

        throw new UnSupportedServletContainerException(servletContext.getServerInfo());
    }

}
