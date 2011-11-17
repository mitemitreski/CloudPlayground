package be.kafana.foursquare.down;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import be.kafana.foursquare.core.OAuthData;
import be.kafana.foursquare.core.GetWebResources;
import be.kafana.foursquare.down.data.Entity;
import be.kafana.foursquare.down.data.SearchResponse;
import be.kafana.foursquare.down.data.Venue;

import com.google.gson.Gson;

public class FourSquareVenuesGrabber implements VenueGrabber {

	private OAuthData data;
	private Set<NameValuePair> queryParams = new HashSet<NameValuePair>();

	private BasicNameValuePair lastLocation = new BasicNameValuePair("ll",
			"42.00000,21.39000");

	public FourSquareVenuesGrabber(OAuthData data) {
		super();
		this.data = data;
		// TODO smarter way to init defaults
		initDefaults();
	}

	private void initDefaults() {
		String foodLikeVenuesID = "4d4b7105d754a06374d81259";
		queryParams.add(lastLocation);
		queryParams
				.add(new BasicNameValuePair("client_id", data.getClientId()));
		queryParams.add(new BasicNameValuePair("client_secret", data
				.getClientSecret()));
		queryParams.add(new BasicNameValuePair("v", new SimpleDateFormat(
				"yyyyMMdd").format(new Date())));
		queryParams.add(new BasicNameValuePair("limit", "49"));
		queryParams.add(new BasicNameValuePair("radius", "4000"));
		// queryParams.add(new BasicNameValuePair("categoryId",
		// foodLikeVenuesID)); //what is the category of the object

	}

	private SearchResponse grabDataFrom(double latitude, double longitude) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"getting info from location ll " + latitude + " " + longitude);
		URI uri = null;
		GetWebResources conn = new GetWebResources();
		String json = null;
		try {
			queryParams.remove(lastLocation);
			lastLocation = new BasicNameValuePair("ll", String.format(
					"%2.6f,%2.6f", latitude, longitude));
			queryParams.add(lastLocation);
			uri = URIUtils.createURI("https", "api.foursquare.com", -1,
					"/v2/venues/search",
					URLEncodedUtils.format(new ArrayList<NameValuePair>(
							queryParams), "UTF-8"), null);
//			System.out.println(uri);
			json = conn.getContent(uri.toURL());
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"problem creating URL from ll ", e);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Entity res = new Gson().fromJson(json, Entity.class);
		return res.getResponse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see be.kafana.foursquare.down.VenueGrabber#grabFromNormalized(long,
	 * long, long, long, long)
	 */
	@Override
	public Set<Venue> grabNormalized(int startLatitiude, int startLongitude,
			int endLatitude, int endLongitude, int rate, long maxItems) {

		Set<Venue> allResults = new HashSet<Venue>(Math.abs((int) maxItems));
		for (long latitude = startLatitiude; latitude < endLatitude; latitude += rate) {
			for (long longitude = startLongitude; longitude < endLongitude; longitude += rate) {
				double lat = latitude / 1000000.0;
				double lon = longitude / 1000000.0;
				SearchResponse res = grabDataFrom(lat, lon);
				allResults.addAll((res.getVenues()));
				if (allResults.size() > maxItems) {
					return allResults;
				}
				Logger.getLogger(this.getClass().getName()).log(Level.INFO,
						"found " + allResults.size() + " venues");
			}
		}

		return allResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see be.kafana.foursquare.down.VenueGrabber#getData()
	 */
	@Override
	public OAuthData getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
	 * @see be.kafana.foursquare.down.VenueGrabber#getQueryParams()
	 */
	@Override
	public Set<NameValuePair> getQueryParams() {
		return queryParams;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see be.kafana.foursquare.down.VenueGrabber#setQueryParams(java.util.Set)
	 */
	@Override
	public void setQueryParams(Set<NameValuePair> queryParams) {
		this.queryParams = queryParams;
	}

	@Override
	public Set<Venue> grab(double startLatitiude, double startLongitude,
			double endLatitude, double endLongitude, int rate, long maxItems) {

		final int DECIMAL_NORMALIZER = 1000000;
		int startLat = (int) (startLatitiude * DECIMAL_NORMALIZER);
		int startLon = (int) (startLongitude * DECIMAL_NORMALIZER);
		int endLat = (int) (endLatitude * DECIMAL_NORMALIZER);
		int endLong = (int) (endLongitude * DECIMAL_NORMALIZER);
		return grabNormalized(startLat, startLon, endLat, endLong, rate,
				maxItems);

	}

}
