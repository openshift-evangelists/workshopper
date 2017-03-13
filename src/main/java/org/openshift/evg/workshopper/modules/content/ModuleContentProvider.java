package org.openshift.evg.workshopper.modules.content;

import org.openshift.evg.workshopper.GenericProvider;
import org.openshift.evg.workshopper.config.Configuration;
import org.openshift.evg.workshopper.modules.cache.ModuleCacheProvider;
import org.openshift.evg.workshopper.workshops.Workshop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class ModuleContentProvider extends GenericProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleContentProvider.class);

    private static final Boolean enableCache =
            Boolean.parseBoolean(System.getenv().getOrDefault("CACHE_ENABLED", "false"));

    @Inject
    private ModuleCacheProvider cacheProvider;

    public byte[] loadModule(Workshop workshop, String module) throws IOException {
        LOG.info("Loading content for module {}", module);
        String path = module.replace("_", "/") + ".adoc";
        return loadContent(workshop, path);
    }

    public byte[] loadAsset(Workshop workshop, String path) throws IOException {
        return loadContent(workshop, path);
    }

    public byte[] loadContent(Workshop workshop, String path) throws IOException {
        String url = workshop.getContent() + "/" + path;

        if(this.cacheProvider.get().contains(url)) {
            LOG.info("Content is cached for url {} ", url);
            return (byte[]) this.cacheProvider.get().get(url);
        } else {
            LOG.info("Loading from url {}", url);
        }

        InputStream stream = getStream(url);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int pos;
        while((pos = stream.read(buffer, 0, buffer.length)) > -1) {
            output.write(buffer, 0, pos);
        }
        byte[] content = output.toByteArray();
        if(enableCache) {
            this.cacheProvider.get().add(url, content);
        }
        return content;
    }

}
