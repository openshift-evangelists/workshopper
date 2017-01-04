package org.openshift.evg.workshopper.controller;

import org.openshift.evg.workshopper.workshops.Workshop;
import org.openshift.evg.workshopper.workshops.Workshops;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/workshops")
@Produces({ MediaType.APPLICATION_JSON })
public class WorkshopController {

    @Inject
    private Workshops workshops;

    @GET
    public Map<String, Workshop> getAllWorkshops() {
        return this.workshops.get();
    }

}
