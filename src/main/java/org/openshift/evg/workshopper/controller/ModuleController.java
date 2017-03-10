package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.GenericProvider;
import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.Module;
import org.openshift.evg.workshopper.modules.Modules;
import org.openshift.evg.workshopper.modules.ModulesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Path("/modules")
@Produces({ MediaType.APPLICATION_JSON })
public class ModuleController extends GenericProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleController.class);

    @Inject
    private ModulesProvider modules;

    @Inject
    private Configuration config;

    @GET
    public Map<String, Module> getAllModules() {
        return this.modules.getModules().get();
    }

    @GET
    @Path("/content/{module}")
    @Produces({ "text/asciidoc" })
    public InputStream content(@PathParam("module") String m) throws IOException {
        String path = "/" + m.replace("_", "/") + ".adoc";
        String url = this.config.getContentUrl() + path;
        LOG.info("Loading module content from {}", url);
        return getStream(url);
    }

}
