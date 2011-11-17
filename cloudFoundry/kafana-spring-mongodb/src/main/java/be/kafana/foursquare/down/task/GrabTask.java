package be.kafana.foursquare.down.task;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.lf5.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import be.kafana.foursquare.down.VenueGrabber;
import be.kafana.foursquare.down.data.Venue;

import com.google.gson.Gson;


public class GrabTask {

  @Autowired(required = false)
  MongoDbFactory mongoDbFactory;
  @Autowired(required = true)
  VenueGrabber venueGrabber;

  @Autowired(required = false)
  MongoTemplate mongoTemplate;

  @Autowired(required = false)
  @Qualifier(value = "serviceProperties")
  Properties serviceProperties;

  @Scheduled(fixedDelay = 5000)
  public void doDownload() {
    Logger.getLogger(getClass().getName().toString()).log(Level.SEVERE, "Currentyly Executing");
    System.out.println("Currently Executing");
  }

  public void doGrab() {


    if (!mongoTemplate.collectionExists(Venue.class)) {
      mongoTemplate.createCollection(Venue.class);
    }
    // 42.378836,23.119812
    Set<Venue> venues = venueGrabber.grab(40.884448, 20.543518, 42.317939, 23.108826, 0.01, 100000);
    Gson gson = new Gson();
    for (Iterator<Venue> iterator = venues.iterator(); iterator.hasNext();) {
      Venue venue = iterator.next();
      mongoTemplate.save(venue);
      System.out.println(gson.toJson(venue));
    }


  }
}
