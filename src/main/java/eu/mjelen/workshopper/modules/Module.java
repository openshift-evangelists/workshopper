package eu.mjelen.workshopper.modules;

import java.util.List;
import java.util.Map;

public class Module {

    private String name;
    private Map<String, String> vars;
    private Map<String, ModuleRevision> revisions;
    private List<String> requires;

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
}
