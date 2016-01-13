package net.jselby.escapists.game.events.functions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Network IO functions provide accessability to network functions.
 */
public class NetIO extends FunctionCollection {
    private int threadID = 1;

    @Action(subId = 56, id = 80)
    public void OpenURLInBrowser(String url) {
        Gdx.net.openURI(url);
    }

    @Action(subId = 52, id = 80)
    public void URLDoGET(final String url) {
        final ObjectInstance[] instances = scope.getObjects();

        System.out.println("Opening URL for a GET operation: " + url);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Make HTTP request
                HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                Net.HttpRequest httpRequest = requestBuilder.newRequest()
                        .method(Net.HttpMethods.GET)
                        .url(url)
                        .header("User-Agent", "jselby's Escapists Runtime")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .build();
                httpRequest.setTimeOut(60000);
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse response) {
                        System.out.println(response.getStatus().getStatusCode());
                        System.out.println("Successfully fetched content: " + url);
                        final String content = response.getResultAsString();

                        System.out.println(content);

                        // Send it back to the main thread
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                for (ObjectInstance instance : instances) {
                                    instance.getVariables().put("_env_gethttp", content);
                                    instance.getVariables().put("_env_gethttp_completed", true);
                                }
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void cancelled() {
                        // This shouldn't happen.
                        System.err.println("HTTP request was cancelled.");
                    }
                });
            }
        });
        thread.setName("NetIO Thread #" + threadID++);
        thread.start();
    }

    @Condition(subId = 52, id = -81)
    public boolean HasGETRequestCompleted() {
        boolean hasCompleted = false;

        for (ObjectInstance instance : scope.getObjects()) {
            if (instance.getVariables().containsKey("_env_gethttp_completed")) {
                if ((Boolean) instance.getVariables().get("_env_gethttp_completed")) {
                    hasCompleted = true;
                }
                instance.getVariables().remove("_env_gethttp_completed");
            }
        }

        return hasCompleted;
    }
}
