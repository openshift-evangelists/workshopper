package org.openshift.evg.workshopper.modules;

import org.openshift.evg.workshopper.GenericProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ModulesProvider extends GenericProvider {
	private static final Logger LOG = LoggerFactory.getLogger(ModulesProvider.class);

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

    private Map<String, Modules> modules = new HashMap<>();

    public void reload() throws IOException {
        this.modules.keySet().forEach(key -> {
            try {
                load(key);
            } catch (IOException e) {
                LOG.error("Problem loading modules from {}", key);
            }
        });
    }

    public void load(String host) throws IOException {
        String url = host + "/_modules.yml";
        LOG.info("Loading modules list from {}", url);
        this.modules.put(host, this.yaml.loadAs(getStream(url), Modules.class));
    }

    public Map<String, Modules> getModules() {
        return modules;
    }

}
