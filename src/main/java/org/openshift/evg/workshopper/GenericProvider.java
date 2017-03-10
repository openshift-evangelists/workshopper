package org.openshift.evg.workshopper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GenericProvider {

    @Inject
    private OkHttpClient client;

    public OkHttpClient getClient() {
        return client;
    }

    protected InputStream getStream(String url) throws IOException {
        InputStream stream;

        if(url.startsWith("file://")) {
            stream = loadFromFile(url);
        } else {
            stream = loadFromHttp(url);
        }

        return stream;
    }

    protected InputStream loadFromFile(String url) throws IOException {
        return new FileInputStream(url.replaceFirst("file://", ""));
    }

    protected InputStream loadFromHttp(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        return response.body().byteStream();
    }

}
