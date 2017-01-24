package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.ModulesProvider;
import org.openshift.evg.workshopper.workshops.WorkshopProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

@Path("/reload")
public class ReloadController {

    @Inject
    private WorkshopProvider workshops;

    @Inject
    private ModulesProvider modules;

    @Inject
    private Configuration config;

    @GET
    public Object reload() throws IOException {
        this.config.reload();
        this.modules.reload();
        this.workshops.reload();
        return true;
    }

}
