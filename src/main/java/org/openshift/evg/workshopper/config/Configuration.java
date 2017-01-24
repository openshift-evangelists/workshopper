package org.openshift.evg.workshopper.config;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class Configuration {

    @Inject
    private OkHttpClient client;

    private final Yaml yaml = new Yaml();
    private final String workshopUrl;
    private final String workshopsUrl;

    private final String contentUrl;
    private ModuleConfiguration config;
    private String defaultWorkshop;

    public Configuration() {
        if(System.getenv().containsKey("CONTENT_URL_PREFIX")) {
            this.contentUrl = System.getenv().get("CONTENT_URL_PREFIX");
        } else {
            this.contentUrl = "https://raw.githubusercontent.com/"
                    + System.getenv().getOrDefault("GITHUB_REPOSITORY", "osevg/workshopper-content")
                    + "/" + System.getenv().getOrDefault("GITHUB_REF", "master");
        }

        this.workshopUrl = System.getenv().getOrDefault("WORKSHOP_URL",
                "https://raw.githubusercontent.com/osevg/workshopper-content/master/_default_workshop.yml");
        this.workshopsUrl = System.getenv().get("WORKSHOPS_URL");
        this.defaultWorkshop = System.getenv().get("DEFAULT_LAB");
    }

    @PostConstruct
    public void initialize() {
        try {
            reload();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Problem loading configuration", e);
        }
    }

    public void reload() throws IOException {
        String url = this.getContentUrl() + "/_config.yml";

        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        this.config = this.yaml.loadAs(response.body().byteStream(), ModuleConfiguration.class);
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getWorkshopUrl() {
        return workshopUrl;
    }

    public String getWorkshopsUrl() {
        return workshopsUrl;
    }

    public String getDefaultWorkshop() {
        return defaultWorkshop;
    }

    public void setDefaultWorkshop(String defaultWorkshop) {
        this.defaultWorkshop = defaultWorkshop;
    }

    public ModuleConfiguration getConfig() {
        return config;
    }

    public Map<String, Object> export() {
        Map<String, Object> export = new HashMap<>();
        export.put("contentUrl", this.contentUrl);
        export.put("defaultWorkshop", this.defaultWorkshop);
        return export;
    }

}
