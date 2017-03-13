package org.openshift.evg.workshopper.modules.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModuleCacheProvider {

    Cache<String, byte[]> cache = CacheBuilder.newBuilder()
            .maximumWeight(1014 * 1024 * 100)
            .weigher(new Weigher<String, byte[]>() {
                @Override
                public int weigh(String key, byte[] value) {
                    return value.length;
                }
            })
            .build();

    public void add(String id, byte[] data) {
        this.cache.put(id, data);
    }

    public byte[] get(String id) {
        return this.cache.getIfPresent(id);
    }

    public boolean contains(String id) {
        return this.cache.asMap().containsKey(id);
    }

    public void remove(String id) {
        this.cache.invalidate(id);
    }

}
