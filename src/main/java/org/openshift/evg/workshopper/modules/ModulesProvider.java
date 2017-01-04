package org.openshift.evg.workshopper.modules;

import org.yaml.snakeyaml.Yaml;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;

@ApplicationScoped
public class ModulesProvider {

    private final Yaml yaml = new Yaml();
    private final Modules modules;

    @Inject
    public ModulesProvider(ServletContext context) {
        this.modules = this.yaml.loadAs(context.getResourceAsStream("/WEB-INF/modules.yml"), Modules.class);
    }

    @Produces
    public Modules getModules() {
        return modules;
    }

}
