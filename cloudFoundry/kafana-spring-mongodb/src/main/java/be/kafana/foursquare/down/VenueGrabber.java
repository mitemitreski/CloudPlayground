package be.kafana.foursquare.down;

import java.util.Set;

import org.apache.http.NameValuePair;

import be.kafana.foursquare.core.OAuthData;
import be.kafana.foursquare.down.data.Venue;

public interface VenueGrabber {
	public abstract Set<Venue> grab(double startLatitiude,
			double startLongitude, double endLatitude, double endLongitude,
			int rate, long maxItems);

	public abstract Set<Venue> grabNormalized(int startLatitiude,
			int startLongitude, int endLatitude, int endLongitude, int rate,
			long maxItems);

	public abstract OAuthData getData();

	public abstract void setData(OAuthData data);

	public abstract Set<NameValuePair> getQueryParams();

	public abstract void setQueryParams(Set<NameValuePair> queryParams);

}