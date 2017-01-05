package org.openshift.evg.workshopper.modules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Module {

    private String name;
    private Map<String, String> vars = new HashMap<>();
    private Map<String, ModuleRevision> revisions = new HashMap<>();
    private List<String> requires = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    public Map<String, ModuleRevision> getRevisions() {
        return revisions;
    }

    public void setRevisions(Map<String, ModuleRevision> revisions) {
        this.revisions = revisions;
    }

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", vars=" + vars +
                ", revisions=" + revisions +
                ", requires=" + requires +
                '}';
    }
}
