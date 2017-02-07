package org.openshift.evg.workshopper.workshops;

import org.openshift.evg.workshopper.modules.Module;
import org.openshift.evg.workshopper.modules.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Workshop {
	private static final Logger LOG = LoggerFactory.getLogger(Workshop.class);

    private String id;
    private String name;
    private String logo;
    private Boolean priv = false;
    private Map<String, String> vars;
    private String revision;
    private WorkshopModules modules;
    private List<String> sortedModules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<String> getSortedModules() {
        return sortedModules;
    }

    public void resolve(Modules modules) {
        // In case the workshop does not specify modules, add all modules
        if(getModules() == null || getModules().getActivate() == null || getModules().getActivate().size() == 0) {
            if(this.modules == null) this.modules = new WorkshopModules();
            this.modules.getActivate().addAll(modules.get().keySet());
        }
        // In case the workshop does not explicitly name a module that is requirement for activated modules, add it
        List<String> activate = new LinkedList<>(this.modules.getActivate());
        getModules().getActivate().forEach(id -> {
            LOG.info("Module '{}' depends on '{}'", getName(), id);
            Module module = modules.get(id);
            if (module == null) {
            	LOG.error("Module {} does not exist", id);
            	throw new RuntimeException("No module found with name " + id);
            }
            if(module.getRequires() == null) return;
            module.getRequires().forEach(req -> {
               if(!activate.contains(req)) {
                   LOG.debug("Adding required module {} to the module list since not included", req);
                   activate.add(req);
               }
            });
        });
        this.modules.setActivate(activate);
        // Sort the modules based on their requirements
        boolean repeat = true;
        List<String> target = new LinkedList<>(this.modules.getActivate());
        while(repeat) {
            repeat = false;
            for(String id : target) {
                Module module = modules.get(id);
                if(module.getRequires() != null) for(String req : module.getRequires()) {
                    int reqpos = target.indexOf(req);
                    int modpos = target.indexOf(id);
                    if(reqpos > modpos) {
                        target.set(reqpos, id);
                        target.set(modpos, req);
                        repeat = true;
                    }
                }
            }
        }
        this.sortedModules = target;
    }

    @Override
    public String toString() {
        return "Workshop{" +
                "name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", priv=" + priv +
                ", vars=" + vars +
                ", revision='" + revision + '\'' +
                ", modules=" + modules +
                '}';
    }
}
