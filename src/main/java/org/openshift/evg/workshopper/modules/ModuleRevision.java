package org.openshift.evg.workshopper.modules;

import java.util.Map;

public class ModuleRevision {

    private Map<String, String> vars;

    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

}
