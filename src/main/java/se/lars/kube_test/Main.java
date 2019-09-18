package se.lars.kube_test;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {

  public static void main(String[] args) {
    VertxOptions options = new VertxOptions().setPreferNativeTransport(true);
    Vertx vertx = Vertx.vertx(options);

    System.out.println("Vertx is using native transport: " + vertx.isNativeTransportEnabled());
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        System.out.println("HTTP server started on port 8888");
      } else {
        System.out.println("Failed to start");
      }
    });
  }

}
