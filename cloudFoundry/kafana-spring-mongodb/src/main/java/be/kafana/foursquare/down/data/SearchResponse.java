package be.kafana.foursquare.down.data;

import java.util.List;


public class SearchResponse implements IFourSquareNode {

  private List<Venue> venues;


  public List<Venue> getVenues() {
    return venues;
  }


  public void setVenues(List<Venue> venues) {
    this.venues = venues;
  }

}
