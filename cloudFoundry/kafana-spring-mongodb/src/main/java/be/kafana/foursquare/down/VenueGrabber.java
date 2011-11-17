package be.kafana.foursquare.down;

import java.util.Set;

import org.apache.http.NameValuePair;

import be.kafana.foursquare.core.OAuthData;
import be.kafana.foursquare.down.data.Venue;

public interface VenueGrabber {

  /**
   * 
   * @param startLatitiude
   * @param startLongitude
   * @param endLatitude
   * @param endLongitude
   * @param rate is the rate in witch longitude and latitude will be increased
   * @param maxItems maximal number of Venue's that will be gathered
   * @return
   */
  public abstract Set<Venue> grab(double startLatitiude,
      double startLongitude,
      double endLatitude,
      double endLongitude,
      double rate,
      long maxItems);
   
  
  public abstract OAuthData getData();

  public abstract void setData(OAuthData data);

  public abstract Set<NameValuePair> getQueryParams();

  public abstract void setQueryParams(Set<NameValuePair> queryParams);

}
