package be.kafana.foursquare.down;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoUtilTest {
//0.3
	@Test
	public void testDistance() {
		double res = GeoUtil.distance( 42.0000000, 20.0000000, 42.0000000, 20.0400000);
		System.out.println(res*1000);
	}
}
