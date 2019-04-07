package com.victoryw.picc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Hello2Servlet extends RootServlet {
    private static Logger logger = LoggerFactory.getLogger(Hello2Servlet.class);

    @Override
    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("key", "test");
        final HttpSession session = req.getSession();
        if(session != null) {
            logger.info(String.format("session is true, %s", session.getId()));
            logger.info(String.format("session username is true, %s", session.getAttribute("username")));
            req.setAttribute("username", session.getAttribute("username"));
        }

        OutputStreamWriter writer;
        if (req.getCookies() != null) {
            Arrays.stream(req.getCookies()).forEach(cookie -> {
                logger.info(String.format("session is null, cookie is value, %s", cookie.getValue()));
                logger.info(String.format("session is null, cookie is domain, %s", cookie.getDomain()));
                logger.info(String.format("session is null, cookie is path, %s", cookie.getPath()));
                logger.info(String.format("session is null, cookie is name, %s", cookie.getName()));
            });
        } else {
            logger.info("HelloServlet2 there is no session");
        }

        String path = "/hello.jsp";
        RequestDispatcher rd = this.getServletContext().getRequestDispatcher(path);

        try {
            rd.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

