package org.openshift.evg.workshopper.modules;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openshift.evg.workshopper.config.Configuration;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class ModulesProvider {

    @Inject
    private OkHttpClient client;

    @Inject
    private Configuration config;

    private final Yaml yaml;

    {
        Constructor constructor = new Constructor(Modules.class);
        TypeDescription description;

        description = new TypeDescription(Modules.class);
        description.putMapPropertyType("modules", String.class, Module.class);
        constructor.addTypeDescription(description);

        description = new TypeDescription(Module.class);
        description.putMapPropertyType("revisions", String.class, ModuleRevision.class);
        constructor.addTypeDescription(description);

        yaml = new Yaml(constructor);
    }

    private Modules modules;

    @PostConstruct
    public void initialize() throws IOException {
        reload();
    }

    public void reload() throws IOException {
        String url = "https://raw.githubusercontent.com/"
                + this.config.getRepository() + "/"
                + this.config.getRef()
                + "/_modules.yml";

        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        this.modules = this.yaml.loadAs(response.body().byteStream(), Modules.class);
    }

    @Produces
    public Modules getModules() {
        return modules;
    }

}
