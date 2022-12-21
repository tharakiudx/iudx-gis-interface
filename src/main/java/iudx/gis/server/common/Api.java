package iudx.gis.server.common;

import iudx.gis.server.apiserver.ApiServerVerticle;

import static iudx.gis.server.apiserver.util.Constants.*;

public class Api {

    private final String dxApiBasePath;
    private final String adminBasePath;

    public Api(String dxApiBasePath,String adminBasePath) {
        this.dxApiBasePath = dxApiBasePath;
        this.adminBasePath = adminBasePath;
        buildEndpoints();
    }

    private StringBuilder entitiesEndpoint;
    private StringBuilder entitesRegex;


    public void buildEndpoints() {
        entitiesEndpoint = new StringBuilder(dxApiBasePath).append(NGSILD_ENTITIES_URL);
        entitesRegex = new StringBuilder(dxApiBasePath).append(ENTITITES_URL_REGEX);
    }


    public String getEntitiesEndpoint() {
        return entitiesEndpoint.toString();
    }

    public String getEntitesRegex() {
        return entitesRegex.toString();
    }
}
