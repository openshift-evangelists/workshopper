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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    private MessageDigest digest;

    @PostConstruct
    public void initialize() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e) {
            LoggerFactory.getLogger(getClass()).error("Problem finding digest", e);
        }
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
            load(workshops);
        } else if(this.config.getWorkshopsUrls() != null) {
            List workshops = Arrays.asList(this.config.getWorkshopsUrls().split(","));
            load(workshops);
        } else {
            loadFrom(this.config.getWorkshopUrl(), "default");
            this.config.setDefaultWorkshop("default");
        }
    }

    private void load(List workshops) {
        workshops.forEach(u -> {
            try {
                String url = (String) u;
                this.loadFrom(url, byteToHex(this.digest.digest(url.getBytes())));
            } catch (IOException e) {
                LoggerFactory.getLogger(getClass()).error("Problem loading workshop", e);
            }
        });
    }

    // http://stackoverflow.com/a/9071224/366271
    private String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private Workshop loadFrom(String url, String id) throws IOException {
        LoggerFactory.getLogger(getClass()).info("Loading workshop from {}", url);
        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        Workshop workshop = this.yaml.loadAs(response.body().byteStream(), Workshop.class);
        workshop.resolve(this.modules);
        if(workshop.getId() == null) {
            workshop.setId(id);
        }
        this.workshops.add(workshop.getId(), workshop);
        return workshop;
    }

    @Produces
    public Workshops getWorkshops() {
        return this.workshops;
    }

}
