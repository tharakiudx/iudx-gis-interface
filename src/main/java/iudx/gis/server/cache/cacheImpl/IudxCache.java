package iudx.gis.server.cache.cacheImpl;

public interface IudxCache {

  void put(String key, String value);

  String get(String key);

  void refreshCache();
}
