package iudx.gis.server.apiserver.util;

public enum RequestType {
    ENTITY_PATH("entity_path"),
    ENTITY_QUERY("entity_query"),
    ADMIN_CRUD_PATH("admin_crud_schema.json");

    private String filename;

    public String getFilename() {
        return this.filename;
    }

    private RequestType(String fileName) {
        this.filename = fileName;
    }
}
