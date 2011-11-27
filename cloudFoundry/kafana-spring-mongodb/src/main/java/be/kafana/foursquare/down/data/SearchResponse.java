package be.kafana.foursquare.down.data;

import java.util.List;

//FIXME Better way of getting data
public class SearchResponse implements IFourSquareNode {

  private List<Venue> venues;
  private List<Photo> photos;


  public List<Venue> getVenues() {
    return venues;
  }


  public void setVenues(List<Venue> venues) {
    this.venues = venues;
  }

}
