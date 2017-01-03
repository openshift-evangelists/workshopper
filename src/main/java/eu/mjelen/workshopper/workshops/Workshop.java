package eu.mjelen.workshopper.workshops;

import java.util.Map;

public class Workshop {

    private String name;
    private String logo;
    private Boolean priv;
    private Map<String, String> vars;
    private String revision;
    private WorkshopModules modules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getPrivate() {
        return priv;
    }

    public void setPrivate(Boolean priv) {
        this.priv = priv;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public WorkshopModules getModules() {
        return modules;
    }

    public void setModules(WorkshopModules modules) {
        this.modules = modules;
    }

}
