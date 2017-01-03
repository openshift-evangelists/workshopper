package eu.mjelen.workshopper;

import eu.mjelen.workshopper.workshops.Workshops;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String defaultLab = System.getenv("DEFAULT_LAB");
        if(defaultLab != null) {
            getServletContext().getRequestDispatcher("/workshop/" + defaultLab + "/").forward(req, resp);
        } else {
            Workshops workshops = Workshops.get(getServletContext());
            getServletContext().setAttribute("workshops", workshops);
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
