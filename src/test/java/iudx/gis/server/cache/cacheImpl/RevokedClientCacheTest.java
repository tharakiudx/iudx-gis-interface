package iudx.gis.server.cache.cacheImpl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.common.Constants;
import iudx.gis.server.database.postgres.PostgresService;
import org.checkerframework.checker.signature.qual.DotSeparatedIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class RevokedClientCacheTest {
    RevokedClientCache revokedClientCache;
    private static final CacheType cacheType = CacheType.REVOKED_CLIENT;

    private final Cache<String, String> cache =
            CacheBuilder.newBuilder().maximumSize(5000).expireAfterWrite(1L, TimeUnit.DAYS).build();
    static JsonObject testJson_0 =
            new JsonObject()
                    .put("type", CacheType.REVOKED_CLIENT)
                    .put("key", "revoked_client_id_0")
                    .put("value", "2020-10-18T14:20:00Z");

    static JsonObject testJson_1 =
            new JsonObject()
                    .put("type", CacheType.REVOKED_CLIENT)
                    .put("key", "revoked_client_id_1")
                    .put("value", "2020-10-19T14:20:00Z");
    @Mock
    PostgresService postgresServiceMock;
    @Mock
    Vertx vertx;

    @BeforeEach
    public void setUp(){
        revokedClientCache = new RevokedClientCache(vertx,postgresServiceMock);
    }

    /*@Test
    @DisplayName("Refresh Cache")
    public void refreshCacheTest(VertxTestContext vertxTestContext){
        String query = Constants.SELECT_REVOKE_TOKEN_SQL;
        postgresServiceMock.executeQuery(query, handler->{
           if (handler.succeeded()){
               vertxTestContext.completeNow();
           }
           else{
               vertxTestContext.failNow("Failed");
           }
        });
       // vertxTestContext.completeNow();
    }*/

   /* @Test
    @DisplayName("Refresh Cache")
    public void refreshCacheTest(VertxTestContext vertxTestContext){
        String query = Constants.SELECT_REVOKE_TOKEN_SQL;
        AsyncResult asyncResult= mock(AsyncResult.class);
        AsyncResult asyncResult2= mock(AsyncResult.class);

        JsonObject jsonObject= mock(JsonObject.class);
        JsonObject jsonObject1= new JsonObject().put("_id", "Abc123456")
                .put("expiry","27 05 2022");
        JsonObject jsonObject2= new JsonObject();
        JsonArray jsonArray= new JsonArray();

        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(jsonArray);
        //when(jsonObject.getJsonArray("result")).thenReturn(any());

        when(asyncResult2.succeeded()).thenReturn(true);


        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                ((Handler<AsyncResult<JsonObject>>) invocation.getArgument(1)).handle(asyncResult);
                return null;
            }
        }).when(postgresServiceMock).executeQuery(any(),any());

        vertxTestContext.completeNow();
    }*/
}
