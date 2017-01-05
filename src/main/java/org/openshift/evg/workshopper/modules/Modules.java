package org.openshift.evg.workshopper.modules;

import javax.enterprise.inject.Vetoed;
import java.util.HashMap;
import java.util.Map;

@Vetoed
public class Modules {

    private Map<String, Module> modules = new HashMap<>();

    public void setModules(Map<String, Module> modules) {
        this.modules = modules;
    }

    public Map<String, Module> get() {
        return modules;
    }

    public Module get(String id) {
        return this.modules.get(id);
    }

}
