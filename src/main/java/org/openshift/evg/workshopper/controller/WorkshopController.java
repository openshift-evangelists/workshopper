package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.Module;
import org.openshift.evg.workshopper.modules.Modules;
import org.openshift.evg.workshopper.modules.ModulesProvider;
import org.openshift.evg.workshopper.modules.content.ModuleContentProvider;
import org.openshift.evg.workshopper.workshops.Workshop;
import org.openshift.evg.workshopper.workshops.WorkshopProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static sun.security.krb5.SCDynamicStoreConfig.getConfig;

@Path("/workshops")
@Produces({ MediaType.APPLICATION_JSON })
public class WorkshopController {

    @Inject
    private WorkshopProvider workshops;

    @Inject
    private ModulesProvider modules;

    @Inject
    private ModuleContentProvider moduleContent;

    @Inject
    private Configuration config;

    @GET
    public Map<String, Workshop> getAllWorkshops() {
        return this.workshops.getWorkshops().get();
    }

    @GET
    @Path("{workshop}")
    public Workshop getWorkshops(@PathParam("workshop") String w) {
        return workshops.getWorkshops().get(w);
    }

    @GET
    @Path("{workshop}/modules")
    public Map<String, Module> getWorkshopModules(@PathParam("workshop") String w) {
        Workshop workshop = workshops.getWorkshops().get(w);
        return this.modules.getModules().get(workshop.getContent().getUrl()).get();
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

    @GET
    @Path("{workshop}/content/module/{module}")
    @Produces({ "text/asciidoc" })
    public byte[] content(@PathParam("workshop") String w, @PathParam("module") String module) throws IOException {
        Workshop workshop = workshops.getWorkshops().get(w);
        return moduleContent.loadModule(workshop, module);
    }

    @GET
    @Path("{workshop}/content/assets/{path : .*}")
    public byte[] asset(@PathParam("workshop") String w, @PathParam("path") String path) throws IOException {
        Workshop workshop = workshops.getWorkshops().get(w);
        return moduleContent.loadAsset(workshop, path);
    }

    private HashMap<String, Object> generateEnv(String w, String m, String revision) {
        Workshop workshop = this.workshops.getWorkshops().get(w);
        Modules mod = this.modules.getModules().get(workshop.getContent().getUrl());
        Module module = this.modules.getModules().get(workshop.getContent().getUrl()).get(m);
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
        if(mod.getConfig().getVars() != null) {
            mod.getConfig().getVars().forEach(var -> {
                env.put(var.getName(), var.getValue());
            });
        }
        // Module defaults
        if(module.getVars() != null) {
            module.getVars().forEach((name, value) -> {
                if(value != null) {
                    env.put(name, value);
                }
            });
        }
        // Revision overrides module defaults
        if(revision != null && module.getRevisions() != null && module.getRevisions().get(revision) != null) {
            env.putAll(module.getRevisions().get(revision).getVars());
        }
        // Workshop defaults override revisions
        if(workshop.getVars() != null) {
            env.putAll(workshop.getVars());
        }
        // Environment variables may override all
        env.keySet().forEach(key -> {
            if(System.getenv().containsKey(key)) {
                env.put(key, System.getenv(key));
            }
        });
        // Expose list of modules in templates
        HashMap<String, Boolean> modules = new HashMap<>();
        workshop.getModules().getActivate().forEach(mname -> {
            modules.put(mname, true);
        });
        env.put("modules", modules);
        return result;
    }

}
