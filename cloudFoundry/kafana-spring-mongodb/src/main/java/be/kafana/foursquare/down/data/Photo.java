package be.kafana.foursquare.down.data;


public class Photo implements IFourSquareNode {

  private String id;
  private long createdAt;
  private String url;
  private String source;
  private Tip tip;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }


  public Tip getTip() {
    return tip;
  }


  public void setTip(Tip tip) {
    this.tip = tip;
  }


}
