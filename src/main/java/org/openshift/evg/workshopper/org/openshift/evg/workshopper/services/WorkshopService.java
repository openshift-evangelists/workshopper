package org.openshift.evg.workshopper.org.openshift.evg.workshopper.services;

import org.openshift.evg.workshopper.workshops.Workshop;
import org.openshift.evg.workshopper.workshops.Workshops;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;

@RestController
@RequestMapping("/workshops")
public class WorkshopService {
    @Autowired
    ServletContext context;


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Workshops getAllWorkshops(){

        Workshops workshops = Workshops.get(context);
        return (workshops);
    }
}
