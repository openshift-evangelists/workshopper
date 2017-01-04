package org.openshift.evg.workshopper.modules;

import javax.enterprise.inject.Vetoed;
import java.util.Map;

@Vetoed
public class Modules {

    private Map<String, Module> modules;

    public Map<String, Module> getModules() {
        return modules;
    }

    public void setModules(Map<String, Module> modules) {
        this.modules = modules;
    }

}
