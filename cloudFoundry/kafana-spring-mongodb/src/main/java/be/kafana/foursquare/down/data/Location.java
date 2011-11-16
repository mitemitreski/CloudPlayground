package be.kafana.foursquare.down.data;

import com.google.gson.annotations.SerializedName;

public class Location implements IFourSquareNode {

	private String address;
	@SerializedName(value = "lat")
	private double latitude;
	@SerializedName(value = "lng")
	private double longitude;
	private String postalCode;
	private String city;
	private String state;
	private String country;

	@Override
	public String toString() {
		return String
				.format("Location [address=%s, latitude=%s, longitude=%s, postalCode=%s, city=%s, state=%s, country=%s]",
						address, latitude, longitude, postalCode, city, state,
						country);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// TO-DO this is error prone, fix it, overflow / underflow errors
		double delta = 0.000010D;
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Location other = (Location) obj;
		if (Math.abs(this.latitude - other.latitude) > delta) {
			return false;
		}
		if (Math.abs(this.longitude - other.longitude) > delta) {
			return false;
		}
		return true;
	}

}
