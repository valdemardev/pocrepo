
package br.com.santander.colaborador.service.discovery;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class RedService {
    @ConfigProperty(name = "red-service-port", defaultValue = "7001") int port;

    /**
     * Start an HTTP server for the red service.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        vertx.createHttpServer()
                .requestHandler(req -> req.response().endAndForget("Hello from Red!"))
                .listenAndAwait(port);
    }

}
