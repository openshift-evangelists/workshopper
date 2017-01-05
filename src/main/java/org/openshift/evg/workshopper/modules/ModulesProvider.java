package org.openshift.evg.workshopper.modules;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;

@ApplicationScoped
public class ModulesProvider {

    private final Yaml yaml;

    {
        Constructor constructor = new Constructor(Modules.class);
        TypeDescription description;

        description = new TypeDescription(Modules.class);
        description.putMapPropertyType("modules", String.class, Module.class);
        constructor.addTypeDescription(description);

        description = new TypeDescription(Module.class);
        description.putMapPropertyType("revisions", String.class, ModuleRevision.class);
        constructor.addTypeDescription(description);

        yaml = new Yaml(constructor);
    }

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
