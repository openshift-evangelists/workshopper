package org.openshift.evg.workshopper.modules.cache;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModuleCacheProvider {

    private Cache cache = new SimpleModuleCache();

    public Cache get() {
        return this.cache;
    }

}
