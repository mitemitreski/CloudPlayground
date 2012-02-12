package be.kafana.foursquare.down.data;


public class Tip implements IFourSquareNode {

  private String id;
  private String text;
  private long createdAt;
  private String url;
  private Photo photo;
  private Venue venue;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Photo getPhoto() {
    return photo;
  }

  public void setPhoto(Photo photo) {
    this.photo = photo;
  }

  public Venue getVenue() {
    return venue;
  }

  public void setVenue(Venue venue) {
    this.venue = venue;
  }

  public String getId() {
    return id;
  }


}
