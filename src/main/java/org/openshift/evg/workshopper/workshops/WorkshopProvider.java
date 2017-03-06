package org.openshift.evg.workshopper.workshops;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.Modules;
import org.openshift.evg.workshopper.modules.ModulesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@ApplicationScoped
public class WorkshopProvider {
	private static final Logger LOG = LoggerFactory.getLogger(WorkshopProvider.class);

    @Inject
    private Configuration config;

    @Inject
    private ModulesProvider modules;

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

        if(this.config.getWorkshopsListUrl() != null) {
        	LOG.info("Reloading workshops from: {}", this.config.getWorkshopsListUrl());
        	
            Request request = new Request.Builder().url(this.config.getWorkshopsListUrl()).build();
            Response response = this.client.newCall(request).execute();
            try {
                List workshops = this.yaml.loadAs(response.body().byteStream(), List.class);
                load(workshops);
            }catch(ClassCastException e){
                LoggerFactory.getLogger(getClass()).error("The provided URL '{}' did not return a List of Workshops", this.config.getWorkshopsListUrl());
            }
        } else if(this.config.getWorkshopsUrl() != null) {
            List workshops = Arrays.asList(this.config.getWorkshopsUrl().split(","));
            load(workshops);
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
        LOG.info("Loading workshop from {}", url);
        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        Workshop workshop = null;
		try {
            InputStream data = response.body().byteStream();
			workshop = this.yaml.loadAs(data, Workshop.class);
			LOG.info("Workshop: {}", workshop);
		} catch (Exception e) {
			throw new IOException("Failed to parse workshop yaml " + url);
		}
        workshop.resolve(this.modules.getModules());

        if(workshop.getId() == null) {
            workshop.setId(id);
        }
        this.workshops.add(workshop.getId(), workshop);
        return workshop;
    }

    public Workshops getWorkshops() {
        return this.workshops;
    }

}
