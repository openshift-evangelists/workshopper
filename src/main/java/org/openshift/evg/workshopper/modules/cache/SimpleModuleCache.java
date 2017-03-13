package org.openshift.evg.workshopper.modules.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleModuleCache implements Cache {

    private Map<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void add(String id, Object data) {
        this.cache.put(id, data);
    }

    @Override
    public Object get(String id) {
        return this.cache.get(id);
    }

    @Override
    public boolean contains(String id) {
        return this.cache.containsKey(id);
    }

    @Override
    public void remove(String id) {
        this.cache.remove(id);
    }

}
