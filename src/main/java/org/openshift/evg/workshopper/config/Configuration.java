package org.openshift.evg.workshopper.config;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private final String repository;
    private final String ref;
    private ModuleConfiguration config;

    public Configuration() {
        this.repository = System.getenv().getOrDefault("CONTENT_REPOSITORY", "osevg/workshopper-content");
        this.ref = System.getenv().getOrDefault("CONTENT_REF", "master");
    }

    @PostConstruct
    public void initialize() throws IOException {
        reload();
    }

    public void reload() throws IOException {
        String url = "https://raw.githubusercontent.com/"
                + this.getRepository() + "/"
                + this.getRef()
                + "/_config.yml";

        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        this.config = this.yaml.loadAs(response.body().byteStream(), ModuleConfiguration.class);
    }

    public String getRepository() {
        return repository;
    }

    public String getRef() {
        return ref;
    }

    public ModuleConfiguration getConfig() {
        return config;
    }

    public Map<String, Object> export() {
        Map<String, Object> export = new HashMap<>();
        export.put("repository", this.repository);
        export.put("ref", this.ref);
        return export;
    }

}
