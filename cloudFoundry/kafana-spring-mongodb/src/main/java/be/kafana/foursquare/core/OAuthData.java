package be.kafana.foursquare.core;

/**
 * Representation of Oauth Data 
 * 
 * @author mite
 * 
 */
public class OAuthData {


  private String clientId;
  private String clientSecret;
  private String redirectUri;

  public OAuthData() {
  }

  public OAuthData(String clientId, String clientSecret, String redirectUri) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

}
