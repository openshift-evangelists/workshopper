package org.openshift.evg.workshopper.modules.cache;

public interface Cache {

    void add(String id, Object data);

    Object get(String id);

    void remove(String id);

    boolean contains(String id);

}
