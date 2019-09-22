package se.lars.xfe;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

  public static void main(String[] args) {
    VertxOptions options = new VertxOptions().setPreferNativeTransport(true);
    Vertx vertx = Vertx.vertx(options);

    System.out.println("Vertx is using native transport: " + vertx.isNativeTransportEnabled());
    vertx.createHttpServer().requestHandler(apiRouter(vertx)).listen(8888, listenHandler());
    vertx.createHttpServer().requestHandler(monitoringRouter(vertx)).listen(8889, listenHandler());
  }

  private static Router monitoringRouter(Vertx vertx) {
    Router router = Router.router(vertx);
    router.route("/health").handler(rc -> rc.response()
      .putHeader("content-type", "text/plain")
      .end("OK"));
    router.route("/*").handler(req -> req.response()
      .putHeader("content-type", "text/plain")
      .end("Status on host: " + getHostName()));

    return router;
  }

  private static Router apiRouter(Vertx vertx) {
    Router router = Router.router(vertx);
    router.route("/stop").handler(rc -> {
      vertx.setTimer(1000, __ -> System.exit(77));
      rc.response()
        .putHeader("content-type", "text/plain")
        .end("OK");
    });
    router.route("/*").handler(req -> req.response()
      .putHeader("content-type", "text/plain")
      .end("API on host: " + getHostName()));

    return router;
  }

  private static Handler<AsyncResult<HttpServer>> listenHandler() {
    return http -> {
      if (http.succeeded()) {
        System.out.println("HTTP server started on port " + http.result().actualPort());
      } else {
        System.out.println("Failed to start");
      }
    };
  }


  private static String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "Unknow Hostname";
    }
  }

}
