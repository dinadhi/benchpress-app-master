package components;

import actors.ApplicationActorProtocol.Response;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WSComponent {

    private WSClient ws;

    @Inject
    public WSComponent(WSClient ws) {
        this.ws = ws;
    }

    public CompletionStage<Response> getCall(final String path, final Long startTime) {
        CompletionStage<WSResponse> responsePromise = ws.url(path).get();

        responsePromise.handle((result, error) -> {
            if (error != null) {
                return ws.url("http://www.google.com").get();
            } else {
                return CompletableFuture.completedFuture(result);
            }
        });

        return responsePromise.thenApply(wsResponse -> {
            final Long timeTaken = System.currentTimeMillis() - startTime;
            return new Response(path, wsResponse.getStatusText(), timeTaken);
        });
    }
}
