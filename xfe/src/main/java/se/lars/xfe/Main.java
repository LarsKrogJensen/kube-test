package se.lars.xfe;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.NetServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.IntStream;

import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static java.lang.System.out;
import static java.net.NetworkInterface.getNetworkInterfaces;

public class Main {

  public static void main(String[] args) throws SocketException {
    VertxOptions options = new VertxOptions().setPreferNativeTransport(true);
    Vertx vertx = Vertx.vertx(options);

    System.out.println("Vertx is using native transport: " + vertx.isNativeTransportEnabled());
    System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
    displayMemory();
    displayNics();
    vertx.createHttpServer().requestHandler(apiRouter(vertx)).listen(8888, Main::httpListenHandler);
    vertx.createHttpServer().requestHandler(monitoringRouter(vertx)).listen(8889, Main::httpListenHandler);
    vertx.createNetServer().connectHandler(socket -> socket.handler(buffer -> {
      System.out.println("Echoing: " + buffer.toString());
      socket.write(buffer);
    })).listen(8887, Main::netListenHandler);
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

  private static void httpListenHandler(AsyncResult<HttpServer> http) {
    if (http.succeeded()) {
      System.out.println("HTTP server started on port " + http.result().actualPort());
    } else {
      System.out.println("Failed to start");
    }
  }

  private static void netListenHandler(AsyncResult<NetServer> server) {
    if (server.succeeded()) {
      System.out.println("Server started on port " + server.result().actualPort());
    } else {
      System.out.println("Failed to start");
    }
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

  private static void displayMemory() {
    int mb = 1024 * 1024;

    //Getting the runtime reference from system
    Runtime runtime = Runtime.getRuntime();

    System.out.println("##### Heap utilization statistics [MB] #####");

    System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb + " MB");
    System.out.println("Free Memory:" + runtime.freeMemory() / mb + " MB");
    System.out.println("Total Memory:" + runtime.totalMemory() / mb + " MB");
    System.out.println("Max Memory:" + runtime.maxMemory() / mb + " MB");
  }

  private static void displayNics() throws SocketException {
    for (NetworkInterface netint : Collections.list(getNetworkInterfaces())) {
      out.printf("Display name: %s\n", netint.getDisplayName());
      out.printf("  Name: %s\n", netint.getName());
      out.printf("  Virtual: %s\n", netint.isVirtual());
      out.printf("  Up: %s\n", netint.isUp());
      Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
      for (InetAddress inetAddress : Collections.list(inetAddresses)) {
        out.printf("  InetAddress: %s\n", inetAddress);
        out.printf("    Hostname: %s\n", inetAddress.getHostName());
        out.printf("    Loopback: %s\n", inetAddress.isLoopbackAddress());
      }
      out.print("\n");
    }
  }

}
