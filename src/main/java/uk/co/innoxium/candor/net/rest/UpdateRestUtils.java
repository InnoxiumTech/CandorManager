package uk.co.innoxium.candor.net.rest;

import org.asynchttpclient.*;


public class UpdateRestUtils {

    private final AsyncHttpClient client;
    public static final UpdateRestUtils instance = new UpdateRestUtils();

    public UpdateRestUtils() {

        DefaultAsyncHttpClientConfig.Builder config = Dsl.config()
//                .setSslContext()
                .setFollowRedirect(true)
                .setUserAgent("InnoxiumTech Candor/0.3.0 (+innoxium.co.uk)");
        client = Dsl.asyncHttpClient(config);
    }

    public ListenableFuture<Response> getCandorLatest() {

        return client.prepareGet("http://innoxium.co.uk/download/candor/latest").execute();
    }
}
