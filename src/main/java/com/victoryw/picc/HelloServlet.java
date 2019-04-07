package com.victoryw.picc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class HelloServlet extends RootServlet {
    private static Logger logger = LoggerFactory.getLogger(HelloServlet.class);

    @Override
    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println(String.format("this is %s is configuration", System.getenv("JAVA_OPTS")));
        final ServletOutputStream writer = resp.getOutputStream();
        writer.println(resp.getClass().toString());
    }
}

