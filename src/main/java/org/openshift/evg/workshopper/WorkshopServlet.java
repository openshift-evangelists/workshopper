package org.openshift.evg.workshopper;

import org.openshift.evg.workshopper.modules.Modules;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/workshop/*")
public class WorkshopServlet extends HttpServlet {

    private Pattern workshopPattern = Pattern.compile("^/([^/]+)/?$");
    private Pattern modulePattern = Pattern.compile("^/([^/]+)/module/([^/]+)/?$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        Matcher moduleMatcher = modulePattern.matcher(path);
        if(moduleMatcher.find()) {
            String workshop = moduleMatcher.group(1);
            String module = moduleMatcher.group(2);
            getServletContext().setAttribute("modules", Modules.get(getServletContext()));
            getServletContext().setAttribute("workshop", workshop);
            getServletContext().setAttribute("module", module);

//            Asciidoctor asciidoctor = Asciidoctor.Factory.create();
//            HashMap<String, Object> context = new HashMap<>();
//
//            String content = asciidoctor.convert(readModule(module), context);
//            getServletContext().setAttribute("content", content);
            doModule(workshop, module, req, resp);
            return;
        }

        Matcher workshopMatcher = workshopPattern.matcher(path);
        if(workshopMatcher.find()) {
            String workshop = workshopMatcher.group(1);
            getServletContext().setAttribute("workshop", workshop);
            getServletContext().setAttribute("modules", Modules.get(getServletContext()));
            doWorkshop(workshop, req, resp);
            return;
        }

    }

    private String readModule(String module) throws IOException {
        InputStream stream = getServletContext().getResourceAsStream("/WEB-INF/modules/" + module + ".adoc");
        StringBuilder buffer = new StringBuilder();
        int i;
        byte[] tmp = new byte[1024];
        while((i = stream.read(tmp)) > -1) {
            buffer.append(new String(tmp, 0, i));
        }
        return buffer.toString();
    }

    private void doWorkshop(String workshop, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/workshop.jsp").forward(req, resp);
    }

    private void doModule(String workshop, String module, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/module.jsp").forward(req, resp);
    }
}
