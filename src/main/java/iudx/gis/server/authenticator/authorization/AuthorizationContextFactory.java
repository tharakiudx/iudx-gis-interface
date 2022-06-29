package iudx.gis.server.authenticator.authorization;

public class AuthorizationContextFactory {


  private final static AuthorizationStrategy consumerAuth = new ConsumerAuthStrategy();

  public static AuthorizationStrategy create(IudxRole role) {
    if(role==null){
      throw new IllegalArgumentException(role + "invalid role.");
    }
    switch (role) {
      case CONSUMER: {
        return consumerAuth;
      }
      default:
        throw new IllegalArgumentException(role + "invalid role.");
    }
  }

}
