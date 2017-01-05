package org.openshift.evg.workshopper.workshops;

import org.openshift.evg.workshopper.modules.Modules;
import org.yaml.snakeyaml.Yaml;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Set;

@ApplicationScoped
public class WorkshopProvider {

    private final Yaml yaml = new Yaml();
    private final Workshops workshops;
    private final Modules modules;

    @Inject
    public WorkshopProvider(ServletContext context, Modules modules) {
        this.modules = modules;
        this.workshops = new Workshops();
        Set<String> paths = context.getResourcePaths("/WEB-INF/workshops");
        paths.forEach(path -> {
            String id = path.replace("/WEB-INF/workshops/", "");
            id = id.replace(".yml", "");
            this.workshops.add(id, this.yaml.loadAs(context.getResourceAsStream(path), Workshop.class));
            this.workshops.get(id).resolve(this.modules);
        });
    }

    @Produces
    public Workshops getWorkshops() {
        return this.workshops;
    }

}
