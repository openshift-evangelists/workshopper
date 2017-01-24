package org.openshift.evg.workshopper.workshops;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.Modules;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class WorkshopProvider {

    @Inject
    private Configuration config;

    @Inject
    private Modules modules;

    @Inject
    private OkHttpClient client;

    private final Yaml yaml = new Yaml();
    private Workshops workshops;

    @PostConstruct
    public void initialize() {
        try {
            reload();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Problem loading workshops", e);
        }
    }

    public void reload() throws IOException {
        this.workshops = new Workshops();

        if(this.config.getWorkshopsUrl() != null) {
            Request request = new Request.Builder().url(this.config.getWorkshopsUrl()).build();
            Response response = this.client.newCall(request).execute();
            List workshops = this.yaml.loadAs(response.body().byteStream(), List.class);
            workshops.forEach(url -> {
                try {
                    this.loadFrom((String) url);
                } catch (IOException e) {
                    LoggerFactory.getLogger(getClass()).error("Problem loading workshop", e);
                }
            });
        } else {
            loadFrom(this.config.getWorkshopUrl());
        }
    }

    private Workshop loadFrom(String url) throws IOException {
        LoggerFactory.getLogger(getClass()).info("Loading default workshop from {}", this.config.getWorkshopUrl());
        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        Workshop workshop = this.yaml.loadAs(response.body().byteStream(), Workshop.class);
        workshop.resolve(this.modules);
        this.workshops.add("default", workshop);
        this.config.setDefaultWorkshop("default");
        return workshop;
    }

    @Produces
    public Workshops getWorkshops() {
        return this.workshops;
    }

}
