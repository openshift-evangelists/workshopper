package org.openshift.evg.workshopper.modules;

import org.openshift.evg.workshopper.GenericProvider;
import org.openshift.evg.workshopper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class ModulesProvider extends GenericProvider {
	private static final Logger LOG = LoggerFactory.getLogger(ModulesProvider.class);

    @Inject
    private Configuration config;

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

    private Modules modules;

    @PostConstruct
    public void initialize() {
        try {
            reload();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Problem loading modules", e);
        }
    }

    public void reload() throws IOException {
        String url = this.config.getContentUrl() + "/_modules.yml";
        LOG.info("Loading modules list from {}", url);
        this.modules = this.yaml.loadAs(getStream(url), Modules.class);
    }

    public Modules getModules() {
        return modules;
    }

}
