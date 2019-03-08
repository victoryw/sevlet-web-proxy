package com.victoryw.picc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultServelt extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = (String)req.getAttribute("javax.servlet.include.path_info");
        if (uri == null) {
            uri = req.getPathInfo();
        }

        String actionName = null;
        int index = -1;
        if ((index = uri.lastIndexOf("/")) != -1) {
            actionName = uri.substring(index + 1);
        }

        try {
            HttpServlet servlet = (HttpServlet) Class.forName(actionName).newInstance();
            servlet.init(this.getServletConfig());
            servlet.service(req,resp);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        super.service(req, resp);
    }
}
