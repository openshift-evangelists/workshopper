package org.openshift.evg.workshopper.modules;

import java.util.HashMap;
import java.util.Map;

public class ModuleRevision {

    private Map<String, String> vars = new HashMap<>();

    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    @Override
    public String toString() {
        return "ModuleRevision{" +
                "vars=" + vars +
                '}';
    }
}
