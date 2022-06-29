package iudx.gis.server.authenticator.authorization;

import java.util.stream.Stream;

public enum IudxRole {
  
  CONSUMER("consumer"),
  CONSUMER2("other");
  private final String role;

  IudxRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }

  public static IudxRole fromRole(final String role) {
    return Stream.of(values())
        .filter(v -> v.role.equalsIgnoreCase(role))
        .findAny()
        .orElse(null);
  }

}
