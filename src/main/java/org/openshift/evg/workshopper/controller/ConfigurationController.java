package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.config.Configuration;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/config")
@Produces({ MediaType.APPLICATION_JSON })
public class ConfigurationController {

    @Inject
    private Configuration configuration;

    @GET
    public Map<String, Object> getConfiguration() {
        return this.configuration.export();
    }

}
