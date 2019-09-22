package se.lars.xfe;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.IntStream;

import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

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
    router.route("/*").handler(Main::logRequest);
    router.route("/health").handler(rc -> rc.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end("OK"));
    router.route("/*").handler(req -> req.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end("Status on host: " + getHostName()));

    return router;
  }

  private static Router apiRouter(Vertx vertx) {
    Router router = Router.router(vertx);
    router.route("/*").handler(Main::logRequest);
    router.route("/stop").handler(rc -> {
      vertx.setTimer(1000, __ -> System.exit(77));
      rc.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end("OK");
    });
    router.route("/spin").handler(rc -> {
      vertx.executeBlocking(promise -> {
        IntStream.range(0, 1000).forEach(__ -> spin(500));
        promise.complete();
      }, result -> {
        rc.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end("OK");
      });
    });
    router.route("/*").handler(req -> req.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end("API on host: " + getHostName()));

    return router;
  }

  private static void logRequest(RoutingContext rc) {
    System.out.println("Request: " + rc.request().path());
    rc.next();
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

  private static void spin(int milliseconds) {
    long sleepTime = milliseconds * 1000000L; // convert to nanoseconds
    long startTime = System.nanoTime();
    while ((System.nanoTime() - startTime) < sleepTime) {
    }
  }
}
