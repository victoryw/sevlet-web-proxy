package com.victoryw.picc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends RootServlet {
    @Override
    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("key", "test");
        RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/second.jsp");
        try {
            rd.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

