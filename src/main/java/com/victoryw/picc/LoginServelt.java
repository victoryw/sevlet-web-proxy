package com.victoryw.picc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpCookie;

public class LoginServelt extends RootServlet {
    private static Logger logger = LoggerFactory.getLogger(LoginServelt.class);
    @Override
    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String username = req.getParameter("username");
        final HttpSession session = req.getSession();
        session.setAttribute("username", username);
        logger.info(String.format("session is created, %s, %s", session.getId(), session.getAttribute("username")));
        resp.sendRedirect("/servlet/com.victoryw.picc.Hello2Servlet");
    }
}
