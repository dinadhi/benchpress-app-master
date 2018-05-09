package actors;

import components.WSComponent;

public class ApplicationActorProtocol {

    public static class Process {
        public final String path;
        public final WSComponent ws;

        public Process(String path, WSComponent ws) {
            this.path = path;
            this.ws = ws;
        }
    }

    public static class Response {
        public final String path;
        public final String status;
        public final Long timeTaken;

        public Response(String path, String status, Long timeTaken) {
            this.path = path;
            this.status = status;
            this.timeTaken = timeTaken;
        }
    }

}
