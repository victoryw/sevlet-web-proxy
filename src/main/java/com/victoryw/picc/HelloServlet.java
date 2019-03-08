package com.victoryw.picc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HelloServlet extends RootServlet {
    private static Logger logger = LoggerFactory.getLogger(HelloServlet.class);

    @Override
    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("key", "test");
        final HttpSession session = req.getSession(false);
        if(session != null) {
            logger.info(String.format("session is true, %s", session.getId()));
            req.setAttribute("username", session.getAttribute("username"));
        }

        RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/hello.jsp");
        try {
            rd.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

