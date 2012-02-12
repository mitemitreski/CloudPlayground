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
  private final Set<NameValuePair> queryParams;
  private BasicNameValuePair lastLocation;


  public FourSquareVenuesGrabber(OAuthData data, double latitude, double longitude) {
    this.data = data;
    lastLocation = new BasicNameValuePair("ll", String.format("%2.6f,%2.6f", latitude, longitude));
    queryParams = initParams();
  }

  private final Set<NameValuePair> initParams() {
    Set<NameValuePair> params = new HashSet<NameValuePair>();
    params.add(new BasicNameValuePair("client_id", data.getClientId()));
    params.add(new BasicNameValuePair("client_secret", data.getClientSecret()));
    params.add(new BasicNameValuePair("v", new SimpleDateFormat("yyyyMMdd").format(new Date())));
    params.add(new BasicNameValuePair("limit", "49"));
    params.add(new BasicNameValuePair("radius", "4000"));
    // String foodLikeVenuesID = "4d4b7105d754a06374d81259";
    // queryParams.add(new BasicNameValuePair("categoryId",
    // foodLikeVenuesID)); //what is the category of the object
    return params;
  }

  private URI formURI() {
    URI uri = null;
    try {
      uri = URIUtils.createURI("https", "api.foursquare.com", -1, "/v2/venues/search",
          URLEncodedUtils.format(new ArrayList<NameValuePair>(queryParams), "UTF-8"), null);
    } catch (URISyntaxException e) {
      LOGGER.fatal("cannot create url with params " + queryParams, e);
      throw new IllegalArgumentException("wrong params while creatiing url" + queryParams);
    }
    return uri;

  }

  private SearchResponse grabDataFrom(double latitude, double longitude, WebResourcesHelper conn) {
    LOGGER.info("getting info from location ll " + latitude + " " + longitude);
    String json = null;
    queryParams.remove(lastLocation);
    lastLocation = new BasicNameValuePair("ll", String.format("%2.6f,%2.6f", latitude, longitude));
    queryParams.add(lastLocation);
    URI uri = formURI();
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
    WebResourcesHelper connection = new WebResourcesHelper();
    Set<Venue> allResults = new HashSet<Venue>();
    for (double latitude = startLatitiude; latitude < endLatitude; latitude += rate) {
      for (double longitude = startLongitude; longitude < endLongitude; longitude += rate) {
        SearchResponse response = grabDataFrom(latitude, longitude, connection);
        allResults.addAll((response.getVenues()));
        if (allResults.size() > maxItems) {
          return allResults;
        }
        LOGGER.info("Currently found " + allResults.size() + " venues");
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


}
