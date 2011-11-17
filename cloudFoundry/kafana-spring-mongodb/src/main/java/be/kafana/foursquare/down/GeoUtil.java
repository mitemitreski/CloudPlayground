package be.kafana.foursquare.down;

public class GeoUtil {

  private GeoUtil() {
  }

  // TODO test this
  /**
   * 
   * 
   * @param lat1
   * @param lon1
   * @param lat2
   * @param lon2
   * @return the distance in km from two locations
   */
  public static double distance(double lat1, double lon1, double lat2, double lon2) {
    double theta = lon1 - lon2;
    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
        * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

    dist = rad2deg(Math.acos(dist));
    dist = dist * 60 * 1.1515;

    dist = dist * 1.609344;// km cost

    return (dist);
  }

  private static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }
}
