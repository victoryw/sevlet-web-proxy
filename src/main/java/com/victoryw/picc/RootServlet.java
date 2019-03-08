package com.victoryw.picc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RootServlet extends HttpServlet {
    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doProcess(req, resp);
    }

    protected abstract void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
