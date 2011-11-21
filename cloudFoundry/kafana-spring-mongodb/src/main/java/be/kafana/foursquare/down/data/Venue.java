package be.kafana.foursquare.down.data;

import java.util.List;

public class Venue implements IFourSquareNode {

  private String id;
  private String name;
  private String url;
  private Location location;
  private Contact contact;
  private List<Photo> photos;

  @Override
  public String toString() {
    return String.format("Venue [id=%s, name=%s, url=%s, location=%s, contact=%s]", id, name, url,
        location, contact);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((location == null) ? 0 : location.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Venue other = (Venue) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (location == null) {
      if (other.location != null)
        return false;
    } else if (!location.equals(other.location))
      return false;
    return true;
  }

}
