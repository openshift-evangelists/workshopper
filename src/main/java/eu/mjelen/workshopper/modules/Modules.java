package eu.mjelen.workshopper.modules;

import org.yaml.snakeyaml.Yaml;

import javax.servlet.ServletContext;
import java.util.Map;

public class Modules {

    private static Yaml YAML = new Yaml();
    private static Modules MODULES;

    public static Modules get(ServletContext servletContext) {
        if(MODULES == null) {
            MODULES = YAML.loadAs(servletContext.getResourceAsStream("/WEB-INF/modules.yml"), Modules.class);
        }
        return MODULES;
    }

    private Map<String, Module> modules;

    public Map<String, Module> getModules() {
        return modules;
    }

    public void setModules(Map<String, Module> modules) {
        this.modules = modules;
    }
}
