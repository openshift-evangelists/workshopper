package org.openshift.evg.workshopper.http;

import okhttp3.OkHttpClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ClientProvider {

    private final OkHttpClient client = new OkHttpClient();

    @Produces
    public OkHttpClient getClient() {
        return this.client;
    }

}
