package se.lars.hcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class Main {

  private static HazelcastInstance instance;

  public static void main(String[] args) throws Exception {
    System.setProperty(
      "vertx.logger-delegate-factory-class-name",
      "io.vertx.core.logging.SLF4JLogDelegateFactory"
    );
    String discoDns = System.getenv("disco_dns");
    System.out.println("DISCO DNS: " + discoDns);
    Config config = new Config();
    JoinConfig join = config.getNetworkConfig().getJoin();
    join.getMulticastConfig().setEnabled(discoDns == null);
    join.getKubernetesConfig().setEnabled(discoDns != null).setProperty("service-dns", discoDns);
    instance = Hazelcast.newHazelcastInstance(config);

    VertxOptions options = new VertxOptions().setPreferNativeTransport(true);
    Vertx vertx = Vertx.vertx(options);

    System.out.println("Vertx is using native transport: " + vertx.isNativeTransportEnabled());
    System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
    vertx.createHttpServer().requestHandler(apiRouter(vertx)).listen(8888, Main::httpListenHandler);
    vertx.createHttpServer().requestHandler(monitoringRouter(vertx)).listen(8889, Main::httpListenHandler);
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
    router.route("/*").handler(req -> {
      IAtomicLong data = instance.getAtomicLong("data");
      req.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end("API on host: " + getHostName() + " - counter: " + data.getAndAdd(1));
    });

    return router;
  }

  private static void logRequest(RoutingContext rc) {
    System.out.println("Request: " + rc.request().path());
    rc.next();
  }

  private static String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "Unknow Hostname";
    }
  }

  private static void httpListenHandler(AsyncResult<HttpServer> http) {
    if (http.succeeded()) {
      System.out.println("HTTP server started on port " + http.result().actualPort());
    } else {
      System.out.println("Failed to start");
    }
  }
}