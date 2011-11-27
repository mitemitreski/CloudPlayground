package be.kafana.foursquare.down;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import be.kafana.foursquare.core.OAuthData;
import be.kafana.foursquare.core.WebResourcesHelper;
import be.kafana.foursquare.down.data.Entity;
import be.kafana.foursquare.down.data.SearchResponse;
import be.kafana.foursquare.down.data.Venue;

import com.google.gson.Gson;

public class FourSquareVenuesGrabber implements VenueGrabber {

  private static final Log LOGGER = LogFactory.getLog(FourSquareVenuesGrabber.class);
  private OAuthData data;
  private Set<NameValuePair> queryParams = new HashSet<NameValuePair>();

  private BasicNameValuePair lastLocation = new BasicNameValuePair("ll", "42.00000,21.39000");

  public FourSquareVenuesGrabber(OAuthData data) {
    super();
    this.data = data;
    initDefaults();
  }

  private void initDefaults() {
    queryParams.add(lastLocation);
    queryParams.add(new BasicNameValuePair("client_id", data.getClientId()));
    queryParams.add(new BasicNameValuePair("client_secret", data.getClientSecret()));
    queryParams.add(new BasicNameValuePair("v", new SimpleDateFormat("yyyyMMdd").format(new Date())));
    queryParams.add(new BasicNameValuePair("limit", "49"));
    queryParams.add(new BasicNameValuePair("radius", "4000"));
    // String foodLikeVenuesID = "4d4b7105d754a06374d81259";
    // queryParams.add(new BasicNameValuePair("categoryId",
    // foodLikeVenuesID)); //what is the category of the object

  }

  private SearchResponse grabDataFrom(double latitude, double longitude) {
    LOGGER.info("getting info from location ll " + latitude + " " + longitude);
    URI uri = null;
    WebResourcesHelper conn = new WebResourcesHelper();
    String json = null;
    queryParams.remove(lastLocation);
    lastLocation = new BasicNameValuePair("ll", String.format("%2.6f,%2.6f", latitude, longitude));
    queryParams.add(lastLocation);
    try {
      uri = URIUtils.createURI("https", "api.foursquare.com", -1, "/v2/venues/search",
          URLEncodedUtils.format(new ArrayList<NameValuePair>(queryParams), "UTF-8"), null);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    json = conn.getContent(uri);
    Entity res = new Gson().fromJson(json, Entity.class);
    return res.getResponse();
  }

  /*
   * (non-Javadoc)
   * @see be.kafana.foursquare.down.VenueGrabber#grabFromNormalized(long, long,
   * long, long, long)
   */
  @Override
  public Set<Venue> grab(double startLatitiude,
      double startLongitude,
      double endLatitude,
      double endLongitude,
      double rate,
      long maxItems) {

    Set<Venue> allResults = new HashSet<Venue>(Math.abs((int) maxItems));
    for (double latitude = startLatitiude; latitude < endLatitude; latitude += rate) {
      for (double longitude = startLongitude; longitude < endLongitude; longitude += rate) {

        SearchResponse res = grabDataFrom(latitude, longitude);
        allResults.addAll((res.getVenues()));
        if (allResults.size() > maxItems) {
          return allResults;
        }
        LOGGER.info("found " + allResults.size() + " venues");
      }
    }

    return allResults;
  }

  /*
   * (non-Javadoc)
   * @see be.kafana.foursquare.down.VenueGrabber#getData()
   */
  @Override
  public OAuthData getData() {
    return data;
  }

  /*
   * (non-Javadoc)
   * @see
   * be.kafana.foursquare.down.VenueGrabber#setData(be.kafana.foursquare.core
   * .FourSquareAuthData)
   */
  @Override
  public void setData(OAuthData data) {
    this.data = data;
  }

  /*
   * (non-Javadoc)
   * @see be.kafana.foursquare.down.VenueGrabber#getQueryParams()
   */
  @Override
  public Set<NameValuePair> getQueryParams() {
    return queryParams;
  }

  /*
   * (non-Javadoc)
   * @see be.kafana.foursquare.down.VenueGrabber#setQueryParams(java.util.Set)
   */
  @Override
  public void setQueryParams(Set<NameValuePair> queryParams) {
    this.queryParams = queryParams;
  }

}
