package org.openshift.evg.workshopper.workshops;

import org.yaml.snakeyaml.Yaml;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Workshops {

    private static Yaml YAML = new Yaml();
    private static Workshops WORKSHOPS;

    public static Workshops get(ServletContext servletContext) {
        if(WORKSHOPS == null) {
            WORKSHOPS = new Workshops();
            Set<String> paths = servletContext.getResourcePaths("/WEB-INF/workshops");
            paths.forEach(path -> {
                String id = path.replace("/WEB-INF/workshops/", "");
                id = id.replace(".yml", "");
                WORKSHOPS.add(id, YAML.loadAs(servletContext.getResourceAsStream(path), Workshop.class));
            });
            System.out.println(WORKSHOPS);
        }
        return WORKSHOPS;
    }

    private Map<String, Workshop> workshops = new HashMap<>();

    public void add(String id, Workshop workshop) {
        this.workshops.put(id, workshop);
    }

    public Workshop get(String id) {
        return this.workshops.get(id);
    }

    public Map<String, Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(Map<String, Workshop> workshops) {
        this.workshops = workshops;
    }

}
