package iudx.gis.server.apiserver.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import iudx.gis.server.authenticate.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CatalogueService {
    private static final Logger LOGGER = LogManager.getLogger(CatalogueService.class);

    private WebClient catWebClient;
    private long cacheTimerid;
    private static String catHost;
    private static int catPort;;
    private static String catSearchPath;
    private static String catItemPath;
    private Vertx vertx;

    private final Cache<String, List<String>> applicableFilterCache =
            CacheBuilder.newBuilder().maximumSize(1000)
                    .expireAfterAccess(Constants.CACHE_TIMEOUT_AMOUNT, TimeUnit.MINUTES).build();

    public CatalogueService(Vertx vertx, JsonObject config) {
        this.vertx=vertx;
        catHost = config.getString("catServerHost");
        catPort = Integer.parseInt(config.getString("catServerPort"));
        catSearchPath = Constants.CAT_RSG_PATH;
        catItemPath = Constants.CAT_ITEM_PATH;

        WebClientOptions options =
                new WebClientOptions().setTrustAll(true).setVerifyHost(false).setSsl(true);
        catWebClient = WebClient.create(vertx, options);
        populateCache();
        cacheTimerid = vertx.setPeriodic(TimeUnit.DAYS.toMillis(1), handler -> {
            populateCache();
        });
    }

    private Future<Boolean> populateCache() {
        Promise<Boolean> promise = Promise.promise();
        catWebClient.get(catPort, catHost, catSearchPath)
                .addQueryParam("property", "[iudxResourceAPIs]")
                .addQueryParam("value", "[[TEMPORAL,ATTR,SPATIAL]]")
                .addQueryParam("filter", "[iudxResourceAPIs,id]").expect(ResponsePredicate.JSON)
                .send(handler -> {
                    if (handler.succeeded()) {
                        JsonArray response = handler.result().bodyAsJsonObject().getJsonArray("results");
                        response.forEach(json -> {
                            JsonObject res = (JsonObject) json;
                            String id = res.getString("id");
                            String[] idArray = id.split("/");
                            if (idArray.length == 4) {
                                applicableFilterCache.put(id + "/*", toList(res.getJsonArray("iudxResourceAPIs")));
                            } else {
                                applicableFilterCache.put(id, toList(res.getJsonArray("iudxResourceAPIs")));
                            }
                        });
                        promise.complete(true);
                    } else if (handler.failed()) {
                        promise.fail(handler.cause());
                    }
                });
        return promise.future();
    }

    private <T> List<T> toList(JsonArray arr) {
        if (arr == null) {
            return null;
        } else {
            return (List<T>) arr.getList();
        }
    }
}
