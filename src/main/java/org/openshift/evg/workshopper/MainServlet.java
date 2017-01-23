package org.openshift.evg.workshopper;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String defaultLab = System.getenv("DEFAULT_LAB");
        if(defaultLab != null) {
            getServletContext().getRequestDispatcher("#/workshop/" + defaultLab + "/").forward(req, resp);
        } else {
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
