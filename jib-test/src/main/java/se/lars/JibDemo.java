package se.lars;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JibDemo {
  private static final Logger log = LoggerFactory.getLogger(JibDemo.class);

  public static void main(String[] args) {
    System.setProperty(
      "vertx.logger-delegate-factory-class-name",
      "io.vertx.core.logging.SLF4JLogDelegateFactory"
    );
    Vertx vertx = Vertx.vertx();
    vertx.createHttpServer()
      .requestHandler(rc -> rc.response().end("Hello World"))
      .listen(8080, ar -> {
        if (ar.succeeded()) log.info("Listening on port {}", ar.result().actualPort());
        else log.error("Failed to start", ar.cause());
      });
  }
}
