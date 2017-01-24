package org.openshift.evg.workshopper;

import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.workshops.Workshops;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class MainServlet extends HttpServlet {

    @Inject
    private Workshops workshops;

    @Inject
    private Configuration config;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
