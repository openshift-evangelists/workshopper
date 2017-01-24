package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.Module;
import org.openshift.evg.workshopper.modules.Modules;
import org.openshift.evg.workshopper.workshops.Workshop;
import org.openshift.evg.workshopper.workshops.Workshops;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/workshops")
@Produces({ MediaType.APPLICATION_JSON })
public class WorkshopController {

    @Inject
    private Workshops workshops;

    @Inject
    private Modules modules;

    @Inject
    private Configuration config;

    @GET
    public Map<String, Workshop> getAllWorkshops() {
        return this.workshops.get();
    }

    @GET
    @Path("{workshop}")
    public Workshop getWorkshops(@PathParam("workshop") String w) {
        return workshops.get(w);
    }

    @GET
    @Path("{workshop}/env/{module}")
    public HashMap<String, Object> workshopEnv(@PathParam("workshop") String w, @PathParam("module") String m) {
        return generateEnv(w, m, null);
    }

    @GET
    @Path("{workshop}/env/{module}/{revision}")
    public HashMap<String, Object> workshopEnv(@PathParam("workshop") String w, @PathParam("module") String m, @PathParam("revision") String r) {
        return generateEnv(w, m, r);
    }

    private HashMap<String, Object> generateEnv(String w, String m, String revision) {
        Workshop workshop = this.workshops.get(w);
        Module module = this.modules.get(m);
        if (revision == null) {
            if(workshop.getModules() != null && workshop.getModules().getRevisions() != null) {
                revision = workshop.getModules().getRevisions().get(m);
            }
            if(workshop.getRevision() != null) {
                revision = workshop.getRevision();
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("revision", revision);
        result.put("workshop", workshop);
        result.put("module", module);
        HashMap<String, Object> env = new HashMap<>();
        result.put("env", env);
        // system defaults has lowest priority
        env.putAll(this.config.getConfig().getVars());
        // Module defaults
        env.putAll(module.getVars());
        // Revision overrides module defaults
        if(revision != null && module.getRevisions() != null && module.getRevisions().get(revision) != null) {
            env.putAll(module.getRevisions().get(revision).getVars());
        }
        // Workshop defaults override revisions
        env.putAll(workshop.getVars());
        // Environment variables may override all
        env.keySet().forEach(key -> {
            if(System.getenv().containsKey(key)) {
                env.put(key, System.getenv(key));
            }
        });
        return result;
    }

}
