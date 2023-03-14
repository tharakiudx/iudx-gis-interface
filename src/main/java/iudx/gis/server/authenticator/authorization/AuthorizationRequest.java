package iudx.gis.server.authenticator.authorization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AuthorizationRequest {

  private static final Logger LOGGER = LogManager.getLogger(AuthorizationRequest.class);
  private final Method method;
  private final String api;

  public AuthorizationRequest(final Method method, final String api) {
    this.method = method;
    this.api = api;
  }

  public Method getMethod() {
    return method;
  }

  public String getApi() {
    return api;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((api == null) ? 0 : api.hashCode());
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AuthorizationRequest other = (AuthorizationRequest) obj;
    if (!api.equals(other.api)) {
      return false;
    }
    if (method != other.method) {
      return false;
    }
    return true;
  }


}
