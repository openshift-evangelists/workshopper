package org.openshift.evg.workshopper.modules;

import java.util.HashMap;
import java.util.Map;

public class Modules {

    private Map<String, Module> modules = new HashMap<>();
    private ModuleConfiguration config = new ModuleConfiguration();

    public void setModules(Map<String, Module> modules) {
        this.modules = modules;
    }

    public Map<String, Module> get() {
        return modules;
    }

    public Module get(String id) {
        return this.modules.get(id);
    }

    public ModuleConfiguration getConfig() {
        return config;
    }

    public void setConfig(ModuleConfiguration config) {
        this.config = config;
    }

}
