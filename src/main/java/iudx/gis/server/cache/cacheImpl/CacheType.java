package iudx.gis.server.cache.cacheImpl;

public enum CacheType {
  REVOKED_CLIENT("revoked_client"),
  NOT_REVOKED_CLIENT("not_revoked_client");

  String cacheName;

  CacheType(String name) {
    this.cacheName = name;
  }
}
