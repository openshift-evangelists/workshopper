package org.openshift.evg.workshopper.config;

import java.util.Map;

public class ModuleConfiguration {

    private Map<String, Object> vars;

    public Map<String, Object> getVars() {
        return vars;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }
}
