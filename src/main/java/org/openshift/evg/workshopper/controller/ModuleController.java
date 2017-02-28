package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.modules.Module;
import org.openshift.evg.workshopper.modules.Modules;
import org.openshift.evg.workshopper.modules.ModulesProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/modules")
@Produces({ MediaType.APPLICATION_JSON })
public class ModuleController {

    @Inject
    private ModulesProvider modules;

    @GET
    public Map<String, Module> getAllWorkshops() {
        return this.modules.getModules().get();
    }

}
