package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class ApplicationActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(ApplicationActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApplicationActorProtocol.Process.class, process -> {
                    final Long startTime = System.currentTimeMillis();
                    ApplicationActorProtocol.Response response;
                    try {
                        response = process.ws.getCall(process.path, startTime)
                                .toCompletableFuture().get();
                    } catch (Exception ex) {
                        final Long timeTaken = System.currentTimeMillis() - startTime;
                        response = new ApplicationActorProtocol.Response(process.path, "error", timeTaken);
                    }

                    sender().tell(response, self());
                })
                .build();
    }

}
