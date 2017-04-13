package org.openshift.evg.workshopper.modules;

import org.openshift.evg.workshopper.modules.content.ModuleVariable;

import java.util.List;

public class ModuleConfiguration {

    private List<ModuleVariable> vars;

    public List<ModuleVariable> getVars() {
        return vars;
    }

    public void setVars(List<ModuleVariable> vars) {
        this.vars = vars;
    }
}
