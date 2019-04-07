package com.victoryw.picc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends RootServlet {
    private static Logger logger = LoggerFactory.getLogger(HelloServlet.class);

    @Override
    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println(String.format("this is %s is configuration", System.getenv("JAVA_OPTS")));
        final ServletContext servletContext = getServletContext();
        IncludedResponsePrintCrossServletContainerProxy responsePrintProxy = new IncludedResponsePrintCrossServletContainerProxy(servletContext, resp);
        responsePrintProxy.println(resp.getClass().toString());
    }
}


