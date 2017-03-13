package org.openshift.evg.workshopper.config;

import org.openshift.evg.workshopper.GenericProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class Configuration extends GenericProvider {
	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    private String workshopsUrl;
    private String workshopsListUrl;
    private String contentUrl;
    private String defaultWorkshop;

    @PostConstruct
    public void initialize() {
        if(System.getenv().containsKey("CONTENT_URL_PREFIX")) {
            this.contentUrl = System.getenv().get("CONTENT_URL_PREFIX");
        }

        this.workshopsUrl = System.getenv().getOrDefault("WORKSHOPS_URLS",
                "https://raw.githubusercontent.com/osevg/workshopper-workshops/master/default_workshop.yml");
        this.workshopsListUrl = System.getenv().get("WORKSHOPS_LIST_URL");
        this.defaultWorkshop = System.getenv().get("DEFAULT_LAB");

        LOG.info("Loading workshops from: {}", this.workshopsUrl);
        LOG.info("Loading workshops list from: {}", this.workshopsListUrl);
        LOG.info("Default workshop is: {}", this.defaultWorkshop);
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getWorkshopsUrl() {
        return workshopsUrl;
    }

    public String getWorkshopsListUrl() {
        return workshopsListUrl;
    }

    public String getDefaultWorkshop() {
        return defaultWorkshop;
    }

    public void setDefaultWorkshop(String defaultWorkshop) {
        this.defaultWorkshop = defaultWorkshop;
    }

    public Map<String, Object> export() {
        Map<String, Object> export = new HashMap<>();
        export.put("contentUrl", this.contentUrl);
        export.put("defaultWorkshop", this.defaultWorkshop);
        return export;
    }

}
