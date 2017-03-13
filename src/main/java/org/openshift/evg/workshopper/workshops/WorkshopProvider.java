package org.openshift.evg.workshopper.workshops;

import org.openshift.evg.workshopper.GenericProvider;
import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.ModulesProvider;
import org.openshift.evg.workshopper.modules.content.ModuleContentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@ApplicationScoped
public class WorkshopProvider extends GenericProvider {
	private static final Logger LOG = LoggerFactory.getLogger(WorkshopProvider.class);

	private static final Boolean preloadCache =
            Boolean.parseBoolean(System.getenv().getOrDefault("CACHE_PRELOAD", "false"));

    @Inject
    private Configuration config;

    @Inject
    private ModulesProvider modules;

    @Inject
    private ModuleContentProvider contentProvider;

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
            try {
                load(this.yaml.loadAs(getStream(this.config.getWorkshopsListUrl()), List.class));
            }catch(ClassCastException e){
                LoggerFactory.getLogger(getClass()).error("The provided URL '{}' did not return a List of Workshops", this.config.getWorkshopsListUrl());
            }
        } else if(this.config.getWorkshopsUrl() != null) {
            load(Arrays.asList(this.config.getWorkshopsUrl().split(",")));
        }
    }

    private void load(List workshops) {
        workshops.forEach(u -> {
            try {
                String url = (String) u;
                this.load(url, byteToHex(this.digest.digest(url.getBytes())));
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

    private Workshop load(String url, String id) throws IOException {
        LOG.info("Loading workshop from {}", url);

        InputStream stream = getStream(url);

        try {
            Workshop workshop = this.yaml.loadAs(stream, Workshop.class);

            if(workshop.getContent() == null) {
                workshop.setContent(this.config.getContentUrl());
            }

            LOG.info("Workshop: {}", workshop);

            this.modules.load(workshop.getContent());
            workshop.resolve(this.modules.getModules().get(workshop.getContent()));

            if(preloadCache) {
                LOG.info("Pre-loading content cache");
                workshop.getSortedModules().forEach(module -> {
                    try {
                        contentProvider.loadModule(workshop, module);
                    } catch (IOException e) {
                        LOG.error("Problem pre-caching module content", e);
                    }
                });
            }

            if(workshop.getId() == null) {
                workshop.setId(id);
            }
            this.workshops.add(workshop.getId(), workshop);
            return workshop;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to parse workshop yaml " + url);
        }
    }

    public Workshops getWorkshops() {
        return this.workshops;
    }

}
